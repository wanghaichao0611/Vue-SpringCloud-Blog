package com.sign.Service;

import org.springframework.stereotype.Service;

@Service
public interface RankService {

    //返回签到的等级
    int ranSign(int myExperience);

    //签到经验值的排名
    int rank(Long myExperience);

}
