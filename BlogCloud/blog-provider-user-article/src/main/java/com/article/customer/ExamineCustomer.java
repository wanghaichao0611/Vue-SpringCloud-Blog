package com.article.customer;


import com.article.Controller.ArticleController;
import com.article.Entity.UserArticle;
import com.article.Entity.UserArticleCategory;
import com.article.Mapper.UserArticleCategoryMapper;
import com.article.Mapper.UserArticleMapper;
import com.article.feign.ArticleSignFeign;
import com.article.feign.EsFeign;
import com.article.requset.EsRequest;
import com.article.util.DateTo;
import com.article.util.MyMessagePostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Date;

@Service
public class ExamineCustomer {

    @Autowired
    UserArticleMapper userArticleMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    ArticleSignFeign articleSignFeign;

    @Autowired
    EsFeign esFeign;

    @Autowired
    UserArticleCategoryMapper userArticleCategoryMapper;

    //查看运行日志
    private static final Logger LOGGER= LoggerFactory.getLogger(ExamineCustomer.class);

    @RabbitListener(queues = "examineRealQueue")
    public void examineQueue(Long id) {
        //根据文章Id查询所有信息
        UserArticle userArticle = userArticleMapper.selectAllById(id);
        if (userArticle == null) {
            LOGGER.info("文章已经被删除了");
        } else {
            //检测文章的内容，若是出现暴力色情等就直接删除，没有的话审核通过,测试是直接通过,审核在考虑
            //根据文章主键Id更新审核通过
            userArticleMapper.updateArticleExamineByid(true, id);
            //审核通过后则需要按照正常流程走就行
            //设置延迟队列并且检查是否已经获得过一天一次的发表文章的经验值
            //打开Jedis查询是否完成每天的经验值任务
            Jedis jedis = new Jedis("localhost", 6379);
            if (jedis.get("articleSign-" + userArticle.getArticleAuthorId()) == null) {
                int seconds= DateTo.getSeconds().intValue();//与明日凌晨的时间秒数差
                //如果Redis中不存在文章标志，则需要手动设置当天的有效期，并且增加当天的经验值
                jedis.set("articleSign-" + userArticle.getArticleAuthorId(), "0");
                jedis.expire("articleSign-" + userArticle.getArticleAuthorId(), seconds);
                LOGGER.info("发表文章的时间差为:"+seconds);
                //TTL信息的秒数
                MyMessagePostProcessor myMessagePostProcessor = new MyMessagePostProcessor(seconds*1000);
                //直接发到给延迟队列中，RabbitMq是以最小的TTL为准，发送的是用户Id
                rabbitTemplate.convertAndSend("articleRealExchange","articleRealQueueKey",userArticle.getArticleAuthorId(),myMessagePostProcessor);
                LOGGER.info("成功在消息队列中添加用户Id");
                //可以的话负负载均衡增加用户Id的经验值
                try {
                    articleSignFeign.articleExperience(userArticle.getArticleAuthorId());
                } catch (Exception e) {
                    LOGGER.info("增加文章经验值出错了");
                }
            }else {
                LOGGER.info("消息队列中已经存在用户Id，发过一篇文章凌晨会自动处理");
            }
            jedis.close();

            //必须得到审核的成功标志
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //如果是公开则放到搜索引擎上
                    if (userArticle.getArticleIsPublic()) {
                        try {
                            //查询主键Id分类在哪
                            UserArticleCategory userArticleCategory=userArticleCategoryMapper.selectAllById(userArticle.getArticleCategoryId());
                            //封装ES信息
                            EsRequest esRequest = new EsRequest();
                            esRequest.setArticleId(userArticle.getId());
                            esRequest.setTitle(userArticle.getArticleTitle());
                            esRequest.setSummary(userArticle.getArticleSummary());
                            esRequest.setCategory(userArticleCategory.getCategoryName());
                            esRequest.setArticlePublishDate(new Date());
                            esRequest.setArticleUpdateDate(new Date());
                            //Feign调用
                            Object code=esFeign.addArticle(esRequest).get("success");
                            LOGGER.info("文章放到ES上:"+code);
                        } catch (Exception e) {
                            LOGGER.info("微服务出现网络的错误");
                        }
                    }
                }
            }).start();
        }
    }
}
