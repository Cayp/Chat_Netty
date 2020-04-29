package com.Service.ServiceImp;

import com.Dao.GetNoReadDao;
import com.Dao.GetUserInfoDao;
import com.Entity.Noreadme;
import com.Entity.UnReadGroup;
import com.Entity.User;
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

    @Resource
    GetUserInfoDao getUserInfoDao;

    @Override
    public List<Noreadme> getNoRead(long toid) {
        return getNoReadDao.getNoRead(toid);
    }

    @Override
    public List<Integer> getUnReadAcc(long toid) {
        return getNoReadDao.getUnReadAcc(toid);
    }

    @Override
    public int setUnRead(long toid, long fromid, int time, int type, String text, String name, String avator) {
        return getNoReadDao.setUnRead(toid, fromid, type, text, time, name, avator);
    }

    @Override
    public List<UnReadGroup> getGroupUnRead() {
        return getNoReadDao.getUnReadGroup();
    }

    @Override
    public int deleteUnRead(long toid) {
        return getNoReadDao.deleteUnRead(toid);
    }
}
