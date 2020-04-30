package com.Service;


import com.Entity.Noreadme;
import com.Entity.UnReadGroup;


import java.util.Date;
import java.util.List;

/**
 * @author ljp
 */
public interface UnReadService {

    List<Noreadme> getNoRead(long toid);
    List<Integer> getUnReadAcc(long toid);
    int setUnRead(long toid, long fromid,long time,int type, String text, String name, String avator);
    int deleteUnRead(long toid);
    List<UnReadGroup> getGroupUnRead();
}
