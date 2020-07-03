package com.Controller;


import com.Service.UserAvatarService;
import com.Service.UserBlogImg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


@RestController
public class FileUploadController {

    //用户的头像
    @Autowired
    private UserAvatarService userAvatarService;

    //发表博客的图片
    @Autowired
    private UserBlogImg userBlogImg;

    //注册(替换)用户头像
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/userPictureUpload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String userPictureUpload(@RequestPart(value = "file") MultipartFile file,@RequestParam("username") String username){
        String realUrl=userAvatarService.avatarCheck(file,username);
        return realUrl;
    }

    //发表博客用的图片
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/articlePublishImg",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String articlePublishImg(@RequestPart(value = "blogImg") MultipartFile blogImg,@RequestParam("username") String username,
                                    @RequestParam("id") Long id){
        String realUrl=userBlogImg.publishBlogImg(blogImg,username,id);
        return realUrl;
    }

    //负载均衡查询用户的头像地址
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/getAuthorAvatarAll")
    public Map<String,Object> getAuthorAvatarAll(@RequestParam("authorIdList") String authorId){
        Map<String,Object> objectMap=new HashMap<>();
        if (authorId==null){
            objectMap.put("success","failed");
            return objectMap;
        }
        String avatarUrlAll=userAvatarService.getAuthorAvatarAll(authorId);
        objectMap.put("success","success");
        objectMap.put("avatarUrlAll",avatarUrlAll);
        return objectMap;
    }
}