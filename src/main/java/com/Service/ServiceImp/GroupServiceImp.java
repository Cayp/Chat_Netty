package com.Service.ServiceImp;


import com.Dao.GetGroupDao;
import com.Entity.User;
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
    public int deleteOne(int id) {
        return getGroupDao.deleteOne(id);
    }

    @Override
    public int addOne(int id) {
        return getGroupDao.addOne(id);
    }

    @Override
    public List<Integer> initGroup() {
        return getGroupDao.getUsers();
    }
}
