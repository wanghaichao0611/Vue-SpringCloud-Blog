package com.Entity;

import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;

@Repository
public class UserAvatar implements Serializable {
    private Long id;

    private Long avatarId;

    private String username;

    private String avatarName;

    private String avatarUrl;

    private Date avatarcreatetime;

    private Date avatarupdatetime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Long avatarId) {
        this.avatarId = avatarId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Date getAvatarcreatetime() {
        return avatarcreatetime;
    }

    public void setAvatarcreatetime(Date avatarcreatetime) {
        this.avatarcreatetime = avatarcreatetime;
    }

    public Date getAvatarupdatetime() {
        return avatarupdatetime;
    }

    public void setAvatarupdatetime(Date avatarupdatetime) {
        this.avatarupdatetime = avatarupdatetime;
    }
}