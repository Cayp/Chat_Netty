package com.Dao;


import com.Entity.Groupchat;
import com.Entity.UnReadGroup;
import com.Entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author ljp
 */

@Mapper
public interface GetGroupDao {
    List<Integer> getGroup(String userid);
    List<Integer> getAllGroup();
    int addOneToGroup(@Param("userid") int userid,@Param("groupid") int groupid);
    int deleteOneFromGroup(@Param("userid") int userid,@Param("groupid") int groupid);

    /**
     * 群聊未读信息
     * 取出根据用户上次退出的时间到现在的所有群未读信息
     * @param userid
     * @param lastime
     * @return
     */
    List<UnReadGroup> getUnReadFromGroup(@Param("userid") int userid,@Param("lastime") int lastime);

    /**
     * 保存群聊信息进入数据库
     * @param groupid
     * @param time
     * @param sendid
     * @param message
     * @param type
     * @return
     */
    int addChatRecGroup(@Param("groupid") int groupid,@Param("time") int time,@Param("sendid") int sendid,@Param("message") String message,@Param("type") int type);

    /**
     * 转移群主
     * @param groupid
     * @param changeUserid
     * @return
     */
    int changeOwner(@Param("groupid") int groupid,@Param("changeUserid") int changeUserid);

    /**
     * 删除群
     * @param groupid
     * @return
     */
    int deleteGroup(@Param("groupid") int groupid);

    /**
     * 返回该群聊的群主
     * @param groupid
     * @return
     */
    int getGroupOwner(@Param("groupid") int groupid);

    /**
     * 创建一个群聊
     * @return
     */
    int buildGroup(Groupchat groupchat);

}
