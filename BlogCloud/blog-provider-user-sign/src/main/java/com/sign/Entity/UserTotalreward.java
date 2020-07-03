package com.sign.Entity;

import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;

@Repository
public class UserTotalreward implements Serializable {
    private Long id;

    private Long totalId;

    private Integer totalOne;

    private Integer totalTwo;

    private Integer totalThree;

    private Integer totalFour;

    private Integer totalFive;

    private Integer totalSix;

    private Integer totalSeven;

    private Date firsttime;

    private Date lasttime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTotalId() {
        return totalId;
    }

    public void setTotalId(Long totalId) {
        this.totalId = totalId;
    }

    public Integer getTotalOne() {
        return totalOne;
    }

    public void setTotalOne(Integer totalOne) {
        this.totalOne = totalOne;
    }

    public Integer getTotalTwo() {
        return totalTwo;
    }

    public void setTotalTwo(Integer totalTwo) {
        this.totalTwo = totalTwo;
    }

    public Integer getTotalThree() {
        return totalThree;
    }

    public void setTotalThree(Integer totalThree) {
        this.totalThree = totalThree;
    }

    public Integer getTotalFour() {
        return totalFour;
    }

    public void setTotalFour(Integer totalFour) {
        this.totalFour = totalFour;
    }

    public Integer getTotalFive() {
        return totalFive;
    }

    public void setTotalFive(Integer totalFive) {
        this.totalFive = totalFive;
    }

    public Integer getTotalSix() {
        return totalSix;
    }

    public void setTotalSix(Integer totalSix) {
        this.totalSix = totalSix;
    }

    public Integer getTotalSeven() {
        return totalSeven;
    }

    public void setTotalSeven(Integer totalSeven) {
        this.totalSeven = totalSeven;
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