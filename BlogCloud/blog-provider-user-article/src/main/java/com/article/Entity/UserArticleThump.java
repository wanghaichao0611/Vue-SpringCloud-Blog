package com.article.Entity;

import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;


@Repository
public class UserArticleThump implements Serializable {
    private Long id;

    private Long blogId;

    private Long userId;

    private Date thumpTime;

    private Integer thumpStatus;

    private Integer thumpActive;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getThumpTime() {
        return thumpTime;
    }

    public void setThumpTime(Date thumpTime) {
        this.thumpTime = thumpTime;
    }

    public Integer getThumpStatus() {
        return thumpStatus;
    }

    public void setThumpStatus(Integer thumpStatus) {
        this.thumpStatus = thumpStatus;
    }

    public Integer getThumpActive() {
        return thumpActive;
    }

    public void setThumpActive(Integer thumpActive) {
        this.thumpActive = thumpActive;
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