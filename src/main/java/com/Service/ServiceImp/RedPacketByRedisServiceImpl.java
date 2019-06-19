package com.Service.ServiceImp;

import com.Entity.PubRedPacket;
import com.Entity.UserRedPacket;
import com.Service.RedPacketByRedisService;
import com.Service.RedPacketBySqlService;
import com.Utils.Const;
import com.Utils.CutPointUtils;
import com.Utils.StringObjectUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 发布红包,抢红包的service类
 *
 * @author ljp
 */

@Service
public class RedPacketByRedisServiceImpl implements RedPacketByRedisService {

    private static String sha1_Pub = null;
    private static String sha1_Get = null;

    @Autowired
    JedisPool jedisPool;

    @Resource(name = "redPacketBySqlServiceImpl")
    RedPacketBySqlService redPacketBySqlService;

    @Resource
    RabbitTemplate rabbitTemplate;

    private static MessagePostProcessor messagePostProcessor = message -> {
        MessageProperties messageProperties = message.getMessageProperties();
        messageProperties.setContentEncoding("utf-8");
        messageProperties.setExpiration(Const.REDPACKETOUTTIME);
        return message;
    };

    /**
     * 发布红包
     *
     * @param userId
     * @param money
     * @param redPacketType
     * @param groupId
     * @param size
     * @return
     */

    @Override
    public PubRedPacket publishRedPacket(int userId, double money, int redPacketType, int groupId, int size) {
        Long redPacketId = -1L;
        ArrayList<String> parts = CutPointUtils.getRedPacketPartsByTypeId(money, size, redPacketType);
        long time = System.currentTimeMillis();
        ArrayList<String> redPacketIdL = new ArrayList<>();
        Jedis jedis = jedisPool.getResource();
        String respo;
        try {
            //获取存在redis的记录红包数的变量作为id并自增
            redPacketId = jedis.incr("RedPacketId");
            redPacketIdL.add(Long.toString(redPacketId));
            redPacketIdL.add(Integer.toString(size));
            redPacketIdL.add(Double.toString(money));
            //判断是否有脚本的sha记录和在redis上脚本是否还存活
            if (sha1_Pub == null || jedis.scriptExists(sha1_Pub)) {
                sha1_Pub = jedis.scriptLoad(Const.PUBLISHREDPACKET_LUA);
            }
            //redis返回的结果
            respo = (String) (jedis.evalsha(sha1_Pub, redPacketIdL, parts));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            jedis.close();

        }
        PubRedPacket pubRedPacket = new PubRedPacket(redPacketId, userId, redPacketType, size, money, parts, time);
        //保存红包信息进mysql
        redPacketBySqlService.addRedPacketMessage(pubRedPacket);
        //发送红包信息进rabbitmq 数据格式为 A-B A为userid B为红包id
        String mqString = String.valueOf(userId)+"-"+String.valueOf(redPacketId);
        rabbitTemplate.convertAndSend(Const.DLEXCHANE, Const.DLQUEUEROUTINGKEY, mqString, messagePostProcessor);
        //成功响应码200
        if (respo != null && Const.SUCCESS.equals(respo)) {
            return pubRedPacket;
        }
        return null;
    }

    /**
     * 抢红包
     *
     * @param redPacketId
     * @param userId
     * @return
     */
    @Override
    public UserRedPacket getRedPacket(long redPacketId, int userId) {
        long time = System.currentTimeMillis();
        Jedis jedis = jedisPool.getResource();
        String respo;
        if (sha1_Get == null || jedis.scriptExists(sha1_Get)) {
            sha1_Get = jedis.scriptLoad(Const.GETREDPACKET_LUA);
        }
        try {
            respo = (String) jedis.evalsha(sha1_Get, 2, Long.toString(redPacketId), "", Integer.toString(userId), Long.toString(time));
        } finally {
            jedis.close();
        }
        String[] split = respo.split("-");
        UserRedPacket userRedPacket;
        //解析返回的结果,以 ‘-’作切割,前面是响应码,后面是金额,除错误响应码外:0,1
        switch (split[0]) {
            //已抢过的情况  0
            case Const.EXIST:
                userRedPacket = new UserRedPacket(redPacketId, userId, "0", time, Const.EXIST);
                break;
            //当抢到最后一个红包,将红包已消费的情况持久化到Mysql 300
            case Const.LASTONE:
                userRedPacket = new UserRedPacket(redPacketId, userId, split[1], time, Const.LASTONE);
                persistToSql(redPacketId, userId);
                break;
            //没抢到 100
            case Const.LOOT:
                userRedPacket = new UserRedPacket(redPacketId, userId, "0", time, Const.LOOT);
                break;
            //抢到成功 200
            case Const.SUCCESS:
            default:
                userRedPacket = new UserRedPacket(redPacketId, userId, split[1], time, Const.SUCCESS);
                break;
        }

        return userRedPacket;
    }

    /**
     * 将redis的已抢红包对应信息持久化到mysql
     *
     * @param redPacketId
     */
    @Override
    public void persistToSql(long redPacketId, int userid) {
        Jedis jedis = jedisPool.getResource();
        jedis.del(Const.LREDPACKETKEY + redPacketId);
        Map<String, String> map = jedis.hgetAll(Const.HREDPACKEKEY + redPacketId);
        //删除hash红包记录队列
        jedis.del(Const.HREDPACKEKEY + redPacketId);
        jedis.close();
        int redpacketsize = Integer.parseInt(map.remove("redpacketsize"));
        int money = (int) (Double.valueOf(map.remove("money")) * 100);
        //如果未抢完则进行退还红包
        if (redpacketsize > map.size() - 1) {
            double surplus;
            //计算已抢的数目
            int sum = map.entrySet().stream()
                    .map(o -> (int)(Double.parseDouble(o.getValue().split("-")[0])*100))
                    .mapToInt(Integer::intValue).sum();
            surplus = (double) (money - sum) / 100;
            //退还剩余红包钱给用户
            redPacketBySqlService.addMoneyToUser(userid, surplus);
        }
        redPacketBySqlService.insertRedPacketDetail(map, redPacketId);
    }


}
