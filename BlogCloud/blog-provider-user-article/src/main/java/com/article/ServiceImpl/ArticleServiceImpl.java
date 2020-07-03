package com.article.ServiceImpl;

import com.article.Entity.*;
import com.article.Mapper.*;
import com.article.Service.ArticleService;
import com.article.feign.ArticleSignFeign;
import com.article.response.ArticleRank;
import com.article.response.BlogFrontPublish;
import com.article.requset.EsRequest;
import com.article.util.DateTo;
import com.article.util.MyMessagePostProcessor;
import com.article.util.StringFromHtmlUtil;
import com.article.feign.EsFeign;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.youbenzi.mdtool.tool.MDTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.List;


@Service
public class ArticleServiceImpl implements ArticleService {

    //失败
    private static final  int FAILED=0;
    //成功
    private static final int SUCCESS=1;

    //博客文章表
    @Autowired
    UserArticleMapper userArticleMapper;

    //博客文章标签表
    @Autowired
    UserArticleTagMapper userArticleTagMapper;

    //博客文章分类表
    @Autowired
    UserArticleCategoryMapper userArticleCategoryMapper;

    //调用ES的Feign
    @Autowired
    EsFeign esFeign;

    //RabbitMQ
    @Autowired
    RabbitTemplate rabbitTemplate;

    //articleFeign
    @Autowired
    ArticleSignFeign articleSignFeign;

    //控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(ArticleServiceImpl.class);

