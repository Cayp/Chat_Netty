package com.Config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class TestRabbitConfig {


    @Bean("deadLetterQueueTest")
    public Queue directQueue() {
        HashMap<String, Object> map = new HashMap<>(2);
        //声明消息变成死信后,交给的交换机和路由键
        map.put("x-dead-letter-exchange", "Test_DEADLETTER_EXCHANGE");
        map.put("x-dead-letter-routing-key", "Test_REDIRECT_QUEUE_KEY");
        return QueueBuilder.nonDurable("Test_DEADLETTER_QUEUE").withArguments(map).build();
    }

    @Bean("deadLetterExchangeTest")
    public Exchange directExchange() {
        return ExchangeBuilder.directExchange("Test_DEADLETTER_EXCHANGE").durable(false).build();
    }


    @Bean("redirectQueueTest")
    public Queue redirectQueue() {
        return QueueBuilder.nonDurable("Test_REDIRECT_QUEUE").build();
    }


    @Bean
    public Binding directBinding2(@Qualifier("deadLetterQueueTest") Queue queue, @Qualifier("deadLetterExchangeTest") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("TEST_DIRECT_ROUTING_KEY").noargs();
    }


    @Bean
    public Binding directBinding3(@Qualifier("redirectQueueTest") Queue queue, @Qualifier("deadLetterExchangeTest") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("Test_REDIRECT_QUEUE_KEY").noargs();
    }


}
