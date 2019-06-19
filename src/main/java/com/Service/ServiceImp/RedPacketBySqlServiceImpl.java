package com.Service.ServiceImp;

import com.Dao.RedPacketBySqlDao;
import com.Entity.PubRedPacket;
import com.Entity.UserRedPacket;
import com.Service.RedPacketBySqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

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
    public int deductRedPacketMoney(int userId, double money) {
        return redPacketBySqlDao.deductRedPacketMoney(userId, money);
    }

    @Override
    public int addMoneyToUser(int userId, double money) {
        return redPacketBySqlDao.addMoneyToUser(userId, money);
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
}