    //发布一个个人博客文章
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void articlePublish(BlogFrontPublish blogFrontPublish, Long id) {
        //获取文章的摘要markdown格式-html-summary
        String words= StringFromHtmlUtil.getString(MDTool.markdown2Html(blogFrontPublish.getArticleContent()));
        //获取文章的摘要且摘要长度为255个字符
        String summary = words.length() > 240 ? words.substring(0, 240) + "......" : words;
        //去除转换后存在的空格
        String tagTar = blogFrontPublish.getArticleTag().replaceAll(" ", "");
        //控制台看标签结果
        LOGGER.info("转换后的数组为:"+tagTar);
        //插入文章的参数
        UserArticle userArticle=new UserArticle();
        userArticle.setArticleAuthorId(id);
        userArticle.setArticleTitle(blogFrontPublish.getArticleTitle());
        userArticle.setArticleMdContent(blogFrontPublish.getArticleContent());
        userArticle.setArticleReading(0);
        userArticle.setArticleThumbUp(0);
        userArticle.setArticleComment(0);
        userArticle.setArticleCollection(0);
        userArticle.setArticleForward(0);
        userArticle.setArticleExamine(false);
        if (blogFrontPublish.getArticleType().equals("original")){
            userArticle.setArticleIsOriginal(true);
            userArticle.setArticleIsForward(false);
            userArticle.setArticleIsTranslate(false);
        }
        if (blogFrontPublish.getArticleType().equals("forward")){
            userArticle.setArticleIsForward(true);
            userArticle.setArticleIsOriginal(false);
            userArticle.setArticleIsTranslate(false);
            userArticle.setArticleForwardUrl(blogFrontPublish.getForWardUrl());
        }
        if (blogFrontPublish.getArticleType().equals("translate")){
            userArticle.setArticleIsTranslate(true);
            userArticle.setArticleIsOriginal(false);
            userArticle.setArticleIsForward(false);
            userArticle.setArticleTranslateUrl(blogFrontPublish.getTranslateUrl());
        }
        if (blogFrontPublish.getArticleSecurity().equals("public")){
            userArticle.setArticleIsPublic(true);
            userArticle.setArticleIsPrivate(false);
            userArticle.setArticleIsVip(false);
        }
        if (blogFrontPublish.getArticleSecurity().equals("private")){
            userArticle.setArticleIsPrivate(true);
            userArticle.setArticleIsPublic(false);
            userArticle.setArticleIsVip(false);
        }
        if (blogFrontPublish.getArticleSecurity().equals("vip")){
            userArticle.setArticleIsVip(true);
            userArticle.setArticleIsPublic(false);
            userArticle.setArticleIsPrivate(false);
        }
        //插入更新时间
        userArticle.setArticlePublishDate(new Date());
        userArticle.setArticleUpdateDate(new Date());
        //插入摘要和发布时间，把本次发布时间作为最后的更新时间，若再更新则可以直接更新最后的时间。
        userArticle.setArticleSummary(summary);
        //防止出现NULL
        userArticle.setArticleTagsId("");

        //将文章的分类写入分类表然后再插入整篇文章
        UserArticleCategory userArticleCategory=userArticleCategoryMapper.findAllByCategoryName(blogFrontPublish.getArticleCategory());
        if (userArticleCategory==null){
            userArticleCategory=new UserArticleCategory();
            userArticleCategory.setCategoryArticles("");
            userArticleCategory.setCategoryName(blogFrontPublish.getArticleCategory());
            //返回获取到的自增ID
            userArticleCategoryMapper.insert(userArticleCategory);
        }
        userArticle.setArticleCategoryId(userArticleCategory.getId());
        //文章存入数据库并且获取到自增的ID
        userArticleMapper.insert(userArticle);

        //更新分类表的数据到里面
        userArticleCategory.setCategoryArticles(userArticleCategory.getCategoryArticles()+userArticle.getId()+",");
        userArticleCategoryMapper.updateCategoryNameAndCategoryArticlesById(userArticleCategory.getCategoryName(),userArticleCategory.getCategoryArticles(),userArticleCategory.getId());

        //把标签写入数据库
        for (String tag :tagTar.split(",")) {
            if (tag.replaceAll(" ", "").length() == 0) {
                //单个标签只含空格
                continue;
            }
            UserArticleTag userArticleTag = userArticleTagMapper.findAllByTagName(tag);
            if (userArticleTag==null){
                userArticleTag=new UserArticleTag();
                userArticleTag.setTagName(tag);
                userArticleTag.setTagArticles("");
                userArticleTagMapper.insert(userArticleTag);
            }
            //转换后的值再更新得到文章表的主键
            userArticleTag.setTagArticles(userArticleTag.getTagArticles()+userArticle.getId()+",");
            userArticle.setArticleTagsId(userArticle.getArticleTagsId()+userArticleTag.getId()+",");
            userArticleTagMapper.updateTagNameAndTagArticlesById(userArticleTag.getTagName(),userArticleTag.getTagArticles(),userArticleTag.getId());
        }
        //审核博客文章的延迟队列(文章主键Id)
        rabbitTemplate.convertAndSend("examineRealExchange","examineRealQueueKey",userArticle.getId());
        LOGGER.info("文章主键Id开始审核:"+userArticle.getId());

        //最终再次更新文章表
        userArticleMapper.updateArticleTagsIdByArticleId(userArticle.getArticleTagsId(),userArticle.getId());
    }

    //获取本人发过的所有博客
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public PageInfo articlePage(int page, int count, Long id) {
        PageHelper.startPage(page,count);
        List<UserArticle> userArticleList=userArticleMapper.selectAllByArticleAuthorId(id);
        PageInfo pageInfo=new PageInfo(userArticleList);
        return pageInfo;
    }

    //鉴定文章ID与作者ID是否一致，不一致的话再判断是否公开，私密的话则返回终止。
    //私密的文章不会传给前端显示，但是作者本人还要修改，所以防止手动篡改，所以加上整个保个险。
    //若文章ID与作者ID保持一致，则前端允许显示编辑的按钮(无法阅读审核中的文章，需要等审核通过)
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int checkAdmin(Long articleId, Long id) {
        UserArticle userArticle=userArticleMapper.selectAllById(articleId);
        if (userArticle.getArticleAuthorId().equals(id)){
            //如果两者成立，则是本人操作，不需要访问量+1。(个人可以查看个人正在审核的文章)
            return SUCCESS;
        }
        else if (!userArticle.getArticleAuthorId().equals(id) && !userArticle.getArticleIsPrivate() && userArticle.getArticleExamine()){
            //如果两者不成立，则不是本人操作，需要访问量+1。
            userArticleMapper.updateArticleReadingById(articleId);
            return SUCCESS;
        }else {
            return FAILED;
        }
    }

