package com.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Noreadme {

  private long recvid;
  private long sendid;
  private String sendName;
  private String sendAvatar;
  private int type;
  private String message;
  private long time;
}
