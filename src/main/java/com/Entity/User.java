package com.Entity;


public class User {

    private long account;
    private String name;
    private String password;
    private String mail;
    private String icon;
    private int lastime;

    public long getAccount() {
        return account;
    }


    public void setAccount(long account) {
        this.account = account;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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
