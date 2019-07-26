package com.Entity;

import com.Utils.Const;

public class RegisterEntity {
    private long account;
    private String password;
    private String name;
    private String phone;
    private String verifycode;
    private String icon = Const.DEFAULT_PICTURE;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAccount() {
        return account;
    }

    public void setAccount(long account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifycode() {
        return verifycode;
    }

    public void setVerifycode(String verifycode) {
        this.verifycode = verifycode;
    }

    @Override
    public String toString() {
        return "RegisterEntity{" +
                "account=" + account +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", verifycode='" + verifycode + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
