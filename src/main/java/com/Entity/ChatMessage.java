package com.Entity;

public class ChatMessage {
    private String account;
    private String textone;

    public ChatMessage(String account, String textone) {
        this.account = account;
        this.textone = textone;
    }

    public String getTextone() {
        return textone;
    }

    public void setTextone(String textone) {
        this.textone = textone;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }


}
