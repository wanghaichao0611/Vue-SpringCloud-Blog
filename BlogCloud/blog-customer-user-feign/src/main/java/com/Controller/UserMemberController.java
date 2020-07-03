package com.Controller;


import com.Service.UsernameToken;
import com.feign.UserMemberFeign;
import com.mapper.UserMapperToken;
import com.tokenauthentication.annotation.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class UserMemberController {

    //Token双向绑定取username
    @Autowired
    private HttpServletRequest requestUsername;

    //根据Headers中的TOKEN获得username
    @Autowired
    private UsernameToken usernameToken;

    //获得ID主键
    @Autowired
    private UserMapperToken userMapperToken;

    @Autowired
    UserMemberFeign userMemberFeign;

    //获取整个会员表返还给前端()
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getMemberAllFeign")
    @AuthToken
    public Map<String,Object> getMemberAllFeign(){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        return userMemberFeign.getMemberAll(id);
    }

}
