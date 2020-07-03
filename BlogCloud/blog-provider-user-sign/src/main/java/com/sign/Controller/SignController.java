package com.sign.Controller;

import com.sign.Entity.UserSign;
import com.sign.Entity.UserSignArticle;
import com.sign.Entity.UserZeroSign;
import com.sign.Feign.SignMemberFeign;
import com.sign.Mapper.UserSignArticleMapper;
import com.sign.Mapper.UserSignMapper;
import com.sign.Service.RankService;
import com.sign.Service.SignService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//签到页面的控制层
@RestController
public class SignController {

    //签到的服务
    @Autowired
    SignService signService;

    //排名和等级的服务
    @Autowired
    RankService rankService;

    @Autowired
    UserSignArticleMapper userSignArticleMapper;

    @Autowired
    SignMemberFeign signMemberFeign;

    @Autowired
    UserSignMapper userSignMapper;

    //签到按钮的显示
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/showSign")
    public Map<String, Object> showSign(@RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //查询主键ID是否存在redis中
        int result=signService.showSignButton(id);
        boolean flag = result > 0;
        resultMap.put("success", flag);
        return resultMap;
    }

    //前端获得整个签到的数据(防止网络出问题刷新按钮)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getSignAll")
    public Map<String, Object> getSignAll(@RequestParam("id") Long id) {
        Map<String, Object> resultMap = new HashMap<>();
        //获取数据返还给前端
        UserSign userSign = signService.getSignAll(id);
        Long myExperience = userSign.getExperience();
        Long totalSign = userSign.getTotalSign();
        Long continueSign = userSign.getContinueSign();
        //判断签到的等级和排名返还给前端
        if (myExperience!=null) {
            int rank = rankService.rank(myExperience);
            int signRank = rankService.ranSign(myExperience.intValue());
            resultMap.put("rank", rank);
            resultMap.put("signRank", signRank);
        }
        //判断签到的经验值排名
        resultMap.put("myExperience", myExperience);
        resultMap.put("totalSign", totalSign);
        resultMap.put("continueSign", continueSign);
        //resultMap.put("userSign",userSign);
        return resultMap;
    }


    //判定Token传过来的id判定是否可以签到
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getExperience")
    public Map<String, Object> getExperience(@RequestParam("id") Long id) {
        Map<String, Object> resultMap = new HashMap<>();
        //判定是否可以签到,并完成签到
        int result = signService.checkSign(id);
        //获取经验值签到数和总签到数返还给前端
        if (result > 0) {
            //签到成功返还给所有的数据给前端
            UserSign userSign = signService.getSignAll(id);
            Long totalSign = userSign.getTotalSign();
            Long continueSign = userSign.getContinueSign();
            Long experience = signService.getExperience(id);
            //判断签到的等级和排名返还给前端
            int signRank = rankService.ranSign(experience.intValue());
            //判断签到的经验值排名
            int rank = rankService.rank(experience);
            resultMap.put("success", true);
            resultMap.put("rank", rank);
            resultMap.put("message", experience);
            resultMap.put("signRank", signRank);
            resultMap.put("totalSign", totalSign);
            resultMap.put("continueSign", continueSign);
            return resultMap;
        }
        resultMap.put("success", false);
        return resultMap;
    }

    //连续签到的奖励
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getContinueReward")
    public Map<String, Object> getContinueReward(@RequestParam("id") Long id) {
        Map<String, Object> resultMap = new HashMap<>();
        //首先判断是否存在可以领取的连续奖励
        int result = signService.getContinueReward(id);
        if (result == 0) {
            //返还给前端获得奖励后的结果
            UserSign userSign = signService.getSignAll(id);
            Long myExperience = userSign.getExperience();
            Long totalSign = userSign.getTotalSign();
            Long continueSign = userSign.getContinueSign();
            //判断签到的等级和排名返还给前端
            int signRank = rankService.ranSign(myExperience.intValue());
            //判断签到的经验值排名
            int rank = rankService.rank(myExperience);
            resultMap.put("success", true);
            resultMap.put("rank", rank);
            resultMap.put("myExperience", myExperience);
            resultMap.put("signRank", signRank);
            resultMap.put("totalSign", totalSign);
            resultMap.put("continueSign", continueSign);
            return resultMap;
        }
        resultMap.put("success", false);
        return resultMap;
    }

