package com.Dao;


import com.Entity.GroupChat;
import com.Entity.UnReadGroup;
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
    int addOneToGroup(@Param("userid") long userid,@Param("groupid") int groupid);
    int deleteOneFromGroup(@Param("userid") long userid,@Param("groupid") int groupid);

    /**
     * 群聊未读信息
     * 取出根据用户上次退出的时间到现在的所有群未读信息
     * @param userid
     * @param lastime
     * @return
     */
    List<UnReadGroup> getUnReadFromGroup(@Param("userid") long userid,@Param("lastime") int lastime);

    /**
     * 保存群聊信息进入数据库
     * @param groupid
     * @param time
     * @param sendid
     * @param message
     * @param type
     * @return
     */
    int addChatRecGroup(@Param("groupid") int groupid,@Param("time")long time,@Param("sendid") long sendid,@Param("message") String message,@Param("type") int type, @Param("name") String name, @Param("avator") String avator);



    List<GroupChat> getGroupList(long userId);

}
