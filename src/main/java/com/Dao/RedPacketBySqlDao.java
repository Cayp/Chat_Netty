package com.Dao;

import com.Entity.PubRedPacket;
import com.Entity.UserRedPacket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ljp
 */
@Mapper
public  interface RedPacketBySqlDao {
    //从发布红包人减去金额
    int deductRedPacketMoney(@Param("userId") int userId, @Param("money") double money);

    //增加抢到红包人的账户
    int addMoneyToUser(@Param("userId") int userId, @Param("money") double money);

    //将红包细节持久化到Mysql
    int insertRedPacketDetail(@Param("partlist") List<UserRedPacket> partlist);

    //将红包信息持久化到Mysql
    int addRedPacketMessage(@Param("redPacket") PubRedPacket redPacket);
}
