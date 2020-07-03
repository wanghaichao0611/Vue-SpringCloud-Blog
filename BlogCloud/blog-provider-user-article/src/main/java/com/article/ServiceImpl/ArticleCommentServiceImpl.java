package com.article.ServiceImpl;

import com.article.Entity.UserArticleComment;
import com.article.Mapper.UserArticleCommentMapper;
import com.article.Service.ArticleCommentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ArticleCommentServiceImpl implements ArticleCommentService {

    //失败
    private static final  int FAILED=0;
    //成功
    private static final int SUCCESS=1;

    @Autowired
    UserArticleCommentMapper userArticleCommentMapper;



    //分页获取整个评论
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public PageInfo getCommentPage(Long articleId, int page, int count) {
        PageHelper.startPage(page,count);
        List<UserArticleComment> userArticleCommentList=userArticleCommentMapper.findAllByCommentArticleId(articleId);
        PageInfo pageInfo=new PageInfo(userArticleCommentList);
        return pageInfo;
    }

    //发表一级评论
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void publishCommentOne(Long articleId, String comment, Long commentatorId) {
        //如果是自己评论则不会发送回复消息，首先把信息比对好，再负载均衡。
        //负载均衡直接发送信息，消息中心会检验信息并且返回标志
        //负载均衡把评论者Id，被评论者Id，文章Id，文章标题，评论者信息
        //负载均衡过来返回了成功的标志后再插入评论
        //直接插入一级评论
        UserArticleComment userArticleComment=new UserArticleComment();
        userArticleComment.setCommentArticleId(articleId);
        userArticleComment.setCommentTotal(1);
        userArticleComment.setCommentContent(comment);
        userArticleComment.setCommentatorId(commentatorId);
        userArticleComment.setCommentDate(new Date());
        userArticleComment.setCommentParentId(commentatorId);
        userArticleCommentMapper.insert(userArticleComment);
    }
}
