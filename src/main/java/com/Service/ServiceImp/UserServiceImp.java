package com.Service.ServiceImp;

import com.Dao.GetUserInfoDao;
import com.Entity.Friend;
import com.Entity.RegisterEntity;
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

    @Override
    public int addFriend(int userId, int toid) {
        getUserInfoDao.addFriend(userId, toid);
        getUserInfoDao.addFriend(toid, userId);
        return 1;
    }

    @Override
    public int deleteFriend(int userId, int toid) {
        getUserInfoDao.deleteFriend(userId, toid);
        getUserInfoDao.deleteFriend(toid, userId);
        return 1;
    }

    @Override
    public Friend checkFriend(int userId, int toid) {
        return getUserInfoDao.checkFriend(userId, toid);
    }

    @Override
    public boolean register(RegisterEntity registerEntity) {
        int register = getUserInfoDao.register(registerEntity);
        if (register > 0) {
            return true;
        }
        return false;
    }
}

