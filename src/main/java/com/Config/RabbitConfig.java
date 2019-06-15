package com.Config;

import com.rabbitmq.client.AMQP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.LifecycleProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;
import java.util.HashMap;


/**
 * rabbitmq配置死信队列类
 *
 * @author ljp
 */
@Configuration
public class RabbitConfig {

    @Resource
    RabbitTemplate rabbitTemplate;


    @Bean("rabbitTemplate1")
    public AmqpTemplate rabbitTemplate() {
        Logger logger = LoggerFactory.getLogger(RabbitTemplate.class);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback((message, i, s, s1, s2) -> {
            logger.info("message_id:{},code:{},reason:{},exchange:{},routeKey:{}", 1, i, s, s1, s2);
        });
        rabbitTemplate.setConfirmCallback((correlationData, b, s) -> {
            if (b) {
                logger.info("send to Exchange success,id:{}", 1);
            } else {
                logger.info("send to Exchange fail,id:{},reason:{}", 1, s);
            }
        });
        return null;
    }

    @Bean("deadLetterExchange")
    public Exchange directExchange() {
        return ExchangeBuilder.directExchange("DEADLETTER_EXCHANGE").durable(true).build();
    }


    /**
     * @return 死信队列
     */
    @Bean("deadLetterQueue")
    public Queue directQueue() {
        HashMap<String, Object> map = new HashMap<>(2);
        //声明消息变成死信后,交给的交换机和路由键
        map.put("x-dead-letter-exchange", "DEADLETTER_EXCHANGE");
        map.put("x-dead-letter-routing-key", "REDIRECT_QUEUE_KEY");
        return QueueBuilder.durable("DEADLETTER_QUEUE").withArguments(map).build();
    }


    /**
     * @return 死信转发给的新队列
     */
    @Bean("redirectQueue")
    public Queue directQueue1() {
        return QueueBuilder.durable("REDIRECT_QUEUE").build();
    }


    @Bean
    public Binding directBinding(@Qualifier("deadLetterQueue") Queue queue, @Qualifier("deadLetterExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("DIRECT_ROUTING_KEY").noargs();
    }


    @Bean
    public Binding directBinding1(@Qualifier("redirectQueue") Queue queue, @Qualifier("deadLetterExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("REDIRECT_QUEUE_KEY").noargs();
    }

}
