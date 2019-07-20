package com.Service;

import com.Entity.User;

import java.util.List;

public interface GroupService {
    int deleteOne(int groupid, int deleteid);
    int addOne(int groupid,int addid);
    int deleteGroup(int groupid,int ownerid);
    int changeOwner(int groupid,int userid);
}
