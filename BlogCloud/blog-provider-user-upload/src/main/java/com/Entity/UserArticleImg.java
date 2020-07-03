package com.Entity;

import java.io.Serializable;
import java.util.Date;

public class UserArticleImg implements Serializable {
    private Long id;

    private Long articleAuthorId;

    private String username;

    private String blogimgName;

    private String blogimgUrl;

    private Date blogimgcreatetime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticleAuthorId() {
        return articleAuthorId;
    }

    public void setArticleAuthorId(Long articleAuthorId) {
        this.articleAuthorId = articleAuthorId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBlogimgName() {
        return blogimgName;
    }

    public void setBlogimgName(String blogimgName) {
        this.blogimgName = blogimgName;
    }

    public String getBlogimgUrl() {
        return blogimgUrl;
    }

    public void setBlogimgUrl(String blogimgUrl) {
        this.blogimgUrl = blogimgUrl;
    }

    public Date getBlogimgcreatetime() {
        return blogimgcreatetime;
    }

    public void setBlogimgcreatetime(Date blogimgcreatetime) {
        this.blogimgcreatetime = blogimgcreatetime;
    }
}