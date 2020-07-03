package com.Controller;

import com.Service.UsernameToken;
import com.feign.UserPictureFeign;
import com.mapper.UserMapperToken;
import com.tokenauthentication.annotation.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserUploadController {

    //Token双向绑定取username
    @Autowired
    private  HttpServletRequest requestUsername;

    //获取图片的id
    @Autowired
    private UserMapperToken userMapperToken;

    //根据Headers中的TOKEN获得username
    @Autowired
    private UsernameToken usernameToken;

    //图片和文件的上传
    @Autowired
    UserPictureFeign userPictureFeign;

    //注册时上传用户头像的服务(无Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @RequestMapping(value = "/userPictureFeign",method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AuthToken
    public String userPictureFeign(@RequestPart(value = "file") MultipartFile file) {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userPictureFeign.userPictureUpload(file,username);
    }

    //注册时上传用户头像的服务(无Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @RequestMapping(value = "/articlePublishImgFeign",method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AuthToken
    public String articlePublishImgFeign(@RequestPart(value = "blogImg") MultipartFile blogImg) {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        return userPictureFeign.articlePublishImg(blogImg,username,id);
    }
}
