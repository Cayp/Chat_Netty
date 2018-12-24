package com.Entity;


import java.sql.Date;

public class Noreadme {

  private int toid;
  private int fromid;
  private String message;
 private Date time;

  public int getToid() {
    return toid;
  }

  public void setToid(int toid) {
    this.toid = toid;
  }


  public long getFromid() {
    return fromid;
  }

  public void setFromid(int fromid) {
    this.fromid = fromid;
  }


  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
