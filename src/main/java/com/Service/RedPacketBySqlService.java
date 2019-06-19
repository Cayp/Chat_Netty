package com.Service;

import com.Entity.PubRedPacket;
import com.Entity.UserRedPacket;

import java.util.List;
import java.util.Map;

/**
 * @author ljp
 */
public interface RedPacketBySqlService {

    //从发红包人账户减去金额
    int deductRedPacketMoney( int userId,  double money);

    //增加抢到红包人的账户
    int addMoneyToUser( int userId,  double money);

    //将红包细节持久化到Mysql
    int insertRedPacketDetail(Map<String,String> userRedPacketMap,long redPacketId);

    //将红包信息持久化到Mysql
    int addRedPacketMessage( PubRedPacket redPacket);





}
