package com.Service.ServiceImp;

import com.Dao.TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class TestServiceImpl {

    @Resource
    TestDao testDao;


    public void insertTest(String url) {
        testDao.insert3(url);
    }

}
