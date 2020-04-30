package com.Service;

import com.Entity.Friend;
import com.Entity.RegisterEntity;
import com.Entity.User;

import java.util.List;

/**
 * @author ljp
 */
public interface UserService {
    User login(String mail);
    User findUser(long id);
    int logout(long id,int time);
    List<User> getUserByIndex(String index);
    List<User> getUserFriends(long account);
    List<User> getUsers();
    int addFriend(long userId,long toid);
    int deleteFriend(long userId,long toid);
    Friend checkFriend(long userId,long toid);
    boolean register(RegisterEntity registerEntity);
    int changePassword(String mail,String password);
    int changePasswordByid(long userid,String password);
    int updateAvatar(long userId, String avatar);
}
