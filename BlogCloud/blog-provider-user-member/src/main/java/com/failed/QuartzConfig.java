package com.failed;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.*;

//配置不成功，需要升级大SpringBoot2.0X，或者自行原生API开启。
@Configuration
public class QuartzConfig {
    //配置第一个Job
    @Bean
    MethodInvokingJobDetailFactoryBean jobDetailFactoryBean(){
        MethodInvokingJobDetailFactoryBean bean=new MethodInvokingJobDetailFactoryBean();
        bean.setTargetBeanName("memberJob");
        bean.setTargetMethod("memberTime");
        return bean;
    }

    //配置第二个Job
    @Bean
    JobDetailFactoryBean memberJobDetail(){
        JobDetailFactoryBean bean=new JobDetailFactoryBean();
        bean.setJobClass(MemberTimeJob.class);
        JobDataMap jobDataMap=new JobDataMap();
        jobDataMap.put("memberTime","nightThree");
        bean.setJobDataAsMap(jobDataMap);
        bean.setDurability(true);
        return bean;
    }


    //配置SimpleTrigger
    SimpleTriggerFactoryBean simpleTrigger(){
        SimpleTriggerFactoryBean bean=new SimpleTriggerFactoryBean();
        bean.setJobDetail(jobDetailFactoryBean().getObject());
        bean.setRepeatCount(3);
        bean.setStartDelay(1000);
        bean.setRepeatInterval(2000);
        return bean;
    }

    //配置Trigger
    @Bean
    CronTriggerFactoryBean cronTrigger(){
        CronTriggerFactoryBean bean=new CronTriggerFactoryBean();
        bean.setJobDetail(memberJobDetail().getObject());
        //0 0 3 * * ? *
        bean.setCronExpression("0 0 3 * * ? *");
        return bean;
    }

    //配置Schedule
    @Bean
    SchedulerFactoryBean schedulerFactoryBean(){
        SchedulerFactoryBean bean=new SchedulerFactoryBean();
        SimpleTrigger simpleTrigger=simpleTrigger().getObject();
        CronTrigger cronTrigger=cronTrigger().getObject();
        bean.setTriggers(cronTrigger,simpleTrigger);
        return bean;
    }


}
