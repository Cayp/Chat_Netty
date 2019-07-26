package com.Entity;

import java.io.Serializable;

/**
 * 用户抢到的红包实体类
 *
 * @author ljp
 */
public class UserRedPacket implements Serializable {

    private static final long serialVersionUID = -6549635816464408763L;
    //抢到该红包id
    public long redPacketId;
    //用户id
    public long userId;
    //抢到的金额
    public String money;
    //抢红包的时间
    public long time;

    private String status;

    public UserRedPacket(long redPacketId, long userId, String money, long time, String status) {
        this.redPacketId = redPacketId;
        this.userId = userId;
        this.money = money;
        this.time = time;
        this.status = status;
    }

    public long getRedPacketId() {
        return redPacketId;
    }

    public long getUserId() {
        return userId;
    }

    public String getMoney() {
        return money;
    }

    public long getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
}
