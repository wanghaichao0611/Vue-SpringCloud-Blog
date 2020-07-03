package com.ServiceImpl;

import com.Controller.FileUploadController;
import com.Entity.UserAvatar;
import com.Mapper.UserAvatarMapper;
import com.Service.UploadService;
import com.Service.UserAvatarService;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.net.URL;

import java.util.Date;
import java.util.UUID;

@Service
public class UserAvatarServiceImpl implements UserAvatarService {

    //上传的Mapper
    @Autowired
    UserAvatarMapper userAvatarMapper;

    //上传的服务
    @Autowired
    UploadService uploadService;

    //失败
    private static final int FAILED = 0;
    //成功
    private static final int SUCCESS = 1;


    //用户头像jpg图片的服务
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public String avatarCheck(MultipartFile file, String username) {

        //查询是否存在头像name
        String oldObjectName = userAvatarMapper.selectAvatarNameByUsername(username);
        //如果name为空
        if (oldObjectName==null) {
            String url=uploadService.registerAvatar(file, username);
            return url;
        }else {
            //删除服务器上的文件
            uploadService.deleteAvatar(file,oldObjectName);
            String url=uploadService.registerAvatar(file, username);
            return url;
        }
    }

    //查询发表人博客的用户头像url
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public String getAuthorAvatarAll(String authorId) {
        String result = "";
        if (authorId.length() > 0) {
            String[] avatarId = authorId.split(",");
            for (String id : avatarId) {
                String avatarUrl = userAvatarMapper.selectAvatarUrlByAvatarId(Long.parseLong(id));
                result =result.toString()+avatarUrl + ",";
            }
        }
        return result;
    }
}
