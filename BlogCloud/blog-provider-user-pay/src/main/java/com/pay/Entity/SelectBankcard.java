package com.pay.Entity;


import org.springframework.stereotype.Repository;

//所有银行卡号
@Repository
public class SelectBankcard {
    private String bankcardOne;
    private String bankcardTwo;
    private String bankcardThree;

    public String getBankcardOne() {
        return bankcardOne;
    }

    public void setBankcardOne(String bankcardOne) {
        this.bankcardOne = bankcardOne;
    }

    public String getBankcardTwo() {
        return bankcardTwo;
    }

    public void setBankcardTwo(String bankcardTwo) {
        this.bankcardTwo = bankcardTwo;
    }

    public String getBankcardThree() {
        return bankcardThree;
    }

    public void setBankcardThree(String bankcardThree) {
        this.bankcardThree = bankcardThree;
    }
}
