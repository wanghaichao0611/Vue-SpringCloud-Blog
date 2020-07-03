package com.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UploadService {

    //注册的时候插入用户头像
    String registerAvatar(MultipartFile file, String username);

    //删除本地服务器上的头像
    void deleteAvatar(MultipartFile file,String oldObjectName);

    //上传博客的图片
    String publishBlogImg(MultipartFile file, String username, Long id);
}
