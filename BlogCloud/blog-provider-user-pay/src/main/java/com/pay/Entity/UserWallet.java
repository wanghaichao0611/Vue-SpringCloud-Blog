package com.pay.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Repository
public class UserWallet implements Serializable {
    private Long id;

    private Long walletId;

    private String bankcardOne;

    private Integer bankcardOnesign;

    private String bankcardnameOne;

    private String bankcardTwo;

    private Integer bankcardTwosign;

    private String bankcardnameTwo;

    private String bankcardThree;

    private Integer bankcardThreesign;

    private String bankcardnameThree;

    private BigDecimal walletMoney;

    private String walletPassword;

    private String realname;

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

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public String getBankcardOne() {
        return bankcardOne;
    }

    public void setBankcardOne(String bankcardOne) {
        this.bankcardOne = bankcardOne;
    }

    public Integer getBankcardOnesign() {
        return bankcardOnesign;
    }

    public void setBankcardOnesign(Integer bankcardOnesign) {
        this.bankcardOnesign = bankcardOnesign;
    }

    public String getBankcardnameOne() {
        return bankcardnameOne;
    }

    public void setBankcardnameOne(String bankcardnameOne) {
        this.bankcardnameOne = bankcardnameOne;
    }

    public String getBankcardTwo() {
        return bankcardTwo;
    }

    public void setBankcardTwo(String bankcardTwo) {
        this.bankcardTwo = bankcardTwo;
    }

    public Integer getBankcardTwosign() {
        return bankcardTwosign;
    }

    public void setBankcardTwosign(Integer bankcardTwosign) {
        this.bankcardTwosign = bankcardTwosign;
    }

    public String getBankcardnameTwo() {
        return bankcardnameTwo;
    }

    public void setBankcardnameTwo(String bankcardnameTwo) {
        this.bankcardnameTwo = bankcardnameTwo;
    }

    public String getBankcardThree() {
        return bankcardThree;
    }

    public void setBankcardThree(String bankcardThree) {
        this.bankcardThree = bankcardThree;
    }

    public Integer getBankcardThreesign() {
        return bankcardThreesign;
    }

    public void setBankcardThreesign(Integer bankcardThreesign) {
        this.bankcardThreesign = bankcardThreesign;
    }

    public String getBankcardnameThree() {
        return bankcardnameThree;
    }

    public void setBankcardnameThree(String bankcardnameThree) {
        this.bankcardnameThree = bankcardnameThree;
    }

    public BigDecimal getWalletMoney() {
        return walletMoney;
    }

    public void setWalletMoney(BigDecimal walletMoney) {
        this.walletMoney = walletMoney;
    }

    public String getWalletPassword() {
        return walletPassword;
    }

    public void setWalletPassword(String walletPassword) {
        this.walletPassword = walletPassword;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
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