package com.Dao;

import com.Entity.Noreadme;
import com.Entity.UnReadGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.Date;
import java.util.List;


@Mapper
public interface GetNoReadDao {


    List<Noreadme> getNoRead(int recvid);
    List<Integer> getUnReadAcc(int recvid);
    int setUnRead(@Param("toid") int toid, @Param("fromid") int fromid,@Param("tpye")int type,@Param("text") String text, @Param("time") long time);
    int deleteUnRead(int toid);
    List<UnReadGroup> getUnReadGroup(int lastime,int groupid);
}
