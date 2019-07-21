package com.Entity;


public class Groupchat {

  private  int groupid;
  private int userId;
  private String groupname;
  private int peoplenum;
  private String picture;

  public Groupchat(int userId, String groupname) {
    this.userId = userId;
    this.groupname = groupname;
  }

  public int getGroupid() {
    return groupid;
  }

  public void setGroupid(int groupid) {
    this.groupid = groupid;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
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
