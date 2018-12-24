package com.Dao;

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
     List<User> getUserByIndex(Object index);
     List<User> getFriends(int account);
}
