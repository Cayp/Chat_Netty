package com.Service;

import com.Entity.GroupChat;
import java.util.List;

public interface GroupService {
    int deleteOne(int groupid, long deleteid);
    int addOne(int groupid,long addid);
    List<GroupChat> getGroupList(long userId);
}
