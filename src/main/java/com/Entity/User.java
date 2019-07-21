package com.Entity;


public class User {

  private int account;
  private String name;
  private String password;
  private String icon;
  private int lastime;

  public int getAccount() {
    return account;
  }

  public void setAccount(int accout) {
    this.account = accout;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public int getLastime() {
    return lastime;
  }

  public void setLastime(int lastime) {
    this.lastime = lastime;
  }
}
