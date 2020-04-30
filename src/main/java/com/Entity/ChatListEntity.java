package com.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatListEntity {
    private String type;
    private long id;
    private long userId;
    private String name;
    private String avatar;
    private long time;
    private String content;
}