    //通过文章ID查找文章的信息
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public UserArticle findArticleAllById(Long articleId) {
        UserArticle userArticle=userArticleMapper.selectAllById(articleId);
        return userArticle;
    }

    //删除本人的文章
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int articleDeleteOne(Long articleId,Long authorId) {
        //判断要删除文章的作者Id是否与用户Id一致
        Long realAuthorId = userArticleMapper.selectArticleAuthorIdById(articleId);
        if (realAuthorId.equals(authorId)) {
            //如果一致则删除整篇文章和和与之关联的表
            Long categoryId = userArticleMapper.selectArticleCategoryIdById(articleId);
            String tagsId = userArticleMapper.selectArticleTagsIdById(articleId);
            //删除分类表中包含的文章Id
            UserArticleCategory userArticleCategory = userArticleCategoryMapper.selectAllById(categoryId);
            userArticleCategory.setCategoryArticles(userArticleCategory.getCategoryArticles().replaceAll(articleId + ",", ""));
            //更新分类表
            userArticleCategoryMapper.updateCategoryArticlesById(userArticleCategory.getCategoryArticles(), categoryId);
            //删除标签表中包含文章的Id
            if (tagsId.length() > 0) {
                for (String a : tagsId.split(",")) {
                    if (a != null) {
                        //查询标签
                        UserArticleTag userArticleTag = userArticleTagMapper.selectAllById(Long.parseLong(a));
                        //去除标签中的articleId中的待删除的文章id
                        userArticleTag.setTagArticles(userArticleTag.getTagArticles().replaceAll(articleId + ",", ""));
                        //更新标签表
                        userArticleTagMapper.updateTagArticlesById(userArticleTag.getTagArticles(), Long.parseLong(a));
                    }
                }
            }
            //如果删除的是私密文章则不需要ES
            if (!userArticleMapper.selectArticleIsPrivateById(articleId)) {
                //删除ES上面的文章
                try {
                    //把文章Id放入Es
                    EsRequest esRequest = new EsRequest();
                    esRequest.setCategory(userArticleCategory.getCategoryName());
                    esRequest.setArticleId(articleId);
                    //Es的Feign负载均衡调用删除
                    Object code = esFeign.deleteArticle(esRequest).get("success");
                    LOGGER.info(code + "");
                } catch (Exception e) {
                    LOGGER.info("微服务调用错误!");
                }
            }
            //删除指定的整篇文章
            userArticleMapper.deleteById(articleId);
            return SUCCESS;
        }
        return FAILED;
    }

    //通过文章ID查找文章的信息(List)
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public List<UserArticle> findArticleListAllById(Long articleId) {
        List<UserArticle> userArticle=userArticleMapper.selectListAllById(articleId);
        return userArticle;
    }

    //分页获取这个时间段的作者的文章
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public PageInfo findVisitAll(int page, int count,Date from,Date to) {
        PageHelper.startPage(page,count);
        List<UserArticle> userArticleAll=userArticleMapper.findAll(from,to);
        PageInfo pageInfo=new PageInfo(userArticleAll);
        return pageInfo;
    }

    //查询时间差的AuthorID
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public List<Long> articleAuthorAll(Date from, Date to) {
        List<Long> authorIdAll=userArticleMapper.selectArticleAuthorId(from,to);
        return authorIdAll;
    }

    //获取所有最新发的List<UserArticle>文章
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public List<UserArticle> articleAllPageId(Date from, Date to) {
        List<UserArticle> userArticle=userArticleMapper.findAll(from,to);
        return userArticle;
    }

    //验证数据库中的作者Id与用户表的Id是否一致
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int checkId(Long articleId, Long authorId) {
        //判断要删除文章的作者Id是否与用户Id一致
        Long realAuthorId=userArticleMapper.selectArticleAuthorIdById(articleId);
        if (realAuthorId.equals(authorId)){
            return SUCCESS;
        }
        return FAILED;
    }

