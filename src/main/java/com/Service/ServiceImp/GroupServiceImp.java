package com.Service.ServiceImp;


import com.Dao.GetGroupDao;
import com.Entity.Groupchat;
import com.Entity.User;
import com.Service.GroupService;
import com.Utils.Const;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ljp
 */
@Service
public class GroupServiceImp implements GroupService {

    @Resource
    private
    GetGroupDao getGroupDao;

    @Override
    public int deleteOne(int groupid, int deleteid) {
        return getGroupDao.deleteOneFromGroup(deleteid, groupid);
    }

    @Override
    public int addOne(int groupid, int addid) {
        return getGroupDao.addOneToGroup(addid, groupid);
    }

    @Override
    public int deleteGroup(int groupid, int ownerid) {
        int groupOwner = getGroupDao.getGroupOwner(groupid);
        if (groupOwner == ownerid) {
            return getGroupDao.deleteGroup(groupid);
        } else {
            return Const.NORIGHT;
        }
    }

    @Override
    public int changeOwner(int groupid, int userid) {
        int groupOwner = getGroupDao.getGroupOwner(groupid);
        if (groupOwner == userid) {
            return getGroupDao.changeOwner(groupid, userid);
        } else {
            return Const.NORIGHT;
        }
    }

    @Override
    public Groupchat buildGroup(int userid, String groupname) {
        Groupchat groupchat = new Groupchat(userid, groupname);
        groupchat.setPeoplenum(1);
        groupchat.setPicture(Const.DEFAULT_PICTURE);
        int i = getGroupDao.buildGroup(groupchat);
        if(i>0){
            return groupchat;
        }
        return null;
    }
}
