package com.article.ServiceImpl;

import com.article.Entity.UserArticle;
import com.article.Entity.UserArticleRank;
import com.article.Entity.UserArticleThump;
import com.article.Mapper.UserArticleMapper;
import com.article.Mapper.UserArticleThumpMapper;
import com.article.Service.ArticleRedisService;
import com.article.Service.ArticleService;
import com.article.Service.ArticleThumpService;
import com.article.feign.ArticleMsgFeign;
import com.article.feign.ArticleSignFeign;
import com.article.util.DateTo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ArticleThumpServiceImpl implements ArticleThumpService {

    //失败
    private static final  int FAILED=0;
    //成功
    private static final int SUCCESS=1;

    //查看运行日志
    private static final Logger LOGGER= LoggerFactory.getLogger(ArticleThumpServiceImpl.class);

    //定义点赞博客时加分布式锁对应的key
    private static final String blogLockKey="whcBlogThumpKey";

    //点赞的Mapper
    @Autowired
    UserArticleThumpMapper userArticleThumpMapper;

    //创建Redisson的客户端操作
    @Autowired
    private RedissonClient redissonClient;

    //缓存的服务
    @Autowired
    private ArticleRedisService redisService;

    //负载均衡增加经验值
    @Autowired
    ArticleSignFeign articleSignFeign;

    //负载均衡告知点赞信息
    @Autowired
    ArticleMsgFeign articleMsgFeign;

    @Autowired
    UserArticleMapper userArticleMapper;

    //Redis分布式锁—点赞博客
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int thumpLock(Long blogId, Long userId) throws Exception {
        //查询当前文章
        UserArticle userArticle=userArticleMapper.selectAllById(blogId);
        //定义用于获取分布式锁的Redis的key
        final String lockName=blogLockKey+blogId+"-"+userId;
        //获取一次性锁对象
        RLock lock=redissonClient.getLock(lockName);
        //上锁并在10秒钟自动释放，可用于避免Redis节点出现坏死导致死锁。
        lock.lock(10L, TimeUnit.MINUTES);
        try{
            //根据博客Id和用户Id查询用户的点赞记录
            UserArticleThump userArticleThump=userArticleThumpMapper.findAllByBlogIdAndUserId(blogId,userId);
            //判断是否有点赞记录(首次点赞才会有消耗每日的次数)
            if (userArticleThump==null){
                //没有则插入记录到点赞实体中
                UserArticleThump thump=new UserArticleThump();
                thump.setBlogId(blogId);
                thump.setUserId(userId);
                thump.setThumpTime(new Date());
                thump.setThumpStatus(1);
                thump.setThumpActive(1);
                thump.setCreateTime(new Date());
                //插入点赞记录到数据库中
                int total=userArticleThumpMapper.insert(thump);
                //判断是否插入成功
                if (total>0){
                    //如果成功插入博客的点赞记录则需要把点赞记录放到缓存中
                    redisService.setCacheBlog(blogId,userId,1);
                    //更新文章的排行榜
                    this.cacheThumpTotal();
                    LOGGER.info("缓存加入点赞记录"+blogId+","+userId);
                    //多线程完成后续不太相关的操作
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //与明日凌晨的时间秒数差
                            int seconds= DateTo.getSeconds().intValue();
                            Jedis jedis = new Jedis("localhost", 6379);
                            try{
                                if (jedis.get("thumpSign-"+userId)==null){
                                    //如果Redis中不存在文章标志，则需要手动设置当天的有效期，并且增加当天的经验值
                                    jedis.set("thumpSign-" + userId, "9");
                                    jedis.expire("thumpSign-" + userId, seconds);
                                    LOGGER.info("点赞文章的时间差为:"+seconds);
                                    //负载均衡完成点赞每日任务
                                    articleSignFeign.thumpSign(userId);
                                }else if (Integer.parseInt(jedis.get("thumpSign-"+userId))>0){
                                    //Redis点赞缓存减少一次机会
                                    jedis.set("thumpSign-"+userId,String.valueOf(Integer.parseInt(jedis.get("thumpSign-"+userId))-1));
                                    jedis.expire("thumpSign-" + userId, seconds);
                                    //负载均衡完成点赞每日任务
                                    articleSignFeign.thumpSign(userId);
                                }else {
                                    //次数未0则不再增加经验值
                                    LOGGER.info("次数已经用完不能再获得点赞的经验值");
                                }
                            }catch (Exception e){
                                LOGGER.info("负载均衡点赞每日出现错误!");
                            }finally {
                                if (jedis!=null){
                                    jedis.close();
                                }
                            }
                            //负载均衡发送点赞信息(必须不是本人点赞，并且文章不在审核期中)
                            if (userArticle!=null && userArticle.getArticleExamine() &&
                                    !userArticle.getArticleAuthorId().equals(userId)){
                                //负载均衡发送点赞消息
                                articleMsgFeign.thumpMsg(userId,userArticle.getArticleAuthorId(),blogId,userArticle.getArticleTitle());
                                LOGGER.info("发送了一条点赞信息");
                            }
                        }
                    }).start();
                    return SUCCESS;
                }
            }
            //判断是否是二次点赞(文章已经点赞过了，取消后再点赞的行为)
            if (userArticleThump.getThumpActive()==0 && userArticleThump.getThumpStatus()==0){
                //如果是二次点赞则重复缓存操作和数据库更新操作
                int twoResult=userArticleThumpMapper.updateTwoThump(blogId,userId);
                if (twoResult>0){
                    //如果成功插入博客的点赞记录则需要把点赞记录放到缓存中
                    redisService.setCacheBlog(blogId,userId,1);
                    //更新文章的排行榜
                    this.cacheThumpTotal();
                    LOGGER.info("缓存加入点赞记录"+blogId+","+userId);
                    return SUCCESS;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if (lock!=null){
                //释放锁
                lock.unlock();
            }
        }
        return FAILED;
    }

    //取消点赞博客
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void cancelThump(Long blogId, Long userId) throws Exception {
        if (blogId!=null && userId!=null){
            //当前用户取消点赞博客(也可以直接删除表中的点赞记录)
            int result=userArticleThumpMapper.updateThumpStatusByBlogIdAndUserId(blogId,userId);
            //int result=userArticleThumpMapper.deleteByBlogIdAndUserId(blogId,userId);
            if (result>0){
                //更新缓存中的点赞情况
                LOGGER.info("成功取消点赞博客");
                redisService.setCacheBlog(blogId,userId,0);
                //更新文章的排行榜
                this.cacheThumpTotal();
            }
        }
    }

    //获取博客的点赞总量
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Long getArticleThumpTotal(Long blogId, Long userId) throws Exception {
        return redisService.getCacheBlogTotal(blogId);
    }

    //获取是否完成点赞
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int getThumpButton(Long blogId, Long userId) {
        //根据博客Id和用户Id查询用户的点赞记录
        UserArticleThump userArticleThump=userArticleThumpMapper.findAllByBlogIdAndUserId(blogId,userId);
        if(userArticleThump==null){
            return SUCCESS;
        }else if (userArticleThump.getThumpStatus()==1 || userArticleThump.getThumpActive()==1){
            return FAILED;
        }
        //返回成功就说明可以点赞
        return SUCCESS;
    }

    //获取博客点赞总数排行榜
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Collection<UserArticleRank> getArticleRank() throws Exception {
        return  redisService.getArticleRank();
    }

    //将当前博客id对应的点赞总数构造为实体加入RList中
    private void cacheThumpTotal(){
        try{
            redisService.rankBlogThump();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
