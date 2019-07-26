package com.Service;

import com.Entity.Groupchat;
import com.Entity.User;

import java.util.List;

public interface GroupService {
    int deleteOne(int groupid, long deleteid);
    int addOne(int groupid,long addid);
    int deleteGroup(int groupid,long ownerid);
    int changeOwner(int groupid,long userid);
    Groupchat buildGroup(long userid,String groupname);
}
