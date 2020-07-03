package com.member.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;

@Repository
public class UserMember implements Serializable {
    private Long id;

    private Long memberId;

    private Integer memberVip;

    private Integer memberSvip;

    private Date rechargetimeVip;

    private Date expiretimeVip;

    private Date rechargetimeSvip;

    private Date expiretimeSvip;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createtime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updatetime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Integer getMemberVip() {
        return memberVip;
    }

    public void setMemberVip(Integer memberVip) {
        this.memberVip = memberVip;
    }

    public Integer getMemberSvip() {
        return memberSvip;
    }

    public void setMemberSvip(Integer memberSvip) {
        this.memberSvip = memberSvip;
    }

    public Date getRechargetimeVip() {
        return rechargetimeVip;
    }

    public void setRechargetimeVip(Date rechargetimeVip) {
        this.rechargetimeVip = rechargetimeVip;
    }

    public Date getExpiretimeVip() {
        return expiretimeVip;
    }

    public void setExpiretimeVip(Date expiretimeVip) {
        this.expiretimeVip = expiretimeVip;
    }

    public Date getRechargetimeSvip() {
        return rechargetimeSvip;
    }

    public void setRechargetimeSvip(Date rechargetimeSvip) {
        this.rechargetimeSvip = rechargetimeSvip;
    }

    public Date getExpiretimeSvip() {
        return expiretimeSvip;
    }

    public void setExpiretimeSvip(Date expiretimeSvip) {
        this.expiretimeSvip = expiretimeSvip;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}