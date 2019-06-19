package com.Listener;


import com.Service.RedPacketByRedisService;
import com.Utils.Const;
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
 * @author ljp
 */
@Component
public class RabbitMqLReceiver {
    public static final Logger logger = LoggerFactory.getLogger(RabbitMqLReceiver.class);

    @Resource
    JedisPool jedisPool;

    @Resource(name = "redPacketByRedisServiceImpl")
    private
    RedPacketByRedisService redPacketByRedisService;

    @RabbitListener(queues = "REDIRECT_QUEUE")
    public void on(Message message, Channel channel) throws IOException, InterruptedException {
        String[] messageStr = new String(message.getBody()).split("-");
        String userid = messageStr[0];
        String redpacketid = messageStr[1];
        Jedis jedis = jedisPool.getResource();
        //判断list redpacket未抢红包的key是否存在,存在则是未抢完进行持久化处理,不存在则可忽略处理
        if (jedis.exists(Const.LREDPACKETKEY + redpacketid)) {
            redPacketByRedisService.persistToSql(Long.parseLong(redpacketid), Integer.parseInt(userid));
        }
        logger.info("redpacketid:{} 已处理",redpacketid);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
    }


}
