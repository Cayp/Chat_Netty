package com.Service;

import com.Entity.User;

import java.util.List;

/**
 * @author ljp
 */
public interface UserService {
    User login(int account);
    List<User> getUserByIndex(String index);
    List<User> getUserFriends(int account);
}
