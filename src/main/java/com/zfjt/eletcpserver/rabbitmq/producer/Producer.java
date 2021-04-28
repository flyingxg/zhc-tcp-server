package com.zfjt.eletcpserver.rabbitmq.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Producer {
    @Autowired
    private AmqpTemplate template;

    public void send() {
        template.convertAndSend("queue", "hello,rabbit~");
    }

    // send raw message to consumer in Message form
    public void send(String msg) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        messageProperties.setTimestamp(new Date());
        messageProperties.setContentEncoding("UTF-8");
        messageProperties.setContentLength(msg.getBytes().length);
        Message message = new Message(msg.getBytes(), messageProperties);

        template.convertAndSend("zhc.exchange", "zhc_queue_key", message);
    }
}