    //更新文章的全部信息
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void articleUpdate(BlogFrontPublish blogFrontPublish, Long authorId) {
        //确定是自己的文章且主键Id一致
        if (userArticleMapper.selectAllByIdAndArticleAuthorId(blogFrontPublish.getId(),authorId)!=null) {
            //获取文章的摘要markdown格式-html-summary
            String words = StringFromHtmlUtil.getString(MDTool.markdown2Html(blogFrontPublish.getArticleContent()));
            //获取文章的摘要且摘要长度为255个字符
            String summary = words.length() > 240 ? words.substring(0, 240) + "......" : words;
            //去除转换后存在的空格
            String tagTar = blogFrontPublish.getArticleTag().replaceAll(" ", "");
            //控制台看标签结果
            LOGGER.info("转换后的数组为" + tagTar);
            UserArticle userArticle = new UserArticle();
            userArticle.setId(blogFrontPublish.getId());
            userArticle.setArticleAuthorId(authorId);
            userArticle.setArticleTitle(blogFrontPublish.getArticleTitle());
            userArticle.setArticleSummary(summary);
            userArticle.setArticleMdContent(blogFrontPublish.getArticleContent());
            //修改最后更新时间
            userArticle.setArticleUpdateDate(new Date());
            //修改所有的权限
            if (blogFrontPublish.getArticleType().equals("original")) {
                userArticle.setArticleIsOriginal(true);
                userArticle.setArticleIsForward(false);
                userArticle.setArticleIsTranslate(false);
            }
            if (blogFrontPublish.getArticleType().equals("forward")) {
                userArticle.setArticleIsForward(true);
                userArticle.setArticleIsOriginal(false);
                userArticle.setArticleIsTranslate(false);
                userArticle.setArticleForwardUrl(blogFrontPublish.getForWardUrl());
            }
            if (blogFrontPublish.getArticleType().equals("translate")) {
                userArticle.setArticleIsTranslate(true);
                userArticle.setArticleIsOriginal(false);
                userArticle.setArticleIsForward(false);
                userArticle.setArticleTranslateUrl(blogFrontPublish.getTranslateUrl());
            }
            if (blogFrontPublish.getArticleSecurity().equals("public")) {
                userArticle.setArticleIsPublic(true);
                userArticle.setArticleIsPrivate(false);
                userArticle.setArticleIsVip(false);
            }
            if (blogFrontPublish.getArticleSecurity().equals("private")) {
                userArticle.setArticleIsPrivate(true);
                userArticle.setArticleIsPublic(false);
                userArticle.setArticleIsVip(false);
            }
            if (blogFrontPublish.getArticleSecurity().equals("vip")) {
                userArticle.setArticleIsVip(true);
                userArticle.setArticleIsPublic(false);
                userArticle.setArticleIsPrivate(false);
            }

            //防止出现NULL
            userArticle.setArticleTagsId("");

            //分类关联的Id
            Long categoryId = userArticleMapper.selectArticleCategoryIdById(blogFrontPublish.getId());
            //删除分类表中包含的文章Id
            UserArticleCategory userArticleCategory = userArticleCategoryMapper.selectAllById(categoryId);
            userArticleCategory.setCategoryArticles(userArticleCategory.getCategoryArticles().replaceAll(blogFrontPublish.getId() + ",", ""));
            //更新分类表
            userArticleCategoryMapper.updateCategoryArticlesById(userArticleCategory.getCategoryArticles(), categoryId);
            //将文章的分类写入分类表
            UserArticleCategory userArticleCategoryUpdate = userArticleCategoryMapper.findAllByCategoryName(blogFrontPublish.getArticleCategory());
            if (userArticleCategoryUpdate == null) {
                userArticleCategoryUpdate = new UserArticleCategory();
                userArticleCategoryUpdate.setCategoryArticles("");
                userArticleCategoryUpdate.setCategoryName(blogFrontPublish.getArticleCategory());
                //返回获取到的自增ID
                userArticleCategoryMapper.insert(userArticleCategoryUpdate);
            }
            //分类Id更新到数据库
            //userArticleMapper.updateArticleCategoryIdById(userArticleCategoryUpdate.getId(),blogFrontPublish.getId());
            //更新分类表的数据到里面
            userArticleCategoryUpdate.setCategoryArticles(userArticleCategoryUpdate.getCategoryArticles() + blogFrontPublish.getId() + ",");
            userArticleCategoryMapper.updateCategoryNameAndCategoryArticlesById(userArticleCategoryUpdate.getCategoryName(), userArticleCategoryUpdate.getCategoryArticles(), userArticleCategoryUpdate.getId());
            //设置文章表中的分类ID
            userArticle.setArticleCategoryId(userArticleCategoryUpdate.getId());

            //标签关联的Id
            String tagsId = userArticleMapper.selectArticleTagsIdById(blogFrontPublish.getId());
            //删除标签表中文章Id
            if (tagsId.length() > 0) {
                for (String a : tagsId.split(",")) {
                    if (a != null) {
                        //查询标签
                        UserArticleTag userArticleTag = userArticleTagMapper.selectAllById(Long.parseLong(a));
                        //去除标签中的articleId中的待删除的文章id
                        userArticleTag.setTagArticles(userArticleTag.getTagArticles().replaceAll(blogFrontPublish.getId() + ",", ""));
                        //更新标签表
                        userArticleTagMapper.updateTagArticlesById(userArticleTag.getTagArticles(), Long.parseLong(a));
                    }
                }
            }
            //把标签写入数据库
            for (String tag : tagTar.split(",")) {
                if (tag.replaceAll(" ", "").length() == 0) {
                    //单个标签只含空格
                    continue;
                }
                UserArticleTag userArticleTag = userArticleTagMapper.findAllByTagName(tag);
                if (userArticleTag == null) {
                    userArticleTag = new UserArticleTag();
                    userArticleTag.setTagName(tag);
                    userArticleTag.setTagArticles("");
                    userArticleTagMapper.insert(userArticleTag);
                }
                //转换后的值再更新得到文章表的主键
                userArticleTag.setTagArticles(userArticleTag.getTagArticles() + userArticle.getId() + ",");
                userArticle.setArticleTagsId(userArticle.getArticleTagsId() + userArticleTag.getId() + ",");
                userArticleTagMapper.updateTagNameAndTagArticlesById(userArticleTag.getTagName(), userArticleTag.getTagArticles(), userArticleTag.getId());
            }

            //如果是公开则更新到搜索引擎上
            if (blogFrontPublish.getArticleSecurity().equals("public")) {
                try {
                    //封装ES信息
                    EsRequest esRequest = new EsRequest();
                    esRequest.setArticleId(blogFrontPublish.getId());
                    esRequest.setTitle(blogFrontPublish.getArticleTitle());
                    esRequest.setSummary(summary);
                    esRequest.setCategory(blogFrontPublish.getArticleCategory());
                    esRequest.setArticleUpdateDate(new Date());
                    String oldCategoryName = userArticleCategory.getCategoryName();
                    //Feign调用
                    Object code = esFeign.updateArticle(esRequest, oldCategoryName).get("success");
                    LOGGER.info(code + "");
                } catch (Exception e) {
                    LOGGER.info("微服务出现网络的错误");
                }
            }

            //最后更新表的部分信息，更新需要的信息即可。
            userArticleMapper.updateSome(userArticle);
        }
    }

    //保存总点赞量到指定文章中
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void setThumpTotal(Long blogId, Integer total) {
        userArticleMapper.updateArticleThumbUpById(total,blogId);
    }

    //查询排行榜的指定信息
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public ArticleRank getArticleRank(Long id) {
        ArticleRank articleRankList=userArticleMapper.findAllById(id);
        return articleRankList;
    }

    //保存总收藏量到指定文章中
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void setCollectionTotal(Long blogId, Integer total) {
        userArticleMapper.updateArticleCollectionUpById(total,blogId);
    }
}
