package com.Service.ServiceImp;

import com.Dao.RedPacketBySqlDao;
import com.Entity.PubRedPacket;
import com.Entity.UserRedPacket;
import com.Service.RedPacketBySqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public int insertRedPacketDetail(List<UserRedPacket> partlist) {
        return redPacketBySqlDao.insertRedPacketDetail(partlist);
    }

    @Override
    public int addRedPacketMessage(PubRedPacket redPacket) {
        return redPacketBySqlDao.addRedPacketMessage(redPacket);
    }
}
