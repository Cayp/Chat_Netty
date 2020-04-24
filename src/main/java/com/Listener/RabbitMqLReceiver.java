package com.Listener;


import com.Service.RedPacketByRedisService;
import com.Utils.Const;
import com.Utils.SendMailUtils;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * mq监听处理类
 *
 * @author ljp
 */
@Component
public class RabbitMqLReceiver {
    public static final Logger logger = LoggerFactory.getLogger(RabbitMqLReceiver.class);
    public static String sha_mail;
    @Resource
    JedisPool jedisPool;

    @Resource(name = "redPacketByRedisServiceImpl")
    private
    RedPacketByRedisService redPacketByRedisService;

    @RabbitListener(queues = "REDIRECT_QUEUE")
    public void on(Message message, Channel channel) throws IOException {
        String[] messageStr = new String(message.getBody()).split("-");
        String userid = messageStr[0];
        String redpacketid = messageStr[1];
        Jedis jedis = jedisPool.getResource();
        //判断list redpacket未抢红包的key是否存在,存在则是未抢完进行持久化处理,不存在则可忽略处理
        if (jedis.exists(Const.LREDPACKETKEY + redpacketid)) {
            redPacketByRedisService.persistToSql(Long.parseLong(redpacketid), Long.parseLong(userid));
        }
        logger.info("redpacketid:{} 已处理", redpacketid);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
    }

    @RabbitListener(queues = "REDIRECT_MAIL_QUEUE")
    public void mailon(Message message, Channel channel) {
        String[] messageStr = new String(message.getBody()).split("-");
        String mail = messageStr[0];
        int type = Integer.parseInt(messageStr[1]);
        String code = messageStr[2];
        Jedis jedis = jedisPool.getResource();
        if (sha_mail == null || jedis.scriptExists(sha_mail)) {
            sha_mail = jedis.scriptLoad(Const.MAILCODE_LUA);
        }
        System.out.println(mail + "-" + code);
        String sendCode = (String) jedis.evalsha(sha_mail, 1, mail + type, code);
        jedis.close();

        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            SendMailUtils.sendMail(sendCode, mail, type);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(mail + " " + e.getMessage());
        }
    }


}
