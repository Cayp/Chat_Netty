package com.Dao;

import com.Entity.AddRequest;
import com.Entity.Friend;
import com.Entity.RegisterEntity;
import com.Entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.annotation.security.PermitAll;
import java.util.List;


/**
 * @author ljp
 */

@Mapper
public interface GetUserInfoDao {

    /**通过邮箱获取用户信息
     * @param mail
     * @return
     */
     User getUserInfo(@Param("mail") String mail);
     User findUser(@Param("id")long id);
     int logout(@Param("id")long id,@Param("lastime")int time);
     List<User> getUserByIndex(@Param("index") Object index);
     List<User> getFriends(@Param("account") long account);
     List<User> getUsers();
     int addFriend(@Param("toid") long toid, @Param("friendid") long friendid);
     int deleteFriend(@Param("toid") long toid, @Param("friendid") long friendid);
     Friend checkFriend(@Param("userid") long userid, @Param("toid") long toid);
     int register(@Param("register") RegisterEntity registerEntity);
     int changePassWord(@Param("mail") String mail,@Param("password") String password);
     int changePassWordByid(@Param("userid")long id,@Param("password")String password);
     int updateAvatar(@Param("userId") long userId, @Param("avatar") String avatar);
     List<AddRequest> getAddRequest(@Param("toId")long toId);
     int insertAddRequest(@Param("request")AddRequest request);
     int deleteAddRequest(@Param("requestId")long requestId);
     AddRequest findRequest(@Param("userId")long userId, @Param("toId")long toId);
     AddRequest getAddRequestById(@Param("id")long requestId);
     List<User> searchUsers(@Param("searchString")String searchString, @Param("userId")long userId);

}
