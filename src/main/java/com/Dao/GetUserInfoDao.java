package com.Dao;

import com.Entity.Friend;
import com.Entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author ljp
 */

@Mapper
public interface GetUserInfoDao {

    /**获取用户信息
     * @param account
     * @return
     */
     User getUserInfo(@Param("account") int account);
     List<User> getUserByIndex(@Param("index") Object index);
     List<User> getFriends(@Param("account") int account);
     int addFriend(@Param("toid") int toid, @Param("friendid") int friendid);
     int deleteFriend(@Param("toid") int toid, @Param("friendid") int friendid);
     Friend checkFriend(@Param("userid") int userid, @Param("toid") int toid);
}
