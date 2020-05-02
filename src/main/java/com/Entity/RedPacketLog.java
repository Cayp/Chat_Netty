package com.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedPacketLog {
    private int id;
    private long userId;
    private int type;
    private String money;
    private long time;

    public RedPacketLog(long userId, int type, String money, long time) {
        this.userId = userId;
        this.type = type;
        this.money = money;
        this.time = time;
    }
}
