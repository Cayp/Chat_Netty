package com.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog {
  private int id;
  private int type;
  private int userId;
  private String content;
  private String userName;
  private String userAvatar;
  private long createTime;
  private int targetUserId;
  private String targetUserName;
  private int blogId;
  private List<Blog> comments;
  private int total;

}
