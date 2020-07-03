package com.article.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;


@Repository
public class UserArticle implements Serializable {
    private Long id;

    private Long articleAuthorId;

    private String articleTitle;

    private String articleSummary;

    private String articleMdContent;

    private String articleTagsId;

    private Long articleCategoryId;

    private Integer articleReading;

    private Integer articleThumbUp;

    private Integer articleComment;

    private Integer articleCollection;

    private Integer articleForward;

    private Boolean articleIsPublic;

    private Boolean articleIsPrivate;

    private Boolean articleIsVip;

    private Boolean articleIsOriginal;

    private Boolean articleIsForward;

    private Boolean articleIsTranslate;

    private String articleForwardUrl;

    private String articleTranslateUrl;

    private Boolean articleExamine;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date articlePublishDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date articleUpdateDate;

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

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleSummary() {
        return articleSummary;
    }

    public void setArticleSummary(String articleSummary) {
        this.articleSummary = articleSummary;
    }

    public String getArticleMdContent() {
        return articleMdContent;
    }

    public void setArticleMdContent(String articleMdContent) {
        this.articleMdContent = articleMdContent;
    }

    public String getArticleTagsId() {
        return articleTagsId;
    }

    public void setArticleTagsId(String articleTagsId) {
        this.articleTagsId = articleTagsId;
    }

    public Long getArticleCategoryId() {
        return articleCategoryId;
    }

    public void setArticleCategoryId(Long articleCategoryId) {
        this.articleCategoryId = articleCategoryId;
    }

    public Integer getArticleReading() {
        return articleReading;
    }

    public void setArticleReading(Integer articleReading) {
        this.articleReading = articleReading;
    }

    public Integer getArticleThumbUp() {
        return articleThumbUp;
    }

    public void setArticleThumbUp(Integer articleThumbUp) {
        this.articleThumbUp = articleThumbUp;
    }

    public Integer getArticleComment() {
        return articleComment;
    }

    public void setArticleComment(Integer articleComment) {
        this.articleComment = articleComment;
    }

    public Integer getArticleCollection() {
        return articleCollection;
    }

    public void setArticleCollection(Integer articleCollection) {
        this.articleCollection = articleCollection;
    }

    public Integer getArticleForward() {
        return articleForward;
    }

    public void setArticleForward(Integer articleForward) {
        this.articleForward = articleForward;
    }

    public Boolean getArticleIsPublic() {
        return articleIsPublic;
    }

    public void setArticleIsPublic(Boolean articleIsPublic) {
        this.articleIsPublic = articleIsPublic;
    }

    public Boolean getArticleIsPrivate() {
        return articleIsPrivate;
    }

    public void setArticleIsPrivate(Boolean articleIsPrivate) {
        this.articleIsPrivate = articleIsPrivate;
    }

    public Boolean getArticleIsVip() {
        return articleIsVip;
    }

    public void setArticleIsVip(Boolean articleIsVip) {
        this.articleIsVip = articleIsVip;
    }

    public Boolean getArticleIsOriginal() {
        return articleIsOriginal;
    }

    public void setArticleIsOriginal(Boolean articleIsOriginal) {
        this.articleIsOriginal = articleIsOriginal;
    }

    public Boolean getArticleIsForward() {
        return articleIsForward;
    }

    public void setArticleIsForward(Boolean articleIsForward) {
        this.articleIsForward = articleIsForward;
    }

    public Boolean getArticleIsTranslate() {
        return articleIsTranslate;
    }

    public void setArticleIsTranslate(Boolean articleIsTranslate) {
        this.articleIsTranslate = articleIsTranslate;
    }

    public String getArticleForwardUrl() {
        return articleForwardUrl;
    }

    public void setArticleForwardUrl(String articleForwardUrl) {
        this.articleForwardUrl = articleForwardUrl;
    }

    public String getArticleTranslateUrl() {
        return articleTranslateUrl;
    }

    public void setArticleTranslateUrl(String articleTranslateUrl) {
        this.articleTranslateUrl = articleTranslateUrl;
    }

    public Boolean getArticleExamine() {
        return articleExamine;
    }

    public void setArticleExamine(Boolean articleExamine) {
        this.articleExamine = articleExamine;
    }

    public Date getArticlePublishDate() {
        return articlePublishDate;
    }

    public void setArticlePublishDate(Date articlePublishDate) {
        this.articlePublishDate = articlePublishDate;
    }

    public Date getArticleUpdateDate() {
        return articleUpdateDate;
    }

    public void setArticleUpdateDate(Date articleUpdateDate) {
        this.articleUpdateDate = articleUpdateDate;
    }
}