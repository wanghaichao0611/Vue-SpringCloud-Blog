package com.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserAvatarService {

    //先判断是否为空值
    String avatarCheck(MultipartFile file,String username);

    //查询发表人博客的用户头像url
    String getAuthorAvatarAll(String authorId);

}
