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
    public long redpacketId;
    //用户id
    public long userId;
    //抢到的金额
    public String money;
    //抢红包的时间
    public long time;

    public String userName;

    public String userAvatar;

    private String status;

    public UserRedPacket(long redpacketId, long userId, String money, long time, String status) {
        this.redpacketId = redpacketId;
        this.userId = userId;
        this.money = money;
        this.time = time;
        this.status = status;
    }

    public UserRedPacket() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getRedpacketId() {
        return redpacketId;
    }

    public void setRedpacketId(long redpacketId) {
        this.redpacketId = redpacketId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }
}
