package com.Dao;


import com.Entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author ljp
 */

@Mapper
public interface GetGroupDao {
    List<Integer> getUsers();
    int deleteOne(int id);
    int addOne(int id);
}
