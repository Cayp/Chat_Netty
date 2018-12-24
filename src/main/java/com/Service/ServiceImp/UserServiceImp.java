package com.Service.ServiceImp;

import com.Dao.GetUserInfoDao;
import com.Entity.User;
import com.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author ljp
 */
@Service
public class UserServiceImp implements UserService {

    @Resource
    private GetUserInfoDao getUserInfoDao;

    @Override
    public User login(int account) {
        return getUserInfoDao.getUserInfo(account);
    }

    @Override
    public List<User> getUserByIndex(String index) {
        return getUserInfoDao.getUserByIndex(index);
    }

    @Override
    public List<User> getUserFriends(int account) {
        return getUserInfoDao.getFriends(account);
    }
}
