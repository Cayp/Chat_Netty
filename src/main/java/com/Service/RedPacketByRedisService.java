package com.Service;

import com.Entity.PubRedPacket;
import com.Entity.UserRedPacket;

public interface RedPacketByRedisService {
    PubRedPacket publishRedPacket(long userId, double money, int redPacketType, int size, long time);

    UserRedPacket getRedPacket(long redPacketId, long userId);

    void persistToSql(long redPacketId,long userid);

}
