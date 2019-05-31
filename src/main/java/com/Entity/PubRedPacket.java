package com.Entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * 发布红包时的实体类
 *
 * @author ljp
 */
public class PubRedPacket implements Serializable {
    private static final long serialVersionUID = -7039240798592909745L;
    //红包id
    public long redPackerId;
    //发红包用户Id
    public int userId;
    //红包类型, average-均值 unaverage-拼手气
    public int redPacket_type;
    //红包个数 最大值MaxSize为100
    public int redPacket_size;
    //红包金额
    public double total_money;
    //每个红包对应的金额数
    public ArrayList<String> redPacket_part;
    //发布红包时的时间戳
    public long publish_time;

    public PubRedPacket(long redPackerId, int userId, int redPacket_type, int redPacket_size, double total_money, ArrayList<String> redPacket_part, long publish_time) {
        this.redPackerId = redPackerId;
        this.userId = userId;
        this.redPacket_type = redPacket_type;
        this.redPacket_size = redPacket_size;
        this.total_money = total_money;
        this.redPacket_part = redPacket_part;
        this.publish_time = publish_time;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getRedPackerId() {
        return redPackerId;
    }

    public int getUserId() {
        return userId;
    }

    public int getRedPacket_type() {
        return redPacket_type;
    }

    public int getRedPacket_size() {
        return redPacket_size;
    }

    public double getTotal_money() {
        return total_money;
    }

    public ArrayList<String> getRedPacket_part() {
        return redPacket_part;
    }

    public long getPublish_time() {
        return publish_time;
    }
}
