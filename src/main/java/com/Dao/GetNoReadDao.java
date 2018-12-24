package com.Dao;

import com.Entity.Noreadme;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.Date;
import java.util.List;


@Mapper
public interface GetNoReadDao {

    /**获取未读信息
     * @param toid
     * @return
     */
    List<Noreadme> getNoRead(int toid);
    List<Integer> getUnReadAcc(int toid);
    int setUnRead(@Param("toid") int toid, @Param("fromid") int fromid, @Param("text") String text, @Param("time") Date time);
    int deleteUnRead(int toid);
}
