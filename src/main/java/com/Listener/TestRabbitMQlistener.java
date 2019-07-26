package com.Listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TestRabbitMQlistener {

    @RabbitListener(queues = "Test_REDIRECT_QUEUE")
    public void on(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),true );
        System.out.println(new String(message.getBody()));
    }
}

