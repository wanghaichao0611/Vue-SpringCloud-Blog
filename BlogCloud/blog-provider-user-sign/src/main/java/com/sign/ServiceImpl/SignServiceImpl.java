package com.sign.ServiceImpl;

import com.sign.Entity.UserContinuereward;
import com.sign.Entity.UserSign;
import com.sign.Entity.UserTotalreward;
import com.sign.Feign.SignMemberFeign;
import com.sign.Mapper.UserContinuerewardMapper;
import com.sign.Mapper.UserSignMapper;
import com.sign.Mapper.UserTotalrewardMapper;
import com.sign.Service.SignService;
import com.sign.Service.TimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class SignServiceImpl implements SignService {

    //失败
    private static final  int FAILED=0;
    //成功
    private static final int SUCCESS=1;

    //控制台输出看时间
    private Logger logger = LoggerFactory.getLogger(SignServiceImpl.class);

    //签到的Mapper
    @Autowired
    UserSignMapper userSignMapper;

    //连续签到表的Mapper
    @Autowired
    UserContinuerewardMapper userContinuerewardMapper;

    //总数签到表的Mapper
    @Autowired
    UserTotalrewardMapper userTotalrewardMapper;

    //判断限时凭证的控制
    @Autowired
    TimeService timeService;

    @Autowired
    SignMemberFeign signMemberFeign;

    //签到按钮的显示
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public int showSignButton(Long id) {
        Jedis jedis = new Jedis("localhost", 6379);
        //获取是否存在主键ID是否存在(不存在限时凭证时返回SUCCESS)
        if (jedis.get("Sign-"+id)==null){
            jedis.close();
            return SUCCESS;
        }else {
            jedis.close();
            return FAILED;
        }
    }

    //前端获取整个签到的实体
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public UserSign  getSignAll(Long id) {
        UserSign userSign=userSignMapper.selectAllBySignId(id);
        return userSign;
    }

    //签到增加经验值的方式
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int checkSign(Long id)   {
        //先负载均衡查询会员开通情况
        Map<String,Object> memberAll=signMemberFeign.selectMember(id);
        //两个会员标志
        Object vipMember=memberAll.get("vipMember");
        Object svipMember=memberAll.get("svipMember");
        //首先判断是否存在限时凭证，大于0不存在之后又设置了时限.
        while (timeService.setTime(id)>0){
            //首先判断是否首次签到（使用连续签到可以消除连续签到失败的情况）
            Long continueSign=userSignMapper.selectContinueBySignId(id);
            //控制台打印连续签到次数
            logger.info("你的连续签到次数是:"+continueSign);
            if (continueSign==0){
                //如果连续签到为null则可以直接更新经验,总签到数和连续签到数.(非会员)
                if (vipMember.equals(0) && svipMember.equals(0)){
                    userSignMapper.updateFirstExperienceBySignId(id);
                    return SUCCESS;
                }else if (vipMember.equals(1) && svipMember.equals(0)){
                    //普通会员首次签到经验值翻5倍
                    userSignMapper.updateFirstExperienceVipBySignId(id);
                    return SUCCESS;
                }else {
                    //超级会员首次签经验吃翻10倍
                    userSignMapper.updateFirstExperienceSvipBySignId(id);
                    return SUCCESS;
                }
            }else {
                //如果不为NUll值时,先获取本次时间和上次更新的天数之差是否>=2
                Date lastDate=userSignMapper.selectRecentsigntimeBySignId(id);
                try {
                    //设置日期格式
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String now = df.format(new Date());
                    Date nowDate = df.parse(now);
                    Long day=(nowDate.getTime()-lastDate.getTime())/(60*60*1000*24);
                    if (day.intValue()>=2){
                        //如果存在大于等于2的情况,则说明需要重置签到连续次数并且+1的同时增加3经验和总签到+1
                        userSignMapper.updateOverExperienceBySignId(id);
                        return SUCCESS;
                    }else {
                        //如果存在天数<2则说明可以判断连续的天数然后增加多少经验值
                        //调用存储过程判断增加的经验值以及总次数和连续次数都+1
                        //控制台打印天数差
                        logger.info("天数的之差是:"+day);
                        //mysql的存储过程渲染确认要增加的经验值(最好在服务端渲染(存储过程的扩展性较差))
                        if (vipMember.equals(0) && svipMember.equals(0)){
                            //没有会员普通加成
                            userSignMapper.callExperience(id.intValue());
                            return SUCCESS;
                        }else if (vipMember.equals(1) && svipMember.equals(0)){
                            //普通会员经验加成
                            userSignMapper.callVipExperience(id.intValue());
                            return SUCCESS;
                        }else {
                            //超级会员经验加成
                            userSignMapper.callSvipExperience(id.intValue());
                            return SUCCESS;
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return FAILED;
    }

    //获取经验给前端
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Long getExperience(Long id) {
        //获得的经验值
       Long experience=userSignMapper.selectExperienceBySignId(id);
        return experience;
    }

    //判断是否存在可以领取连续签到的奖励(SUCCESS代表失败,而FAILED代表领取成功)
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int getContinueReward(Long id) {
        //获得连续签到的主键ID
        Long continueId=userSignMapper.selectIdBySignId(id);
        //获得奖励的领取情况
        UserContinuereward userContinuereward=userContinuerewardMapper.selectAllByContinueId(continueId);
        //查询连续签到的次数
        Long continueSign=userSignMapper.selectContinueBySignId(id);
        //如果没有签到则返回失败
        if (continueSign==0){
            return FAILED;
        }
        else {
            //客户端渲染one连续签到
            if (userContinuereward.getContinuerewardOne()==null){
                if (continueSign>=1) {
                    //更新第一个奖励的值为1
                    userContinuerewardMapper.updateContinuerewardOneByContinueId(continueId);
                    //增加经验值+20
                    userSignMapper.updateContinueOneBySignId(id);
                }else {
                    return FAILED;
                }
            }
            //客户端渲染two连续签到
            if (userContinuereward.getContinuerewardTwo()==null){
                if (continueSign>=5) {
                    //更新第二个奖励的值为1
                    userContinuerewardMapper.updateContinuerewardTwoByContinueId(continueId);
                    //增加经验值+30
                    userSignMapper.updateContinueTwoBySignId(id);
                }else {
                    return FAILED;
                }
            }
            //客户端渲染three连续签到
            if (userContinuereward.getContinuerewardThree()==null){
                if (continueSign>=30){
                    //更新第三个奖励的值为1
                    userContinuerewardMapper.updateContinuerewardThreeByContinueId(continueId);
                    //VIP七天的体验时间
                    int time=7;
                    signMemberFeign.couponVipTime(id,time);
                }else {
                    return FAILED;
                }
            }
            //客户端渲染four连续签到
            if (userContinuereward.getContinuerewardFour()==null){
                if (continueSign>=90){
                    //更新第四个奖励的值为1
                    userContinuerewardMapper.updateContinuerewardFourByContinueId(continueId);
                    //VIP30天的体验时间
                    int time=30;
                    signMemberFeign.couponVipTime(id,time);
                }else {
                    return FAILED;
                }
            }
            //客户端渲染five连续签到
            if (userContinuereward.getContinuerewardFive()==null){
                if (continueSign>=180){
                    //更新第五个奖励的值为1
                    userContinuerewardMapper.updateContinuerewardFiveByContinueId(continueId);
                    //增加经验值+500
                    userSignMapper.updateContinueFiveBySignId(id);
                }else {
                    return FAILED;
                }
            }
            //客户端渲染six连续签到
            if (userContinuereward.getContinuerewardSix()==null){
                if (continueSign>=360){
                    //更新第六个奖励的值为1
                    userContinuerewardMapper.updateContinuerewardSixByContinueId(continueId);
                    //VIP90天的体验时间
                    int time=90;
                    signMemberFeign.couponVipTime(id,time);
                }else {
                    return FAILED;
                }
            }
            //客户端渲染seven连续签到
            if (userContinuereward.getContinuerewardSeven()==null){
                if (continueSign>=720){
                    //更新第七个奖励的值为1
                    userContinuerewardMapper.updateContinuerewardSevenByContinueId(continueId);
                    //VIP90天的体验时间
                    int time=180;
                    signMemberFeign.couponVipTime(id,time);
                }else {
                    return FAILED;
                }
            }
        }
        //代表所有奖励均被领取完成
        return FAILED;
    }

    //连续签到的Button
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int showContinue(Long id) {
        //获得连续签到的主键ID
        Long continueId=userSignMapper.selectIdBySignId(id);
        //获得奖励的领取情况
        UserContinuereward userContinuereward=userContinuerewardMapper.selectAllByContinueId(continueId);
        //查询连续签到的次数
        Long continueSign=userSignMapper.selectContinueBySignId(id);
        //判断连续签到的次数处于哪个阶段
        if (continueSign==0){
            return FAILED;
        }
        else if (continueSign>=1 && continueSign<5 ){
            if (userContinuereward.getContinuerewardOne()==null){
                //返还给前端可以显示按钮
                return SUCCESS;
            }else {
                return FAILED;
            }
        }
        else if (continueSign>=5 && continueSign<30){
            if (userContinuereward.getContinuerewardTwo()==null){
                return SUCCESS;
            }else {
                return FAILED;
            }
        }
        else if (continueSign>=30 && continueSign<90){
            if (userContinuereward.getContinuerewardThree()==null){
                return SUCCESS;
            }else {
                return FAILED;
            }
        }
        else if (continueSign>=90 && continueSign<180){
            if (userContinuereward.getContinuerewardFour()==null){
                return SUCCESS;
            }else {
                return FAILED;
            }
        }
        else if (continueSign>=180 && continueSign<360){
            if (userContinuereward.getContinuerewardFive()==null){
                return SUCCESS;
            }else {
                return FAILED;
            }
        }
        else if (continueSign>=360 && continueSign<720){
            if (userContinuereward.getContinuerewardSix()==null){
                return SUCCESS;
            }else {
                return FAILED;
            }
        }
        else{
            if (userContinuereward.getContinuerewardSeven()==null){
                return SUCCESS;
            }else {
                return FAILED;
            }
        }
    }

    //总数签到的Button
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int showTotal(Long id) {
        //获得连续签到的主键ID
        Long totalId=userSignMapper.selectIdBySignId(id);
        //查询总数签到的次数
        Long totalSign=userSignMapper.selectTotalSignBySignId(id);
        //获得总数签到的领取情况
        UserTotalreward userTotalreward=userTotalrewardMapper.selectAllByTotalId(totalId);
        //判断总数签到处于哪个阶段
        if (totalSign==0 || totalSign<30){
            return FAILED;
        }else if (totalSign<60){
            if (userTotalreward.getTotalOne()==null){
                return SUCCESS;
            }else {
                return FAILED;
            }
        }else if (totalSign<90){
            if (userTotalreward.getTotalTwo()==null){
                return SUCCESS;
            }else {
                return FAILED;
            }
        }else if (totalSign<120){
            if (userTotalreward.getTotalThree()==null){
                return SUCCESS;
            }else {
                return FAILED;
            }
        }else if (totalSign<180){
            if (userTotalreward.getTotalFour()==null){
                return SUCCESS;
            }else {
                return FAILED;
            }
        }else if (totalSign<360){
            if (userTotalreward.getTotalFive()==null){
                return SUCCESS;
            }else {
                return FAILED;
            }
        }else if (totalSign<720){
            if (userTotalreward.getTotalSix()==null){
                return SUCCESS;
            }else {
                return FAILED;
            }
        }else {
            if (userTotalreward.getTotalSeven()==null){
                return SUCCESS;
            }else {
                return FAILED;
            }
        }
    }
    //获得总数签到的奖励(SUCCESS代表失败,而FAILED代表领取成功)
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int getTotalReward(Long id) {
        //获得连续签到的主键ID
        Long totalId=userSignMapper.selectIdBySignId(id);
        //查询总数签到的次数
        Long totalSign=userSignMapper.selectTotalSignBySignId(id);
        //获得总数签到的领取情况
        UserTotalreward userTotalreward=userTotalrewardMapper.selectAllByTotalId(totalId);
        if (totalSign==null){
            return SUCCESS;
        }
        if (userTotalreward.getTotalOne()==null){
            if (totalSign>=30){
                //更新总数签到的第一个奖励
                userTotalrewardMapper.updateTotalOneByTotalId(totalId);
                //总数签到的第一个奖励经验值+100
                userSignMapper.updateTotalOneBySignId(id);
            }else {
                return FAILED;
            }
        }
        if (userTotalreward.getTotalTwo()==null){
            if (totalSign>=60){
                //更新总数签到的第二个奖励
                userTotalrewardMapper.updateTotalTwoByTotalId(totalId);
                //总是签到第二个奖励经验值+200
                userSignMapper.updateTotalTwoBySignId(id);
            }else {
                return FAILED;
            }
        }
        if (userTotalreward.getTotalThree()==null){
            if (totalSign>=90){
                //更新总数签到的第三个奖励
                userTotalrewardMapper.updateTotalThreeByTotalId(totalId);
                //总是签到第三个奖励经验值+300
                userSignMapper.updateTotalThreeBySignId(id);
            }else {
                return FAILED;
            }
        }
        if (userTotalreward.getTotalFour()==null){
            if (totalSign>=120){
                //更新总数签到的第四个奖励
                userTotalrewardMapper.updateTotalFourByTotalId(totalId);
                //VIP30天的体验时间
                int time=30;
                signMemberFeign.couponVipTime(id,time);
            }else{
                return FAILED;
            }
        }
        if (userTotalreward.getTotalFive()==null){
            if (totalSign>=180){
                //更新总数签到的第五个奖励
                userTotalrewardMapper.updateTotalFiveByTotalId(totalId);
                //总数签到第五个奖励经验值+500
                userSignMapper.updateTotalFiveBySignId(id);
            }else {
                return FAILED;
            }
        }
        if (userTotalreward.getTotalSix()==null){
            if (totalSign>=360){
                //更新总数签到的第六个奖励
                userTotalrewardMapper.updateTotalSixByTotalId(totalId);
                //总数签到第六个奖励经验值+2000
                userSignMapper.updateTotalSixBySignId(id);
            }else {
                return FAILED;
            }
        }
        if (userTotalreward.getTotalSeven()==null){
            if (totalSign>=720){
                //更新总是签到的第七个奖励
                userTotalrewardMapper.updateTotalSevenByTotalId(totalId);
                //总数签到第七个奖励经验值+5000
                userSignMapper.updateTotalSevenBySignId(id);
            }else {
                return FAILED;
            }
        }
        return FAILED;
    }
}
