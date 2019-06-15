package com.Listener;


import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author ljp
 */
@Component
public class RabbitMqLReceiver {
    public static final Logger logger = LoggerFactory.getLogger(RabbitMqLReceiver.class);


    @RabbitListener(queues = "REDIRECT_QUEUE")
    public void on(Message message, Channel channel) throws IOException, InterruptedException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        System.out.println("DIRECT_QUEUE:" + new String(message.getBody()));
        Thread.sleep(10000);
    }


}
