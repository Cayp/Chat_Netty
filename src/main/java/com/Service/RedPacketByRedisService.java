package com.Service;

import com.Entity.PubRedPacket;
import com.Entity.UserRedPacket;

public interface RedPacketByRedisService {
    PubRedPacket publishRedPacket(int userId, double money, int redPacketType, int groupId, int size);

    UserRedPacket getRedPacket(long redPacketId, int userId);

    void persistToSql(long redPacketId);

}
