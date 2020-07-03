package com.member.customer;


import com.member.config.RabbitFanoutConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


//Fanout信息消费者
@Component
public class FanoutCustomer {

    //Fanout路由策略One
    @RabbitListener(queues = RabbitFanoutConfig.QUEUE_FANOUT_NAME_ONE)
    public void fanoutMemberOne(String fanoutMsgOne){
        System.out.println("Fanout的测试信息:"+fanoutMsgOne);
    }

    //Fanout路由策略Two
    @RabbitListener(queues = RabbitFanoutConfig.QUEUE_FANOUT_NAME_TWO)
    public void fanoutMemberTwo(String fanoutMsgTwo){
        System.out.println("Fanout的测试信息:"+fanoutMsgTwo);
    }

}
