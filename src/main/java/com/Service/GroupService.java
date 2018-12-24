package com.Service;

import com.Entity.User;

import java.util.List;

public interface GroupService {
    int deleteOne(int id);
    int addOne(int id);
    List<Integer> initGroup();
}