    //获得连续奖励的Button的显示
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/showContinue")
    public Map<String, Object> showContinue(@RequestParam("id") Long id) {
        Map<String, Object> resultMap = new HashMap<>();
        //查询连续签到数处于某个阶段之下判定是否都已经签到成功
        int result = signService.showContinue(id);
        boolean flag = result > 0;
        resultMap.put("success", flag);
        return resultMap;
    }

    //获得总数签到的Button的显示
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/showTotal")
    public Map<String, Object> showTotal(@RequestParam("id") Long id) {
        Map<String, Object> resultMap = new HashMap<>();
        //查询总数签到数处于某个阶段之下判定是否都已经签到成功
        int result=signService.showTotal(id);
        boolean flag = result > 0;
        resultMap.put("success", flag);
        return resultMap;
    }

    //总数签到的奖励
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getTotalReward")
    public Map<String, Object> getTotalReward(@RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //首先先判断是否存在可以领取总数签到的奖励
        int result=signService.getTotalReward(id);
        //result==0说明领取存在的奖励然后返回给前端
        if (result == 0) {
                //返还给前端获得奖励后的结果
                UserSign userSign = signService.getSignAll(id);
                Long myExperience = userSign.getExperience();
                Long totalSign = userSign.getTotalSign();
                Long continueSign = userSign.getContinueSign();
                //判断签到的等级和排名返还给前端
                int signRank = rankService.ranSign(myExperience.intValue());
                //判断签到的经验值排名
                int rank = rankService.rank(myExperience);
                resultMap.put("success", true);
                resultMap.put("rank", rank);
                resultMap.put("myExperience", myExperience);
                resultMap.put("signRank", signRank);
                resultMap.put("totalSign", totalSign);
                resultMap.put("continueSign", continueSign);
                return resultMap;
            }
        resultMap.put("success", false);
        return resultMap;
    }

    //负载均衡添加文章发表经验值
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/articleExperience")
    public void articleExperience(@RequestParam("id")Long id){
        //根据用户Id查询所有，再根据主键Id更新经验值
        UserSign userSign=signService.getSignAll(id);
        //先负载均衡查询会员开通情况
        Map<String,Object> memberAll=signMemberFeign.selectMember(id);
        //两个会员标志
        Object vipMember=memberAll.get("vipMember");
        Object svipMember=memberAll.get("svipMember");
        //首先判断表是否为空值
        UserSignArticle userSignArticle=userSignArticleMapper.selectAllByUserId(id);
        if (userSignArticle==null){
            UserSignArticle userSignArticle1=new UserSignArticle();
            userSignArticle1.setUserId(id);
            userSignArticle1.setSignArticle(1);
            userSignArticle1.setSignThump(0);
            userSignArticle1.setSignComment(0);
            userSignArticle1.setCreateTime(new Date());
            userSignArticle1.setUpdateTime(new Date());
            userSignArticleMapper.insert(userSignArticle1);
            //更新发表文章的经验值
            if (svipMember.equals(1)){
                userSign.setExperience(userSign.getExperience()+15L);
            }else if (vipMember.equals(1)){
                userSign.setExperience(userSign.getExperience()+10L);
            }else {
                userSign.setExperience(userSign.getExperience()+5L);
            }
            //最终根据主键Id更新经验值
            userSignMapper.updateExperienceById(userSign.getExperience(),userSign.getId());
        }else if (userSignArticle.getSignArticle()==0){
            //直接更新次数完成，并完成增加经验值
            userSignArticleMapper.updateSignArticleById(1,userSignArticle.getId());
            //更新发表文章的经验值
            if (svipMember.equals(1)){
                userSign.setExperience(userSign.getExperience()+15L);
            }else if (vipMember.equals(1)){
                userSign.setExperience(userSign.getExperience()+10L);
            }else {
                userSign.setExperience(userSign.getExperience()+5L);
            }
            //最终根据主键Id更新经验值
            userSignMapper.updateExperienceById(userSign.getExperience(),userSign.getId());
        }
    }

    //重置所有的奖励
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/deleteArticleSign")
    public void deleteArticleSign(@RequestParam("userId") Long userId){
        //删除指定UserId的完成表
        userSignArticleMapper.delete(userId);
    }

