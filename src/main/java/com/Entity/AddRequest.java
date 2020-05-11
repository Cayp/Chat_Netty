package com.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddRequest {
    private long id;
    private long userId;
    private String userName;
    private String userAvatar;
    private long toId;
    private long time;
}
