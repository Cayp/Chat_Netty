package com.Entity;

import com.Utils.Const;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterEntity {
    private long account;
    private String password;
    private String name;
    private String mail;
    private String verifycode;
    private String icon = Const.DEFAULT_PICTURE;

}
