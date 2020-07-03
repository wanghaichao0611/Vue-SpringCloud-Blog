package com.failed;


import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;


public class MemberTimeJob extends QuartzJobBean {

    //IOC配置不成功
    public void executeInternal(JobExecutionContext jobExecutionContext) {
        System.out.println("hello:"+new Date());
    }
}
