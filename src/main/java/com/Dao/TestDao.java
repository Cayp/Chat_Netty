package com.Dao;

import org.apache.ibatis.annotations.Param;

public interface TestDao {
    void insert1(@Param("url") String url);
    int insert2(@Param("url") String url);
    int insert3(@Param("url") String url);
}
