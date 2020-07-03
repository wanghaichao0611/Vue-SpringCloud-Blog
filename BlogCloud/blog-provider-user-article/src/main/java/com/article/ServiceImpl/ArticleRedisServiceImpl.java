package com.article.ServiceImpl;

import com.article.Entity.UserArticleRank;
import com.article.Mapper.UserArticleThumpMapper;
import com.article.Service.ArticleRedisService;
import com.article.util.DateTo;
import org.assertj.core.util.Strings;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class ArticleRedisServiceImpl implements ArticleRedisService {

    //查看运行日志
    private static final Logger LOGGER= LoggerFactory.getLogger(ArticleRedisServiceImpl.class);

    //定义点赞博客缓存存储时的key
    private static final String keyBlog="whcBlogCacheThumpKey";

    //定义收藏博客缓存存储时的key
    private static final String keyBlogCollection="whcBlogCacheCollectionKey";

    @Autowired
    private UserArticleThumpMapper userArticleThumpMapper;

    @Autowired
    private RedissonClient redissonClient;


    //博客的点赞与取消点赞的缓存服务
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void setCacheBlog(Long blogId, Long userId, Integer status) throws Exception {
        //创建用户获取分布式锁的key
        final String lockName=new StringBuffer("whcBlogThumpKey").append(blogId).append(userId).append(status).toString();
        //获取分布式锁实例
        RLock rLock=redissonClient.getLock(lockName);
        //可重入锁的方式获取分布式锁
        Boolean suo=rLock.tryLock(100,10, TimeUnit.MINUTES);
        try{
            if (suo){
                //如果锁==true则说明可以进行缓存的添加
                if (blogId!=null && userId!=null && status!=null){
                    //设置缓存中的key
                    final String key=blogId+":"+userId;
                    //定义Redisson中的RMap据结构
                    RMap<String,Integer> rMap=redissonClient.getMap(keyBlog);
                    if (status==1){
                        //添加点赞操作
                        rMap.putIfAbsent(key,Integer.parseInt(userId.toString()));
                    }else if (status==0){
                        //取消点赞操作
                        rMap.remove(key);
                        //删除点赞表中的点赞记录
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //释放锁
            if(rLock!=null){
                rLock.forceUnlock();
            }
        }
    }

    //获取博客的总点赞数
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Long getCacheBlogTotal(Long blogId) throws Exception {
        //定义返回的最终的结果
        Long resultTotal=0L;
        if (blogId!=null){
            //获取key-value的值列表
            RMap<String,Integer> rMap=redissonClient.getMap(keyBlog);
            Map<String,Integer> dataMap=rMap.readAllMap();
            //判断取出来是否有值
            if (dataMap!=null && dataMap.keySet()!=null){
                //获取Map的键值，id：id的格式
                Set<String> set=dataMap.keySet();
                Integer bId;
                //循环键列表，查询是否存在当前博客id开头的数据记录
                for (String key:set){
                    if (!Strings.isNullOrEmpty(key)){
                        String[] arr=key.split(":");
                        if (arr!=null && arr.length>0){
                            bId=Integer.valueOf(arr[0]);
                            //判断当前取出的键对应的博客Id是否跟当前待比较的博客Id相等
                            //如果相等则代表有一个点赞记录，则结果需要加1
                            if (blogId.intValue()==bId){
                                resultTotal +=1;
                            }
                        }
                    }
                }
            }
        }
        //最后返回统计的结果
        return resultTotal;
    }

    //触发博客点赞赞总数排行榜
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void rankBlogThump() throws Exception {
        //获取当前系统时间和从0点开始的时间
        //Date from= DateTo.getTimesMorning();
        //获取这个时间的上一年用于测试
        Date from=DateTo.lastYear();
        Date to=DateTo.getTonight();
        //定义用于缓存排行榜的key
        final String key="blogThumpRank";
        //根据数据库查询已经排序好的博客实体对象列表
        List<UserArticleRank> list=userArticleThumpMapper.getTotalRank(from,to);
        //判断列表中是否有数据
        if (list!=null && list.size()>0){
            //获取Redisson的列表组件RList实例
            RList<UserArticleRank> rList=redissonClient.getList(key);
            //先清空缓存中的列表数据
            rList.clear();
            //将得到最新的排行榜更新至缓存中
            rList.addAll(list);
        }
    }

    //获得博客点赞排行榜
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public List<UserArticleRank> getArticleRank() throws Exception {
        //定义缓存排行榜的key
        final String key="blogThumpRank";
        //获取Redisson的列表组件RList实例
        RList<UserArticleRank> rList=redissonClient.getList(key);
        //获取缓存中最新的排行榜
        return rList.readAll();
    }

    //缓存当前用户的收藏博客的记录
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void setCacheBlogCollection(Long blogId, Long userId, Integer status) throws Exception {
        //创建用户获取分布式锁的key
        final String lockName=new StringBuffer("whcBlogCollectKey").append(blogId).append(userId).append(status).toString();
        //获取分布式锁实例
        RLock rLock=redissonClient.getLock(lockName);
        //可重入锁的方式获取分布式锁
        Boolean suo=rLock.tryLock(100,10, TimeUnit.MINUTES);
        try{
            if (suo){
                //如果锁==true则说明可以进行缓存的添加
                if (blogId!=null && userId!=null && status!=null){
                    //设置缓存中的key(::是收藏，只是为了区别，用什么都行)
                    final String key=blogId+"::"+userId;
                    //定义Redisson中的RMap据结构
                    RMap<String,Integer> rMap=redissonClient.getMap(keyBlogCollection);
                    if (status==1){
                        //添加收藏操作
                        rMap.putIfAbsent(key,Integer.parseInt(userId.toString()));
                    }else if (status==0){
                        //取消收藏操作
                        rMap.remove(key);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //释放锁
            if(rLock!=null){
                rLock.forceUnlock();
            }
        }
    }

    //获取博客的总收藏数
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Long getCacheBlogCollectionTotal(Long blogId) throws Exception {
        //定义返回的最终的结果
        Long resultTotal=0L;
        if (blogId!=null){
            //获取key-value的值列表
            RMap<String,Integer> rMap=redissonClient.getMap(keyBlogCollection);
            Map<String,Integer> dataMap=rMap.readAllMap();
            //判断取出来是否有值
            if (dataMap!=null && dataMap.keySet()!=null){
                //获取Map的键值，id：id的格式
                Set<String> set=dataMap.keySet();
                Integer bId;
                //循环键列表，查询是否存在当前博客id开头的数据记录
                for (String key:set){
                    if (!Strings.isNullOrEmpty(key)){
                        //收藏是::，只是为了区别，用什么都行
                        String[] arr=key.split("::");
                        if (arr!=null && arr.length>0){
                            bId=Integer.valueOf(arr[0]);
                            //判断当前取出的键对应的博客Id是否跟当前待比较的博客Id相等
                            //如果相等则代表有一个点赞记录，则结果需要加1
                            if (blogId.intValue()==bId){
                                resultTotal +=1;
                            }
                        }
                    }
                }
            }
        }
        //最后返回统计的结果
        return resultTotal;
    }
}
