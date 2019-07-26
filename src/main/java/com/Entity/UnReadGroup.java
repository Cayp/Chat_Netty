package com.Entity;

/**
 * 群聊未读信息实体类
 */
public class UnReadGroup {
    private int groupid;
    private int time;
    private long sendid;
    private String message;
    private int type;

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public long getSendid() {
        return sendid;
    }

    public void setSendid(long sendid) {
        this.sendid = sendid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
