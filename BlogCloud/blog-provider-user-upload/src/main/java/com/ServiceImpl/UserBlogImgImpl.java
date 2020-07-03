package com.ServiceImpl;

import com.Service.UploadService;
import com.Service.UserBlogImg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
public class UserBlogImgImpl implements UserBlogImg {

    //上传的服务
    @Autowired
    UploadService uploadService;

    //保存博客的图片并且返回URL给前端绑定显示
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public String publishBlogImg(MultipartFile blogImg, String username, Long id) {
        String url=uploadService.publishBlogImg(blogImg,username,id);
        return url;
    }


}
