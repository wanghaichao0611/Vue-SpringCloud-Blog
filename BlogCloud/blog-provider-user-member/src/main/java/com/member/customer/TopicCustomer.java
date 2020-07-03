package com.member.customer;


import com.member.config.RabbitTopicConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


//Topic信息消费者
@Component
public class TopicCustomer {

    //Topic路由策略one
    @RabbitListener(queues = RabbitTopicConfig.QUEUE_TOPIC_NAME_ONE)
    public void topicMemberOne(String topicMsgOne){
        System.out.println("Topic的测试信息:"+topicMsgOne);
    }

    //Topic路由策略two
    @RabbitListener(queues = RabbitTopicConfig.QUEUE_TOPIC_NAME_TWO)
    public void topicMemberTwo(String topicMsgTwo){
        System.out.println("Topic的测试信息:"+topicMsgTwo);
    }

    //Topic路由策略three
    @RabbitListener(queues = RabbitTopicConfig.QUEUE_TOPIC_NAME_THREE)
    public void topicMemberThree(String topicMsgThree){
        System.out.println("Topic的测试信息:"+topicMsgThree);
    }

}
