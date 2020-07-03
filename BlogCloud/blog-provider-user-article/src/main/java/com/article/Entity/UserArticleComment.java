package com.article.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;


@Repository
public class UserArticleComment implements Serializable {
    private Long commentId;

    private Long commentArticleId;

    private Integer commentTotal;

    private Long commentatorId;

    private String commentContent;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date commentDate;

    private Long commentParentId;

    private String commentResponseId;

    private String commentResponseDate;

    private static final long serialVersionUID = 1L;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getCommentArticleId() {
        return commentArticleId;
    }

    public void setCommentArticleId(Long commentArticleId) {
        this.commentArticleId = commentArticleId;
    }

    public Integer getCommentTotal() {
        return commentTotal;
    }

    public void setCommentTotal(Integer commentTotal) {
        this.commentTotal = commentTotal;
    }

    public Long getCommentatorId() {
        return commentatorId;
    }

    public void setCommentatorId(Long commentatorId) {
        this.commentatorId = commentatorId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public Long getCommentParentId() {
        return commentParentId;
    }

    public void setCommentParentId(Long commentParentId) {
        this.commentParentId = commentParentId;
    }

    public String getCommentResponseId() {
        return commentResponseId;
    }

    public void setCommentResponseId(String commentResponseId) {
        this.commentResponseId = commentResponseId;
    }

    public String getCommentResponseDate() {
        return commentResponseDate;
    }

    public void setCommentResponseDate(String commentResponseDate) {
        this.commentResponseDate = commentResponseDate;
    }
}