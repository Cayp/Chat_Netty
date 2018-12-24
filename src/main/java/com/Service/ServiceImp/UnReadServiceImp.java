package com.Service.ServiceImp;

import com.Dao.GetNoReadDao;
import com.Entity.Noreadme;
import com.Service.UnReadService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

/**
 * @author ljp
 */
@Service
public class UnReadServiceImp implements UnReadService {

    @Resource
    GetNoReadDao getNoReadDao;

    @Override
    public List<Noreadme> getNoRead(int toid) {
        return getNoReadDao.getNoRead(toid);
    }

    @Override
    public List<Integer> getUnReadAcc(int toid) {
        return getNoReadDao.getUnReadAcc(toid);
    }

    @Override
    public int setUnRead(int toid, int fromid, String text, Date date) {
        return getNoReadDao.setUnRead(toid,fromid,text,date);
    }

    @Override
    public int deleteUnRead(int toid) {
        return getNoReadDao.deleteUnRead(toid);
    }
}
