package com.Service;


import com.Entity.Noreadme;


import java.util.Date;
import java.util.List;

/**
 * @author ljp
 */
public interface UnReadService {

    List<Noreadme> getNoRead(int toid);
    List<Integer> getUnReadAcc(int toid);
    int setUnRead(int toid, int fromid, String text, int time);
    int deleteUnRead(int toid);
}
