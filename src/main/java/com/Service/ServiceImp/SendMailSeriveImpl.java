package com.Service.ServiceImp;

import com.Service.SendMailSerivce;
import com.Utils.Const;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SendMailSeriveImpl  implements SendMailSerivce{

    @Resource
    RabbitTemplate rabbitTemplate;


    @Override
    public void sendMail(String mail, String code) {
        String mqString = mail + "-" + code;
        rabbitTemplate.convertAndSend("MAIL_EXCHANGE","MAIL_QUEUE_KEY" , mqString);
    }
}
