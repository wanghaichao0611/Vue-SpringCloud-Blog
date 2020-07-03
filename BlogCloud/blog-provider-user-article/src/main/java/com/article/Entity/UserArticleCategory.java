package com.article.Entity;

import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public class UserArticleCategory implements Serializable {
    private Long id;

    private String categoryName;

    private String categoryArticles;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryArticles() {
        return categoryArticles;
    }

    public void setCategoryArticles(String categoryArticles) {
        this.categoryArticles = categoryArticles;
    }
}