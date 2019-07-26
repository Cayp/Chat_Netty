package com.Dao;

import com.Entity.Friend;
import com.Entity.RegisterEntity;
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
     User getUserInfo(@Param("account") long account);
     List<User> getUserByIndex(@Param("index") Object index);
     List<User> getFriends(@Param("account") long account);
     int addFriend(@Param("toid") long toid, @Param("friendid") long friendid);
     int deleteFriend(@Param("toid") long toid, @Param("friendid") long friendid);
     Friend checkFriend(@Param("userid") long userid, @Param("toid") long toid);
     int register(RegisterEntity registerEntity);
}
