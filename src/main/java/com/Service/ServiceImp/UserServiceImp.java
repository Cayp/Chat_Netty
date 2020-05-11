package com.Service.ServiceImp;

import com.Dao.GetGroupDao;
import com.Dao.GetUserInfoDao;
import com.Entity.AddRequest;
import com.Entity.Friend;
import com.Entity.RegisterEntity;
import com.Entity.User;
import com.Service.GroupService;
import com.Service.UserService;
import com.Utils.Const;
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

    @Resource
    private GetGroupDao getGroupDao;

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
    @Transactional(rollbackFor = {Exception.class})
    public int addFriend(long userId, long toid) {
        return getUserInfoDao.addFriend(toid, userId) + getUserInfoDao.addFriend(userId, toid);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
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
            getGroupDao.addOneToGroup(registerEntity.getAccount(), 1);
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

    @Override
    public int updateAvatar(long userId, String avatar) {
        return getUserInfoDao.updateAvatar(userId, avatar);
    }

    @Override
    public List<User> getUsers() {
        return getUserInfoDao.getUsers();
    }

    @Override
    public List<AddRequest> getAddRequest(long toId) {
        return getUserInfoDao.getAddRequest(toId);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean operateRequest(long requestId, int type) {
        AddRequest request = getUserInfoDao.getAddRequestById(requestId);
        getUserInfoDao.deleteAddRequest(request.getId());
        if (type == Const.REQUEST_AGREE) {
            getUserInfoDao.addFriend(request.getToId(), request.getUserId());
            getUserInfoDao.addFriend(request.getUserId(), request.getToId());
        }
        return true;
    }

    @Override
    public AddRequest findRequest(long userId, long toId) {
        return getUserInfoDao.findRequest(userId, toId);
    }

    @Override
    public int addRequest(AddRequest addRequest) {
        return getUserInfoDao.insertAddRequest(addRequest);
    }

    @Override
    public List<User> searchUsers(String searchString, long userId) {
        return getUserInfoDao.searchUsers(searchString, userId);
    }
}

