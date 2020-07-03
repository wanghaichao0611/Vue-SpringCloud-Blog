package com.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserBlogImg {

    //保存博客的图片并且返回URL给前端绑定显示
    String publishBlogImg(MultipartFile blogImg,String username,Long id);

}
