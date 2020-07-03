package com.sign.Entity;

import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;


@Repository
public class UserSignArticle implements Serializable {
    private Long id;

    private Long userId;

    private Integer signArticle;

    private Integer signThump;

    private Integer signComment;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getSignArticle() {
        return signArticle;
    }

    public void setSignArticle(Integer signArticle) {
        this.signArticle = signArticle;
    }

    public Integer getSignThump() {
        return signThump;
    }

    public void setSignThump(Integer signThump) {
        this.signThump = signThump;
    }

    public Integer getSignComment() {
        return signComment;
    }

    public void setSignComment(Integer signComment) {
        this.signComment = signComment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}