package com.member.customer;

import com.member.config.RabbitDirectConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


//Direct信息消费者
@Component
public class DirectCustomer {

    //Direct路由策略
    @RabbitListener(queues = RabbitDirectConfig.QUEUE_DIRECT_NAME)
    public void directMember(String directMsg){
        System.out.println("Direct的测试信息:"+directMsg);
    }
}
