package com.Controller;

import com.Service.UsernameToken;
import com.feign.UserEsFeign;
import com.mapper.UserMapperToken;
import com.tokenauthentication.annotation.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
public class UserEsController {

    //Token双向绑定取username
    @Autowired
    private HttpServletRequest requestUsername;

    //根据Headers中的TOKEN获得username
    @Autowired
    private UsernameToken usernameToken;

    //获得ID主键
    @Autowired
    private UserMapperToken userMapperToken;

    //Es的负载均衡
    @Autowired
    private UserEsFeign userEsFeign;

    //HomePage第一个主页的查询文章(page代表从第几条开始，count代表查询多少条,类似mysql的limit分页参数)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/esPageAllFeign")
    @AuthToken
    public Map<String,Object> esPageAllFeign(@RequestParam(name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "count", defaultValue = "5") int count,
                                              @RequestParam("categoryName") String categoryName,
                                              @RequestParam("keyWord") String  keyWord){
        return userEsFeign.esPageAll(page,count,categoryName,keyWord);
    }

    //博客的分类主页搜索
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/esCategoryAllFeign")
    @AuthToken
    public Map<String,Object> esCategoryAllFeign(@RequestParam("categoryName") String categoryName){
        return userEsFeign.esCategoryAll(categoryName);
    }
}
