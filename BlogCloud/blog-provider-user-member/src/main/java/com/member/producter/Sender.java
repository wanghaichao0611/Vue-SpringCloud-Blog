package com.member.producter;

import com.member.config.RabbitDirectConfig;
import com.member.config.RabbitFanoutConfig;
import com.member.config.RabbitHeaderConfig;
import com.member.config.RabbitTopicConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


//信息生产者
@Service
public class Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //direct生产者(routingKey有关)
    public void directSend() {
        System.out.println("Direct发送消息...");
        rabbitTemplate.convertAndSend(RabbitDirectConfig.QUEUE_DIRECT_EXCHANGE_NAME,
                RabbitDirectConfig.QUEUE_DIRECT_NAME, "DirectTest");
    }

    //fanout生产者(routingKey无关)
    public void fanoutSend() {
        System.out.println("Fanout发送消息...");
        rabbitTemplate.convertAndSend(RabbitFanoutConfig.QUEUE_FANOUT_EXCHANGE_NAME,
                null, "FanoutTest");
    }

    //topic生产者(routingKey有关)
    public void topicSend() {
        System.out.println("Topic发送消息...");
        rabbitTemplate.convertAndSend(RabbitTopicConfig.QUEUE_TOPIC_EXCHANGE_NAME,
                "topicMemberOne.one", "TopicTestOne-1");
        rabbitTemplate.convertAndSend(RabbitTopicConfig.QUEUE_TOPIC_EXCHANGE_NAME,
                "topicMemberTwo.one", "TopicTestTwo-1");
        rabbitTemplate.convertAndSend(RabbitTopicConfig.QUEUE_TOPIC_EXCHANGE_NAME,
                "topicMemberOne.topicMemberThree",
                "TopicTestOne-2");
        rabbitTemplate.convertAndSend(RabbitTopicConfig.QUEUE_TOPIC_EXCHANGE_NAME,
                "topicMemberTwo.topicMemberThree",
                "TopicTestTwo-2");
        rabbitTemplate.convertAndSend(RabbitTopicConfig.QUEUE_TOPIC_EXCHANGE_NAME,
                "topicMemberThree.one", "TopicTestThree-1");
    }

    //header生产者(与routingKey无关)
    public void sendHeader(){
        System.out.println("Header发送信息...");
        Message messageOne= MessageBuilder
                .withBody("hello header! one".getBytes())
                .setHeader("name","whc").build();
        Message messageTwo=MessageBuilder
                .withBody("hello header! two".getBytes())
                .setHeader("all","666").build();
        rabbitTemplate.send(RabbitHeaderConfig.QUEUE_Header_EXCHANGE_NAME,null,messageOne);
        rabbitTemplate.send(RabbitHeaderConfig.QUEUE_Header_EXCHANGE_NAME,null,messageTwo);
    }
}