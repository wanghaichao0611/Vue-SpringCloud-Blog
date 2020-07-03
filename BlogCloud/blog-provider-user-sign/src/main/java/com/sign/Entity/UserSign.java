package com.sign.Entity;

import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;

@Repository
public class UserSign implements Serializable {
    private Long id;

    private Long signId;

    private Long experience;

    private Long totalSign;

    private Long continueSign;

    private Date lastsigntime;

    private Date recentsigntime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSignId() {
        return signId;
    }

    public void setSignId(Long signId) {
        this.signId = signId;
    }

    public Long getExperience() {
        return experience;
    }

    public void setExperience(Long experience) {
        this.experience = experience;
    }

    public Long getTotalSign() {
        return totalSign;
    }

    public void setTotalSign(Long totalSign) {
        this.totalSign = totalSign;
    }

    public Long getContinueSign() {
        return continueSign;
    }

    public void setContinueSign(Long continueSign) {
        this.continueSign = continueSign;
    }

    public Date getLastsigntime() {
        return lastsigntime;
    }

    public void setLastsigntime(Date lastsigntime) {
        this.lastsigntime = lastsigntime;
    }

    public Date getRecentsigntime() {
        return recentsigntime;
    }

    public void setRecentsigntime(Date recentsigntime) {
        this.recentsigntime = recentsigntime;
    }
}