    //更新零点的文章奖励情况
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/updateZero")
    public void updateZero(@RequestBody UserZeroSign userZeroSign, @RequestParam("id")Long id){
        //根据用户Id查询所有，再根据主键Id更新经验值
        UserSign userSign=signService.getSignAll(id);
        //先负载均衡查询会员开通情况
        Map<String,Object> memberAll=signMemberFeign.selectMember(id);
        //两个会员标志
        Object vipMember=memberAll.get("vipMember");
        Object svipMember=memberAll.get("svipMember");
        if (userZeroSign.getComment()){
            //更新评论经验值
            if (svipMember.equals(1)){
                userSign.setExperience(userSign.getExperience()+50L);
            }else if (vipMember.equals(1)){
                userSign.setExperience(userSign.getExperience()+30L);
            }else {
                userSign.setExperience(userSign.getExperience()+10L);
            }
        }
        if (userZeroSign.getThump()){
            //更新点赞的经验值
            if (svipMember.equals(1)){
                userSign.setExperience(userSign.getExperience()+50L);
            }else if (vipMember.equals(1)){
                userSign.setExperience(userSign.getExperience()+30L);
            }else {
                userSign.setExperience(userSign.getExperience()+10L);
            }
        }
        if (userZeroSign.getRank()){
            //更新排行榜的经验值
            if (svipMember.equals(1)){
                userSign.setExperience(userSign.getExperience()+500L);
            }else if (vipMember.equals(1)){
                userSign.setExperience(userSign.getExperience()+300L);
            }else {
                userSign.setExperience(userSign.getExperience()+100L);
            }
        }
        //最终根据主键Id更新经验值
        userSignMapper.updateExperienceById(userSign.getExperience(),userSign.getId());
    }

    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/selectZero")
    //查询当天签到的完成情况
    public Map<String, Object> selectZero(@RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //首先判断表是否为空值
        UserSignArticle userSignArticle=userSignArticleMapper.selectAllByUserId(id);
        if (userSignArticle==null){
            UserSignArticle userSignArticle1=new UserSignArticle();
            userSignArticle1.setUserId(id);
            userSignArticle1.setSignArticle(0);
            userSignArticle1.setSignThump(0);
            userSignArticle1.setSignComment(0);
            userSignArticle1.setCreateTime(new Date());
            userSignArticle1.setUpdateTime(new Date());
            userSignArticleMapper.insert(userSignArticle1);
            resultMap.put("signArticle",0);
            resultMap.put("signThump",0);
            resultMap.put("signComment",0);
        }else {
            resultMap.put("signArticle",userSignArticle.getSignArticle());
            resultMap.put("signThump",userSignArticle.getSignThump());
            resultMap.put("signComment",userSignArticle.getSignComment());
        }
        resultMap.put("success",true);
        return resultMap;
    }

    //完成每日点赞任务
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/thumpSign")
    public void thumpSign(@RequestParam("userId")Long userId){
        //根据用户Id查询所有，再根据主键Id更新经验值
        UserSign userSign=signService.getSignAll(userId);
        //先负载均衡查询会员开通情况
        Map<String,Object> memberAll=signMemberFeign.selectMember(userId);
        //两个会员标志
        Object vipMember=memberAll.get("vipMember");
        Object svipMember=memberAll.get("svipMember");
        //首先判断表是否为空值
        UserSignArticle userSignArticle=userSignArticleMapper.selectAllByUserId(userId);
        if (userSignArticle==null){
            UserSignArticle userSignArticleThump=new UserSignArticle();
            userSignArticleThump.setUserId(userId);
            userSignArticleThump.setSignArticle(0);
            userSignArticleThump.setSignThump(1);
            userSignArticleThump.setSignComment(0);
            userSignArticleThump.setCreateTime(new Date());
            userSignArticleThump.setUpdateTime(new Date());
            userSignArticleMapper.insert(userSignArticleThump);
            //更新发表文章的经验值
            if (svipMember.equals(1)){
                userSign.setExperience(userSign.getExperience()+3L);
            }else if (vipMember.equals(1)){
                userSign.setExperience(userSign.getExperience()+2L);
            }else {
                userSign.setExperience(userSign.getExperience()+1L);
            }
            //最终根据主键Id更新经验值
            userSignMapper.updateExperienceById(userSign.getExperience(),userSign.getId());
        }else {
            //直接更新次数完成，并完成增加经验值
            userSignArticleMapper.updateThumpById(userSignArticle.getId());
            //更新发表文章的经验值
            if (svipMember.equals(1)){
                userSign.setExperience(userSign.getExperience()+3L);
            }else if (vipMember.equals(1)){
                userSign.setExperience(userSign.getExperience()+2L);
            }else {
                userSign.setExperience(userSign.getExperience()+1L);
            }
            //最终根据主键Id更新经验值
            userSignMapper.updateExperienceById(userSign.getExperience(),userSign.getId());
        }
    }
}