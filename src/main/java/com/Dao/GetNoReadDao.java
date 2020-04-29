package com.Dao;

import com.Entity.Noreadme;
import com.Entity.UnReadGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.Date;
import java.util.List;


@Mapper
public interface GetNoReadDao {


    List<Noreadme> getNoRead(long recvid);
    List<Integer> getUnReadAcc(long  recvid);
    int setUnRead(@Param("toid") long toid, @Param("fromid") long fromid,@Param("type")int type,@Param("text") String text, @Param("time") long time, @Param("name")String name, @Param("avator")String avator);
    int deleteUnRead(long toid);
    List<UnReadGroup> getUnReadGroup();
}
