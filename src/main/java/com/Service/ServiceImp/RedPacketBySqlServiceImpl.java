package com.Service.ServiceImp;

import com.Dao.RedPacketBySqlDao;
import com.Entity.PubRedPacket;
import com.Entity.RedPacketLog;
import com.Entity.UserRedPacket;
import com.Entity.Wallet;
import com.Service.RedPacketBySqlService;
import com.Utils.Const;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 红包相关信息操作Service类
 *
 * @author ljp
 */
@Service
public class RedPacketBySqlServiceImpl implements RedPacketBySqlService {

    @Resource
    RedPacketBySqlDao redPacketBySqlDao;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int deductRedPacketMoney(long userId, double money, long time) {
        redPacketBySqlDao.addRedPacketLog(new RedPacketLog(userId, Const.PUBLISH, String.valueOf(money), time));
        return redPacketBySqlDao.deductRedPacketMoney(userId, money);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int backMoneyToUser(long userId, double money) {
        int res = redPacketBySqlDao.addMoneyToUser(userId, money);
        int res1 = redPacketBySqlDao.addRedPacketLog(new RedPacketLog(userId, Const.REBACK, String.valueOf(money), System.currentTimeMillis() / 1000));
        return res + res1;
    }


    //异步插入Mysql
    @Async
    @Override
    public int insertRedPacketDetail(Map<String, String> userRedPacketMap, long redPacketId) {
        ArrayList<UserRedPacket> partlist = new ArrayList<>();
        Set<Map.Entry<String, String>> entries = userRedPacketMap.entrySet();
        //记录已抢红包的金额总数
        double moneySum = 0.0;
        for (Map.Entry<String, String> entry : entries) {
            String value = entry.getValue();
            String[] split = value.split("-");
            String userid = entry.getKey();
            String money = split[0];
            String time = split[1];
            moneySum += Double.valueOf(money);
            UserRedPacket userRedPacket = new UserRedPacket(redPacketId, Integer.parseInt(userid), money, Long.valueOf(time), "1");
            partlist.add(userRedPacket);
        }
        return redPacketBySqlDao.insertRedPacketDetail(partlist,moneySum,redPacketId);
    }

    @Override
    public int addRedPacketMessage(PubRedPacket redPacket) {
        return redPacketBySqlDao.addRedPacketMessage(redPacket);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int addRedPacketDetail(UserRedPacket userRedPacket) {
        redPacketBySqlDao.addMoneyToUser(userRedPacket.userId, Double.valueOf(userRedPacket.money));
        int res = redPacketBySqlDao.addRedPacketDetail(userRedPacket);
        int res1 = redPacketBySqlDao.addRedPacketLog(new RedPacketLog(userRedPacket.userId, Const.GRAP, userRedPacket.money, userRedPacket.time));
        return res + res1;
    }

    @Override
    public List<PubRedPacket> getRedPackets() {
        return redPacketBySqlDao.getRedPackets();
    }

    @Override
    public List<RedPacketLog> getRedPacketLogByUserId(long userId) {
        return redPacketBySqlDao.getRedPacketLogByUserId(userId);
    }

    @Override
    public int addRedPacketLog(RedPacketLog redPacketLog) {
        return redPacketBySqlDao.addRedPacketLog(redPacketLog);
    }

    @Override
    public List<UserRedPacket> getRedPacketGraps(long redpacketId) {
        return redPacketBySqlDao.getRedPacketGraps(redpacketId);
    }

    @Override
    public Wallet getWallet(long userId) {
        return redPacketBySqlDao.getWallet(userId);
    }
}
