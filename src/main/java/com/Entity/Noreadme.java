package com.Entity;


import java.sql.Date;

public class Noreadme {

  private long recvid;
  private long sendid;
  private int type;
  private String message;
  private long time;

    public long getRecvid() {
        return recvid;
    }

    public void setRecvid(long recvid) {
        this.recvid = recvid;
    }

    public long getSendid() {
        return sendid;
    }

    public void setSendid(long sendid) {
        this.sendid = sendid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
