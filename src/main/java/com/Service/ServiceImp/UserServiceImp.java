package com.Service.ServiceImp;

import com.Dao.GetUserInfoDao;
import com.Entity.Friend;
import com.Entity.RegisterEntity;
import com.Entity.User;
import com.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public User login(String mail) {
        return getUserInfoDao.getUserInfo(mail);
    }

    @Override
    public User findUser(long id) {
        return getUserInfoDao.findUser(id);
    }

    @Override
    public int logout(long id, int time) {
        return getUserInfoDao.logout(id, time);
    }

    @Override
    public List<User> getUserByIndex(String index) {
        return getUserInfoDao.getUserByIndex(index);
    }

    @Override
    public List<User> getUserFriends(long account) {
        return getUserInfoDao.getFriends(account);
    }

    @Override
    @Transactional
    public int addFriend(long userId, long toid) {
        return getUserInfoDao.addFriend(toid, userId) + getUserInfoDao.addFriend(userId, toid);
    }

    @Override
    @Transactional
    public int deleteFriend(long userId, long toid) {
        return getUserInfoDao.deleteFriend(toid, userId) + getUserInfoDao.deleteFriend(userId, toid);
    }

    @Override
    public Friend checkFriend(long userId, long toid) {
        return getUserInfoDao.checkFriend(userId, toid);
    }

    @Override
    public boolean register(RegisterEntity registerEntity) {
        User userInfo = getUserInfoDao.getUserInfo(registerEntity.getMail());
        if (userInfo != null) {
            return false;
        }
        int register = getUserInfoDao.register(registerEntity);
        if (register > 0) {
            return true;
        }
        return false;
    }

    @Override
    public int changePassword(String mail, String password) {
        return getUserInfoDao.changePassWord(mail,password );
    }

    @Override
    public int changePasswordByid(long userid, String password) {
        return getUserInfoDao.changePassWordByid(userid,password );
    }
}

