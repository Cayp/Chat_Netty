package com.Service.ServiceImp;

import com.Entity.PubRedPacket;
import com.Entity.UserRedPacket;
import com.Service.RedPacketByRedisService;
import com.Service.RedPacketBySqlService;
import com.Utils.Const;
import com.Utils.CutPointUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

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
            //判断是否有脚本的sha记录和在redis上脚本是否还存活
            if (sha1_Pub == null || jedis.scriptExists(sha1_Pub)) {
                sha1_Pub = jedis.scriptLoad(Const.PUBLISHREDPACKET_LUA);
            }
            //redis返回的结果
            respo = (String) (jedis.evalsha(sha1_Pub, redPacketIdL, parts));
        } finally {
            jedis.close();
        }
        PubRedPacket pubRedPacket = new PubRedPacket(redPacketId, userId, redPacketType, size, money, parts, time);
        //保存红包信息进mysql
        redPacketBySqlService.addRedPacketMessage(pubRedPacket);
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
            sha1_Get  = jedis.scriptLoad(Const.GETREDPACKET_LUA);
        }
        try {
            respo = (String) jedis.evalsha(sha1_Get, 2, Long.toString(redPacketId),"", Integer.toString(userId), Long.toString(time));
        } finally {
            jedis.close();
        }
        String[] split = respo.split("-");
        UserRedPacket userRedPacket;
        //解析返回的结果,以 ‘-’作切割,前面是响应码,后面是金额,除错误响应码外:0,1
        switch (split[0]) {
            //已抢过的情况
            case Const.EXIST:
                userRedPacket = new UserRedPacket(redPacketId, userId, "0", time, Const.EXIST);
                break;
            //当抢到最后一个红包,将红包已消费的情况持久化到Mysql
            case Const.LASTONE:
                userRedPacket = new UserRedPacket(redPacketId, userId, split[1], time, Const.LASTONE);
                jedis = jedisPool.getResource();
                Map<String, String> map = jedis.hgetAll(Const.HREDPACKEKEY + redPacketId);
                jedis.close();
                redPacketBySqlService.insertRedPacketDetail(map, redPacketId);
                break;
            //没抢到
            case Const.LOOT:
                userRedPacket = new UserRedPacket(redPacketId, userId, "0", time, Const.LOOT);
                break;
            //抢到成功
            case Const.SUCCESS:
                userRedPacket = new UserRedPacket(redPacketId, userId, split[1], time, Const.SUCCESS);
                break;
            default:
                userRedPacket = new UserRedPacket(redPacketId, userId, split[1], time, Const.SUCCESS);
                break;
        }
        return userRedPacket;
    }
}
