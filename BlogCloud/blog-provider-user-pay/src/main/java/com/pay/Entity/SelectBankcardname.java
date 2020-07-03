package com.pay.Entity;


import org.springframework.stereotype.Repository;


//所有银行卡姓名
@Repository
public class SelectBankcardname {
    private String bankcardnameOne;
    private String bankcardnameTwo;
    private String bankcardnameThree;

    public String getBankcardnameOne() {
        return bankcardnameOne;
    }

    public void setBankcardnameOne(String bankcardnameOne) {
        this.bankcardnameOne = bankcardnameOne;
    }

    public String getBankcardnameTwo() {
        return bankcardnameTwo;
    }

    public void setBankcardnameTwo(String bankcardnameTwo) {
        this.bankcardnameTwo = bankcardnameTwo;
    }

    public String getBankcardnameThree() {
        return bankcardnameThree;
    }

    public void setBankcardnameThree(String bankcardnameThree) {
        this.bankcardnameThree = bankcardnameThree;
    }
}
