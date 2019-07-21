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
    public List<Noreadme> getNoRead(int toid) {
        return getNoReadDao.getNoRead(toid);
    }

    @Override
    public List<Integer> getUnReadAcc(int toid) {
        return getNoReadDao.getUnReadAcc(toid);
    }

    @Override
    public int setUnRead(int toid, int fromid, int time, int type, String text) {
        return getNoReadDao.setUnRead(toid, fromid, type, text, time);
    }

    @Override
    public List<UnReadGroup> getGroupUnRead(int userid, int groupid) {
        User userInfo = getUserInfoDao.getUserInfo(userid);
        return getNoReadDao.getUnReadGroup(userInfo.getLastime(), groupid);
    }

    @Override
    public int deleteUnRead(int toid) {
        return getNoReadDao.deleteUnRead(toid);
    }
}
