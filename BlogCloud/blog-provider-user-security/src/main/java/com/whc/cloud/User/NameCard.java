package com.whc.cloud.User;


import org.springframework.stereotype.Repository;

@Repository
public class NameCard {

    //真实姓名
    private String realname;
    //身份证号码
    private String cardnumber;

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }
}
