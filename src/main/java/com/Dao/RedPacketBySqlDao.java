package com.Dao;

import com.Entity.PubRedPacket;
import com.Entity.RedPacketLog;
import com.Entity.UserRedPacket;
import com.Entity.Wallet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ljp
 */
@Mapper
public  interface RedPacketBySqlDao {
    //从发布红包人减去金额
    int deductRedPacketMoney(@Param("userId") long userId, @Param("money") double money);

    //增加抢到红包人的账户
    int addMoneyToUser(@Param("userId") long userId, @Param("money") double money);

    //将红包细节持久化到Mysql
    int insertRedPacketDetail(@Param("list") List<UserRedPacket> partlist,double moneysum,long redpacketid);

    //将红包信息持久化到Mysql
    int addRedPacketMessage(@Param("redPacket") PubRedPacket redPacket);

    int addRedPacketDetail(@Param("redPacket") UserRedPacket userRedPacket);

    List<PubRedPacket> getRedPackets();

    List<RedPacketLog> getRedPacketLogByUserId(@Param("userId") long userId);

    int addRedPacketLog(@Param("log") RedPacketLog redPacketLog);

    List<UserRedPacket> getRedPacketGraps(@Param("redpacketId") long redpacketId);

    Wallet getWallet(long userId);
}
