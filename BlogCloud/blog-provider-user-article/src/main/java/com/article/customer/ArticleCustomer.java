package com.article.customer;



import com.article.Entity.UserArticle;
import com.article.Entity.UserArticleRank;
import com.article.Entity.UserZeroSign;
import com.article.Mapper.UserArticleMapper;
import com.article.feign.ArticleSignFeign;
import com.article.util.DateTo;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


//监听用户Id，查询当天文章的完成情况
@Service
public class ArticleCustomer {

    //查看运行日志
    private static final Logger LOGGER= LoggerFactory.getLogger(ArticleCustomer.class);

    @Autowired
    UserArticleMapper userArticleMapper;

    @Autowired
    ArticleSignFeign articleSignFeign;

    @Autowired
    RedissonClient redissonClient;

    @RabbitListener(queues = "articleRealQueue")
    public void articleQueue(Long id){
        //监听用户Id，查询当天的文章
        List<UserArticle> userArticleList=userArticleMapper.selectAllByArticleAuthorIdAndTime(DateTo.getTimeFrom(DateTo.getTimesMorning()),DateTo.getTimesMorning(),id);
        //创建负载均衡经验值的标志
        UserZeroSign userZeroSign=new UserZeroSign(false,false,false);
        if (userArticleList==null || userArticleList.isEmpty()){
            //文章已被删除或者未发表任何文章则不需要再认证
            LOGGER.info("文章已被删除或者未发表任何文章");
        }else {
            LOGGER.info("已经进入凌晨扫描时间:"+DateTo.getTimeFrom(DateTo.getTimesMorning())+"至"+DateTo.getTimesMorning());
            //判断文章是否存在有效恢复大于10，有效点赞大于20，排行榜中前10是否有你的文章
            for (int i=0;i<userArticleList.size();i++){
                if (userArticleList.get(i).getArticleComment()>=10){
                    userZeroSign.setComment(true);
                    break;
                }
            }
            for (int i=0;i<userArticleList.size();i++){
                if (userArticleList.get(i).getArticleThumbUp()>=20){
                    userZeroSign.setThump(true);
                    break;
                }
            }
            //定义缓存排行榜的key
            final String key="blogThumpRank";
            //获取Redisson的列表组件RList实例
            RList<UserArticleRank> rList=redissonClient.getList(key);
            //获取当前交叉点的凌晨的排行榜
            List<UserArticleRank> list=rList.readAll();
            for (int i=0;i<list.size();i++){
                for (int j=0;j<userArticleList.size();j++){
                    if (list.get(i).getBlogId().toString().equals(userArticleList.get(j).getId().toString())){
                        //break，更新排行榜的标志
                        userZeroSign.setRank(true);
                        break;
                    }
                }
                if (userZeroSign.getRank()){
                    break;
                }
            }
            //最终负载均衡添加经验值
            if (!userZeroSign.getComment() && !userZeroSign.getThump() && !userZeroSign.getRank()){
                LOGGER.info("当天发表的文章当没有完成任务，所以不需要增加经验值!");
            }else {
                try{
                    articleSignFeign.updateZero(userZeroSign,id);
                    LOGGER.info("完成了一定的任务，可以增加对应的经验值");
                }catch (Exception e){
                    LOGGER.info("凌晨增加经验值出错了");
                }
            }
            LOGGER.info("三个成功的标志:"+userZeroSign.toString());
        }
        //不管是否为null都会去调用，签到那边还会再次判断
        try{
            //重置任务奖励表
            articleSignFeign.deleteArticleSign(id);
        }catch (Exception e){
            LOGGER.info("凌晨重置了文章奖励表");
        }
    }
}
