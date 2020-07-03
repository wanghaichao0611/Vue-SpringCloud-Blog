package com.article.ServiceImpl;

import com.article.Entity.UserArticleCollection;
import com.article.Entity.UserArticleThump;
import com.article.Mapper.UserArticleCollectionMapper;
import com.article.Mapper.UserArticleCommentMapper;
import com.article.Mapper.UserArticleThumpMapper;
import com.article.Service.ArticleCollectionService;
import com.article.Service.ArticleRedisService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ArticleCollectionServiceImpl implements ArticleCollectionService {

    //失败
    private static final  int FAILED=0;
    //成功
    private static final int SUCCESS=1;

    //查看运行日志
    private static final Logger LOGGER= LoggerFactory.getLogger(ArticleCollectionServiceImpl.class);

    //定义点赞博客时加分布式锁对应的key
    private static final String blogLockKey="whcBlogCollectKey";

    //点赞的Mapper
    @Autowired
    UserArticleCollectionMapper userArticleCollectionMapper;

    //创建Redisson的客户端操作
    @Autowired
    private RedissonClient redissonClient;

    //缓存的服务
    @Autowired
    private ArticleRedisService redisService;

    //Redis分布式锁—点赞博客
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int collectLock(Long blogId, Long userId) throws Exception {
        //定义用于获取分布式锁的Redis的key
        final String lockName=blogLockKey+blogId+"-"+userId;
        //获取一次性锁对象
        RLock lock=redissonClient.getLock(lockName);
        //上锁并在15秒钟自动释放，可用于避免Redis节点出现坏死导致死锁。
        lock.lock(15L, TimeUnit.MINUTES);
        try{
            //根据博客Id和用户Id查询用户的收藏记录
            UserArticleCollection userArticleCollection=userArticleCollectionMapper.findAllByBlogIdAndUserId(blogId,userId);
            //判断是否有点赞记录
            if (userArticleCollection==null){
                //没有则插入记录到收藏实体中
                UserArticleCollection collection=new UserArticleCollection();
                collection.setBlogId(blogId);
                collection.setUserId(userId);
                collection.setCollectionTime(new Date());
                collection.setCollectionStatus(1);
                collection.setCollectionActive(1);
                collection.setCreateTime(new Date());
                //插入收藏记录到数据库中
                int total=userArticleCollectionMapper.insert(collection);
                //判断是否插入成功
                if (total>0){
                    //如果成功插入博客的收藏记录则需要把收藏记录放到缓存中
                    redisService.setCacheBlogCollection(blogId,userId,1);
                    LOGGER.info("缓存加入收藏记录"+blogId+","+userId);
                    return SUCCESS;
                }
            }
            //判断是否是二次收藏
            if (userArticleCollection.getCollectionActive()==0 && userArticleCollection.getCollectionStatus()==0){
                //如果是二次收藏则重复缓存操作和数据库更新操作
                int twoResult=userArticleCollectionMapper.updateTwoCollection(blogId,userId);
                if (twoResult>0){
                    //如果成功插入博客的收藏记录则需要把收藏记录放到缓存中
                    redisService.setCacheBlogCollection(blogId,userId,1);
                    LOGGER.info("缓存加入收藏记录"+blogId+","+userId);
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

    //取消收藏博客
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void cancelCollect(Long blogId, Long userId) throws Exception {
        if (blogId!=null && userId!=null){
            //当前用户取消收藏博客
            int result=userArticleCollectionMapper.updateCollectStatusByBlogIdAndUserId(blogId,userId);
            if (result>0){
                //更新缓存中的收藏情况
                LOGGER.info("成功取消收藏博客");
                redisService.setCacheBlogCollection(blogId,userId,0);
            }
        }
    }

    //获取是否完成收藏
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int getCollectButton(Long blogId, Long userId) {
        //根据博客Id和用户Id查询用户的收藏记录
        UserArticleCollection userArticleCollection=userArticleCollectionMapper.findAllByBlogIdAndUserId(blogId,userId);
        if(userArticleCollection==null){
            return SUCCESS;
        }else if (userArticleCollection.getCollectionStatus()==1 || userArticleCollection.getCollectionActive()==1){
            return FAILED;
        }
        //返回成功就说明可以收藏
        return SUCCESS;
    }

    //获取博客的收藏总量
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Long getArticleCollectionTotal(Long blogId, Long userId) throws Exception {
        return redisService.getCacheBlogCollectionTotal(blogId);
    }

    //根据userId分页获取所有收藏记录
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public PageInfo getAllCollection(int page, int count, Long userId) {
        PageHelper.startPage(page,count);
        List<Long> idList=userArticleCollectionMapper.findBlogIdByUserId(userId);
        PageInfo pageInfo=new PageInfo(idList);
        return pageInfo;
    }
}
