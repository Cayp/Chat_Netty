package com.Service.ServiceImp;


import com.Dao.GetGroupDao;
import com.Entity.GroupChat;
import com.Service.GroupService;
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
    public int deleteOne(int groupid, long deleteid) {
        return getGroupDao.deleteOneFromGroup(deleteid, groupid);
    }

    @Override
    public int addOne(int groupid, long addid) {
        return getGroupDao.addOneToGroup(addid, groupid);
    }


    @Override
    public List<GroupChat> getGroupList(long userId) {
        return getGroupDao.getGroupList(userId);
    }
}
