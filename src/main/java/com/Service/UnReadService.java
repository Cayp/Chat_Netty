package com.Service;


import com.Entity.Noreadme;
import com.Entity.UnReadGroup;


import java.util.Date;
import java.util.List;

/**
 * @author ljp
 */
public interface UnReadService {

    List<Noreadme> getNoRead(int toid);
    List<Integer> getUnReadAcc(int toid);
    int setUnRead(int toid, int fromid,int time,int type, String text);
    int deleteUnRead(int toid);
    List<UnReadGroup> getGroupUnRead(int userid,int groupid);
}
