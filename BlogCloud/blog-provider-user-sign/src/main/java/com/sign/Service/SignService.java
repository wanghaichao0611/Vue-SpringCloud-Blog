package com.sign.Service;

import com.sign.Entity.UserSign;
import org.springframework.stereotype.Service;


@Service
public interface SignService {

    //签到按钮的显示
    int showSignButton(Long id);

    //获取整个经验
    UserSign getSignAll(Long id);

    //签到增加经验
    int checkSign(Long id);

    //获取经验给前端
    Long getExperience(Long id);

    //判断是否可以领取连续签到的奖励
    int getContinueReward(Long id);

    //显示连续签到的按钮
    int showContinue(Long id);

    //显示总数签到的按钮
    int showTotal(Long id);

    //判断是否可以领取总数签到的奖励
    int getTotalReward(Long id);

}
