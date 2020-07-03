package com.member.feign;


import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "blog-provider-user",fallbackFactory = MemberNameFallback.class)
public interface MemberNameFeign {

    //负载均衡更新紫色名字
    @RequestMapping(value = "/updateName", method = RequestMethod.POST)
    public void updateName(@RequestParam("id")Long id);

    //负载均衡查询紫色名字
    @RequestMapping(value = "/selectName", method = RequestMethod.POST)
    public String selectName(@RequestParam("id")Long id);

    //负载均衡失效紫色名字
    @RequestMapping(value = "/deleteName", method = RequestMethod.POST)
    public void deleteName(@RequestParam("id")Long id);
}
@Component
class MemberNameFallback implements FallbackFactory<MemberNameFeign>{

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(MemberNameFallback.class);

    @Override
    public MemberNameFeign create(Throwable throwable) {
        return new MemberNameFeign(){

            //负载均衡更新紫色名字的fallback
            @Override
            public void updateName(Long id) {
                MemberNameFallback.LOGGER.info("造成回退的原因是:",throwable);
            }

            //负载均衡查询紫色名字的fallback
            public String selectName(@RequestParam("id")Long id){
                MemberNameFallback.LOGGER.info("造成回退的原因是:",throwable);
                return "查询紫色名字超时";
            }

            //负载均衡失效紫色名字的fallback
            @Override
            public void deleteName(Long id) {
                MemberNameFallback.LOGGER.info("造成回退的原因是:",throwable);
            }
        };
    }
}