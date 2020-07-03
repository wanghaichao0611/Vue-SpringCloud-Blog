package com.sign.Entity;

import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;

@Repository
public class UserContinuereward implements Serializable {
    private Long id;

    private Long continueId;

    private Integer continuerewardOne;

    private Integer continuerewardTwo;

    private Integer continuerewardThree;

    private Integer continuerewardFour;

    private Integer continuerewardFive;

    private Integer continuerewardSix;

    private Integer continuerewardSeven;

    private Date firsttime;

    private Date lasttime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContinueId() {
        return continueId;
    }

    public void setContinueId(Long continueId) {
        this.continueId = continueId;
    }

    public Integer getContinuerewardOne() {
        return continuerewardOne;
    }

    public void setContinuerewardOne(Integer continuerewardOne) {
        this.continuerewardOne = continuerewardOne;
    }

    public Integer getContinuerewardTwo() {
        return continuerewardTwo;
    }

    public void setContinuerewardTwo(Integer continuerewardTwo) {
        this.continuerewardTwo = continuerewardTwo;
    }

    public Integer getContinuerewardThree() {
        return continuerewardThree;
    }

    public void setContinuerewardThree(Integer continuerewardThree) {
        this.continuerewardThree = continuerewardThree;
    }

    public Integer getContinuerewardFour() {
        return continuerewardFour;
    }

    public void setContinuerewardFour(Integer continuerewardFour) {
        this.continuerewardFour = continuerewardFour;
    }

    public Integer getContinuerewardFive() {
        return continuerewardFive;
    }

    public void setContinuerewardFive(Integer continuerewardFive) {
        this.continuerewardFive = continuerewardFive;
    }

    public Integer getContinuerewardSix() {
        return continuerewardSix;
    }

    public void setContinuerewardSix(Integer continuerewardSix) {
        this.continuerewardSix = continuerewardSix;
    }

    public Integer getContinuerewardSeven() {
        return continuerewardSeven;
    }

    public void setContinuerewardSeven(Integer continuerewardSeven) {
        this.continuerewardSeven = continuerewardSeven;
    }

    public Date getFirsttime() {
        return firsttime;
    }

    public void setFirsttime(Date firsttime) {
        this.firsttime = firsttime;
    }

    public Date getLasttime() {
        return lasttime;
    }

    public void setLasttime(Date lasttime) {
        this.lasttime = lasttime;
    }
}