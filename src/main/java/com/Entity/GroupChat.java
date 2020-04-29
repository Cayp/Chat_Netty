package com.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupChat {

  private int groupId;
  private long userId;
  private String groupName;
  private int peopleNum;
  private String picture;
}
