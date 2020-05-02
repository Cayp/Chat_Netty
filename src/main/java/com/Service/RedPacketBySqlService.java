package com.Service;

import com.Entity.PubRedPacket;
import com.Entity.RedPacketLog;
import com.Entity.UserRedPacket;
import com.Entity.Wallet;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author ljp
 */
public interface RedPacketBySqlService {

    //从发红包人账户减去金额
    int deductRedPacketMoney( long userId,  double money, long time);

    int backMoneyToUser( long userId,  double money);

    int addRedPacketDetail(UserRedPacket userRedPacket);

    //将红包细节持久化到Mysql
    int insertRedPacketDetail(Map<String,String> userRedPacketMap,long redPacketId);

    //将红包信息持久化到Mysql
    int addRedPacketMessage( PubRedPacket redPacket);

    List<PubRedPacket> getRedPackets();

    List<RedPacketLog> getRedPacketLogByUserId(long userId);

    int addRedPacketLog(RedPacketLog redPacketLog);

    List<UserRedPacket> getRedPacketGraps(long redpacketId);

    Wallet getWallet(long userId);

}
