package com.Dao;


import com.Entity.UnReadGroup;
import com.Entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ljp
 */

@Mapper
public interface GetGroupDao {
    List<Integer> getGroup(String userid);
    List<Integer> getAllGroup();
    int addOneToGroup(int userid,int groupid);
    int deleteOneFromGroup(int userid,int groupid);

    /**
     * 群聊未读信息
     * 取出根据用户上次退出的时间到现在的所有群未读信息
     * @param userid
     * @param lastime
     * @return
     */
    List<UnReadGroup> getUnReadFromGroup(int userid,int lastime);

    /**
     * 保存群聊信息进入数据库
     * @param groupid
     * @param time
     * @param sendid
     * @param message
     * @param type
     * @return
     */
    int addChatRecGroup(int groupid,int time,int sendid,String message,int type);

    /**
     * 转移群主
     * @param groupid
     * @param changeUserid
     * @return
     */
    int changeOwner(int groupid,int changeUserid);

    /**
     * 删除群
     * @param groupid
     * @return
     */
    int deleteGroup(int groupid);

    int getGroupOwner(int groupid);

}
