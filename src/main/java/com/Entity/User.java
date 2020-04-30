package com.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private long account;
    private String name;
    private String password;
    private String mail;
    private String icon;
    private long lastime;

}
