package com.ServiceImpl;

import com.Entity.UserArticleImg;
import com.Mapper.UserArticleImgMapper;
import com.Mapper.UserAvatarMapper;
import com.Service.UploadService;
import com.aliyun.oss.OSS;
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
public class UploadServiceImpl implements UploadService {

    //上传用户头像的服务
    @Autowired
    UserAvatarMapper userAvatarMapper;

    //上传博客图片的服务
    @Autowired
    UserArticleImgMapper userArticleImgMapper;

    // Beijing的服务器
    private String endpoint = "http://oss-cn-beijing.aliyuncs.com";

    // 我的API
    private String accessKeyId = "阿里云操作账号";
    private String accessKeySecret = "阿里云操作密码";

    //仓库的名称
    private String bucketName = "文件存储的位置名称";

    //日志的输出
    private Logger logger= LoggerFactory.getLogger(UserAvatarServiceImpl.class);


    //更新头像并且返回给前端url
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public String registerAvatar(MultipartFile file, String username) {

        //开启连接途径
        OSS  ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        //Url过期的时间(10年)
        Date expiration = new Date(new Date().getTime() + 3600L * 1000 * 24 * 365 * 10);
        //原先的名字
        String oldName = file.getOriginalFilename();
        //图片的新名字
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."), oldName.length());
        try {
            // 完整的上传路径
            String objectName = username + "/" + newName;
            //文件的url
            logger.info(objectName);
            // 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称（objectName）。
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(file.getBytes()));
            //生成url保存到数据库和前端
            URL url = ossClient.generatePresignedUrl(bucketName, objectName, expiration);
            //如果url存在则保存url到数据库中
            if (!url.toString().isEmpty()) {
                //name-url相互对应
                userAvatarMapper.updateAvatarNameAndAvatarUrlByUsername(objectName, url.toString(), username);
            }
            //返还外网url给前端
            return url.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
        return "上传失败";
    }


    //删除服务器上的文件
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public void deleteAvatar(MultipartFile file, String oldObjectName) {
        //删除服务器中的文件
        try {
            //开启连接途径
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            ossClient.deleteObject(bucketName, oldObjectName);
            ossClient.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //上传博客的图片
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    @Override
    public String publishBlogImg(MultipartFile file, String username, Long id) {
        //开启连接途径
        OSS  ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        //Url过期的时间(10年)
        Date expiration = new Date(new Date().getTime() + 3600L * 1000 * 24 * 365 * 10);
        //原先的名字
        String oldName = file.getOriginalFilename();
        //图片的新名字
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."), oldName.length());
        try {
            // 完整的上传路径
            String objectName = "blogImg"+"/"+username + "/" + newName;
            //文件的url
            logger.info(objectName);
            // 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称（objectName）。
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(file.getBytes()));
            //生成url保存到数据库和前端
            URL url = ossClient.generatePresignedUrl(bucketName, objectName, expiration);
            //如果url存在则保存url到数据库中
            if (!url.toString().isEmpty()) {
                //把博客图片的信息插入到表中
                UserArticleImg userArticleImg=new UserArticleImg();
                userArticleImg.setArticleAuthorId(id);
                userArticleImg.setUsername(username);
                userArticleImg.setBlogimgName(objectName);
                userArticleImg.setBlogimgUrl(url.toString());
                userArticleImg.setBlogimgcreatetime(new Date());
                userArticleImgMapper.insert(userArticleImg);
            }
            //返还外网url给前端
            return url.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
        return "上传失败";
    }
}
