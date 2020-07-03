package com.message.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;


@Repository
public class UserMsgThump implements Serializable {
    private Long id;

    private Long thumpId;

    private Long thumptoId;

    private Long articleId;

    private String articleTitle;

    private Boolean thumpmsgOn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createtime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getThumpId() {
        return thumpId;
    }

    public void setThumpId(Long thumpId) {
        this.thumpId = thumpId;
    }

    public Long getThumptoId() {
        return thumptoId;
    }

    public void setThumptoId(Long thumptoId) {
        this.thumptoId = thumptoId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public Boolean getThumpmsgOn() {
        return thumpmsgOn;
    }

    public void setThumpmsgOn(Boolean thumpmsgOn) {
        this.thumpmsgOn = thumpmsgOn;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}