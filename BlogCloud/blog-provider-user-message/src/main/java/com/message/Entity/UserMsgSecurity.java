package com.message.Entity;

import org.springframework.stereotype.Repository;

import java.io.Serializable;


@Repository
public class UserMsgSecurity implements Serializable {
    private Long id;

    private Long userId;

    private Integer msgOn;

    private Integer commentmsgOn;

    private Integer atmsgOn;

    private Integer thumpmsgOn;

    private Integer leavemsgOn;

    private Integer behaviorOn;

    private Integer commentOn;

    private Integer leaveOn;

    private Integer forwardOn;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getMsgOn() {
        return msgOn;
    }

    public void setMsgOn(Integer msgOn) {
        this.msgOn = msgOn;
    }

    public Integer getCommentmsgOn() {
        return commentmsgOn;
    }

    public void setCommentmsgOn(Integer commentmsgOn) {
        this.commentmsgOn = commentmsgOn;
    }

    public Integer getAtmsgOn() {
        return atmsgOn;
    }

    public void setAtmsgOn(Integer atmsgOn) {
        this.atmsgOn = atmsgOn;
    }

    public Integer getThumpmsgOn() {
        return thumpmsgOn;
    }

    public void setThumpmsgOn(Integer thumpmsgOn) {
        this.thumpmsgOn = thumpmsgOn;
    }

    public Integer getLeavemsgOn() {
        return leavemsgOn;
    }

    public void setLeavemsgOn(Integer leavemsgOn) {
        this.leavemsgOn = leavemsgOn;
    }

    public Integer getBehaviorOn() {
        return behaviorOn;
    }

    public void setBehaviorOn(Integer behaviorOn) {
        this.behaviorOn = behaviorOn;
    }

    public Integer getCommentOn() {
        return commentOn;
    }

    public void setCommentOn(Integer commentOn) {
        this.commentOn = commentOn;
    }

    public Integer getLeaveOn() {
        return leaveOn;
    }

    public void setLeaveOn(Integer leaveOn) {
        this.leaveOn = leaveOn;
    }

    public Integer getForwardOn() {
        return forwardOn;
    }

    public void setForwardOn(Integer forwardOn) {
        this.forwardOn = forwardOn;
    }
}