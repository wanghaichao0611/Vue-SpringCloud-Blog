package com.member.Util;

import org.springframework.amqp.AmqpException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.stereotype.Repository;


public class MyMessagePostProcessor implements MessagePostProcessor {

    private final Long ttl;

    public MyMessagePostProcessor(final Long ttl) {
        this.ttl = ttl;
    }

    @Override
    public Message postProcessMessage(final Message message) throws AmqpException {
        message.getMessageProperties().getHeaders().put("expiration", ttl.toString());
        return message;
    }
}
