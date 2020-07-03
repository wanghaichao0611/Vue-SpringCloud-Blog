package com.Controller;

import com.Service.UsernameToken;
import com.feign.UserSignFeign;
import com.mapper.UserMapperToken;
import com.tokenauthentication.annotation.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class UserSignController {

    //Token双向绑定取username
    @Autowired
    private HttpServletRequest requestUsername;

    //根据Headers中的TOKEN获得username
    @Autowired
    private UsernameToken usernameToken;

    //获得ID主键
    @Autowired
    private UserMapperToken userMapperToken;

    //签到的feign
    @Autowired
    private UserSignFeign userSignFeign;

    //签到按钮的显示(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/showSignFeign")
    @AuthToken
    public Map<String, Object> showSignFeign(){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        return userSignFeign.showSign(id);
    }


    //获得所有的排名和经验值(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getSignAllFeign")
    @AuthToken
    public Map<String,Object> getSignAllFeign(){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        return userSignFeign.getSignAll(id);
    }

    //签到的Button(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getExperienceButton")
    @AuthToken
    public Map<String,Object> getExperienceButton(){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        return userSignFeign.getExperience(id);
    }

    //连续签到的奖励(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getContinueRewardFeign")
    @AuthToken
    public Map<String, Object> getContinueRewardFeign(){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        return userSignFeign.getContinueReward(id);
    }

    //连续签到的按钮的显示(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/showContinueButton")
    @AuthToken
    public Map<String, Object> showContinueButton(){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        return userSignFeign.showContinue(id);
    }


    //连续签到的按钮的显示(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/showTotalButton")
    @AuthToken
    public Map<String, Object> showTotalButton(){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        return userSignFeign.showTotal(id);
    }

    //总数签到的奖励(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getTotalRewardFeign")
    @AuthToken
    public Map<String, Object> getTotalRewardFeign(){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        return userSignFeign.getTotalReward(id);
    }

    //查询当天文章完成奖励情况
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/selectZeroFeign")
    @AuthToken
    public Map<String, Object> selectZeroFeign(){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        return userSignFeign.selectZero(id);
    }

}
