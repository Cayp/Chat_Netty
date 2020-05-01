package com.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群聊未读信息实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnReadGroup {
    private int groupId;
    private long time;
    private long sendId;
    private String message;
    private int type;
    private String sendName;
    private String sendAvatar;
}
