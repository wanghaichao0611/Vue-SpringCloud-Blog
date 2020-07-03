package com.feign;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@FeignClient(name = "blog-provider-user-upload",configuration = UserPictureFeign.MultipartSupportConfig.class,fallbackFactory = UserPictureFallback.class )
public interface UserPictureFeign {

    //上传和替换用户头像的Feign
    @RequestMapping(value = "/userPictureUpload",method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String userPictureUpload(@RequestPart(value = "file") MultipartFile file,@RequestParam("username") String username);

    //上传博客图片的Feign
    @RequestMapping(value = "/articlePublishImg",method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String articlePublishImg(@RequestPart(value = "blogImg") MultipartFile blogImg,@RequestParam("username") String username,
                                    @RequestParam("id") Long id);

    class  MultipartSupportConfig{
        //@Autowired
        //private ObjectFactory<HttpMessageConverters> messageConverters;
        @Bean
        public Encoder feignFormEncoder(){
            return new SpringFormEncoder();
        }
    }
}
@Component
class UserPictureFallback implements FallbackFactory<UserPictureFeign>{

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(UserPictureFallback.class);

    @Override
    public UserPictureFeign create(Throwable throwable) {
        return new UserPictureFeign() {

            //上传用户头像的fallback
            @Override
            public String userPictureUpload(MultipartFile file,String username) {
                UserPictureFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "上传超时，请重新再试!");
                return "上传超时";
            }

            //上传博客图片的fallback
            @Override
            public String articlePublishImg(MultipartFile blogImg, String username, Long id) {
                UserPictureFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "上传超时，请重新再试!");
                return "上传超时";
            }
        };
    }
}

