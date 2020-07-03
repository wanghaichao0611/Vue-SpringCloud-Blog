package com.member.customer;

import com.member.config.RabbitHeaderConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


//Header信息消费者
@Component
public class HeaderCustomer {

    //Header路由策略One
    @RabbitListener(queues = RabbitHeaderConfig.QUEUE_Header_NAME_ONE)
    public void headerMemberOne(byte[] headerMsgOne){
        System.out.println("Header的测试信息:"+new String(headerMsgOne,0,headerMsgOne.length));
    }

    //Header路由策略Two
    @RabbitListener(queues = RabbitHeaderConfig.QUEUE_Header_NAME_TWO)
    public void headerMemberTwo(byte[] headerMsgTwo){
        System.out.println("Header的测试信息:"+new String(headerMsgTwo,0,headerMsgTwo.length));
    }
}
