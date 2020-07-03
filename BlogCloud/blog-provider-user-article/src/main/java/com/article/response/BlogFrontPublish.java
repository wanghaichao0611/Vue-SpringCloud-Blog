package com.article.response;

import lombok.Data;
import org.springframework.stereotype.Repository;

import java.io.Serializable;


@Repository
public class BlogFrontPublish implements Serializable {
    private Long id;
    private String articleTitle;
    private String articleContent;
    private String articleTag;
    private String articleCategory;
    private String articleType;
    private String forWardUrl;
    private String translateUrl;
    private String articleSecurity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public String getArticleTag() {
        return articleTag;
    }

    public void setArticleTag(String articleTag) {
        this.articleTag = articleTag;
    }

    public String getArticleCategory() {
        return articleCategory;
    }

    public void setArticleCategory(String articleCategory) {
        this.articleCategory = articleCategory;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    public String getForWardUrl() {
        return forWardUrl;
    }

    public void setForWardUrl(String forWardUrl) {
        this.forWardUrl = forWardUrl;
    }

    public String getTranslateUrl() {
        return translateUrl;
    }

    public void setTranslateUrl(String translateUrl) {
        this.translateUrl = translateUrl;
    }

    public String getArticleSecurity() {
        return articleSecurity;
    }

    public void setArticleSecurity(String articleSecurity) {
        this.articleSecurity = articleSecurity;
    }
}
