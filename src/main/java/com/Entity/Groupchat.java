package com.Entity;


public class Groupchat {

  private  int groupid;
  private long userId;
  private String groupname;
  private int peoplenum;
  private String picture;

  public Groupchat(long userId, String groupname) {
    this.userId = userId;
    this.groupname = groupname;
  }

  public int getGroupid() {
    return groupid;
  }

  public void setGroupid(int groupid) {
    this.groupid = groupid;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public String getGroupname() {
    return groupname;
  }

  public void setGroupname(String groupname) {
    this.groupname = groupname;
  }

  public int getPeoplenum() {
    return peoplenum;
  }

  public void setPeoplenum(int peoplenum) {
    this.peoplenum = peoplenum;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }
}
