package com.Service;

import com.Entity.Friend;
import com.Entity.User;

import java.util.List;

/**
 * @author ljp
 */
public interface UserService {
    User login(int account);
    List<User> getUserByIndex(String index);
    List<User> getUserFriends(int account);
    int addFriend(int userId,int toid);
    int deleteFriend(int userId,int toid);
    Friend checkFriend(int userId,int toid);
}
