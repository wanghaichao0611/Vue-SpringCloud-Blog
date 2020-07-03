package com.article.Entity;

import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public class UserArticleTag implements Serializable {
    private Long id;

    private String tagName;

    private String tagArticles;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagArticles() {
        return tagArticles;
    }

    public void setTagArticles(String tagArticles) {
        this.tagArticles = tagArticles;
    }
}