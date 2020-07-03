package com.sign.ServiceImpl;

import com.sign.Mapper.UserSignMapper;
import com.sign.Service.RankService;
import com.sign.Service.SortService;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class RankServiceImpl implements RankService {

    //调用crud的服务
    @Autowired
    UserSignMapper userSignMapper;

    //算法的服务
    @Autowired
    SortService sortService;

    //控制台输出看排名和等级
    private Logger logger = LoggerFactory.getLogger(RankServiceImpl.class);

    //签到的等级
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int ranSign(int myExperience) {

        if (myExperience<100){
            return 1;
        }else if (myExperience<200){
            return 2;
        }else if (myExperience<500){
            return 3;
        }else if (myExperience<1000){
            return 4;
        }else if (myExperience<2000){
            return 5;
        }else if (myExperience<3000){
            return 6;
        }else if (myExperience<5000){
            return 7;
        }else if (myExperience<10000){
            return 8;
        }else if (myExperience<20000){
            return 9;
        }else if (myExperience<30000){
            return 10;
        }else if (myExperience<60000){
            return 11;
        }else if (myExperience<100000){
            return 12;
        }else if (myExperience<180000){
            return 13;
        }else if (myExperience<300000){
            return 14;
        }else if (myExperience<500000){
            return 15;
        }else if (myExperience<600000){
            return 16;
        }else if (myExperience<700000){
            return 17;
        }else if (myExperience<1000000){
            return 18;
        }else if (myExperience<1500000){
            return 19;
        }else {
            return 20;
        }
    }

    //经验值排名的查找
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public int rank(Long myExperience) {

        //先查询所有的经验值
        List<Long> exAll=userSignMapper.selectExperience();
        //先将List<Long>集合转为Long[]数组
        Long[] a=exAll.stream().toArray(Long[]::new);
        //再将Long[]数组转为long[]数组。使用的是org.apache.commons.lang包中的一个工具类ArrayUtils.java
        long[] quickSort = ArrayUtils.toPrimitive(a);
        //快速排序排序成从小到大的顺序
        sortService.quickSort(quickSort,0,quickSort.length-1);
        //查找经验值所在的索引位置
        int position=sortService.binarySearch(quickSort,myExperience);
        //最终排名
        int lastPosition=quickSort.length-position;
        //控制条输出你的排名
        logger.info("你在经验值排行榜中的名次是:"+lastPosition);
        //返回给前端的排名
        return lastPosition;
    }
}
