package com.pay.Entity;


import org.springframework.stereotype.Repository;


//三张不同类型的银行卡的标志
@Repository
public class SelectBankcardSign {
    private Integer bankcardOnesign;
    private Integer bankcardTwosign;
    private Integer bankcardThreesign;

    public Integer getBankcardOnesign() {
        return bankcardOnesign;
    }

    public void setBankcardOnesign(Integer bankcardOnesign) {
        this.bankcardOnesign = bankcardOnesign;
    }

    public Integer getBankcardTwosign() {
        return bankcardTwosign;
    }

    public void setBankcardTwosign(Integer bankcardTwosign) {
        this.bankcardTwosign = bankcardTwosign;
    }

    public Integer getBankcardThreesign() {
        return bankcardThreesign;
    }

    public void setBankcardThreesign(Integer bankcardThreesign) {
        this.bankcardThreesign = bankcardThreesign;
    }
}
