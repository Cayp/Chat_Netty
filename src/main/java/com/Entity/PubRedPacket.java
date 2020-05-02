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
    public long redPacketId;
    //发红包用户Id
    public long userId;

    private String userName;

    private String avatar;

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

    public PubRedPacket(long userId, int redPacket_type, int redPacket_size, double total_money, ArrayList<String> redPacket_part, long publish_time) {

        this.userId = userId;
        this.redPacket_type = redPacket_type;
        this.redPacket_size = redPacket_size;
        this.total_money = total_money;
        this.redPacket_part = redPacket_part;
        this.publish_time = publish_time;
    }

    public PubRedPacket() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(long redPacketId) {
        this.redPacketId = redPacketId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getRedPacket_type() {
        return redPacket_type;
    }

    public void setRedPacket_type(int redPacket_type) {
        this.redPacket_type = redPacket_type;
    }

    public int getRedPacket_size() {
        return redPacket_size;
    }

    public void setRedPacket_size(int redPacket_size) {
        this.redPacket_size = redPacket_size;
    }

    public double getTotal_money() {
        return total_money;
    }

    public void setTotal_money(double total_money) {
        this.total_money = total_money;
    }

    public ArrayList<String> getRedPacket_part() {
        return redPacket_part;
    }

    public void setRedPacket_part(ArrayList<String> redPacket_part) {
        this.redPacket_part = redPacket_part;
    }

    public long getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(long publish_time) {
        this.publish_time = publish_time;
    }

    @Override
    public String toString() {
        return "PubRedPacket{" +
                "redPacketId=" + redPacketId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", redPacket_type=" + redPacket_type +
                ", redPacket_size=" + redPacket_size +
                ", total_money=" + total_money +
                ", redPacket_part=" + redPacket_part +
                ", publish_time=" + publish_time +
                '}';
    }
}
