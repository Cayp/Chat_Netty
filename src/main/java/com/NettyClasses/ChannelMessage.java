package com.NettyClasses;

import com.Dao.GetGroupDao;
import com.Entity.PersonEntity;
import com.Entity.User;
import com.Utils.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 处理接收信息handler类
 *
 * @author ljp
 */
public class ChannelMessage {
    //存储channel的总集合
    private static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    //存userid和channel对应关系
    private static ConcurrentHashMap<String, ChannelId> channelIds = new ConcurrentHashMap<>();
    //存groupid和channelgroup对应关系
    private static ConcurrentHashMap<String, ChannelGroup> chatgroupmap = new ConcurrentHashMap<>();
    //存channelid与userid对应关系,为了解决remove
    private static ConcurrentHashMap<ChannelId, String> channeluserid = new ConcurrentHashMap<>();
    //user对象容器
    private static ConcurrentHashMap<String, User> userMap = new ConcurrentHashMap<>();

    private static Logger logger = LoggerFactory.getLogger(ChannelMessage.class);
    public final static String SINGLE_CHAT = "1";
    public final static String GROUP_CHAT = "2";
    private final static String IN = "6";
    private final static String OFF = "7";
    private final static String ONLINE = "8";
    private final static String OFFLINE = "9";


    private ChannelMessage() {
    }


    /**
     * 在总集合中和群聊集合中全删除channel
     *
     * @param channel
     * @return
     */
    public void removeChannel( Channel channel) {
        logger.info("client from {} 退出连接", channel.remoteAddress());
        String userid = channeluserid.get(channel.id());
        group.remove(channel);
        channelIds.remove(userid);
        User user = userMap.remove(userid);
        deleteUserAllGroup(user, channel);
        ;
    }

    /**
     * 从总集合中取出channel
     *
     * @param userid
     * @return
     */
    public Channel getChannel(String userid) {
        ChannelId channelId = channelIds.get(userid);
        if (channelId == null) {
            return null;
        } else {
            return group.find(channelId);
        }

    }

    /**
     * 第一次添加到channel总集合中
     *
     * @param user
     * @param channel
     */
    public void saveAccount(User user, Channel channel) {
        logger.info("client from {} 已连接", channel.remoteAddress());
        String userId = String.valueOf(user.getAccount());
        //若用户不存在,添加到集合中
        if (!channelIds.containsValue(channel.id())) {
            channelIds.put(userId, channel.id());
            channeluserid.put(channel.id(), userId);
            userMap.put(userId, user);
            group.add(channel);
            initUserToGroup(userId, channel);
        }
    }


    /**
     * 指定用户删除所选择群聊
     *
     * @param userid
     * @return
     */
    public void removeFromGroup(String userid, String groupid) {
        Channel channel = getChannel(userid);
        ChannelGroup channels = chatgroupmap.get(groupid);
        if (channel != null) {
            channels.remove(channel);
        }
        //通知群聊中的用户
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", OFF);
        jsonObject.put("id", userid);
        jsonObject.put("time", System.currentTimeMillis() / 1000);
        channels.writeAndFlush(new TextWebSocketFrame(jsonObject.toString()));
    }

    /**
     * 添加用户入群聊
     *
     * @param groupid
     * @return
     */
    public void addToGroup(String userid, String groupid) {
        ChannelId channelId = channelIds.get(userid);
        ChannelGroup group = chatgroupmap.get(groupid);
        if (channelId != null) {
            group.add(group.find(channelId));
        }
        //通知群聊中的人，该用户邀请进群聊
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", IN);
        jsonObject.put("id", userid);
        jsonObject.put("time", System.currentTimeMillis() / 1000);
        group.writeAndFlush(new TextWebSocketFrame(jsonObject.toString()));
    }

    /**
     * 群聊通知上线
     * @param userId
     * @param groupId
     */
    public void onlineToGroup(String userId, String groupId) {
        User user = userMap.get(userId);
        if (user != null) {
            ChannelGroup chatgroup = getChatgroup(groupId);
            Collection<User> onLineUsers = getOnLineUsers();
            List<PersonEntity> onlineList = onLineUsers.stream().map(user1 -> new PersonEntity(user1.getAccount(), user1.getName(), user1.getIcon())).collect(Collectors.toList());
            String onlineListStr = JSONObject.toJSONString(onlineList);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", IN);
            jsonObject.put("msg", user.getName() + "已上线");
            jsonObject.put("onlineList", onlineListStr);
            chatgroup.writeAndFlush(new TextWebSocketFrame(jsonObject.toString()));
        }
    }

    /**
     * 群聊通知下线
     * @param user
     * @param groupId
     */
    public void offlineToGroup(User user, String groupId) {
        if (user != null) {
            ChannelGroup chatgroup = getChatgroup(groupId);
            Collection<User> onLineUsers = getOnLineUsers();
            List<PersonEntity> onlineList = onLineUsers.stream().map(user1 -> new PersonEntity(user1.getAccount(), user1.getName(), user1.getIcon())).collect(Collectors.toList());
            String onlineListStr = JSONObject.toJSONString(onlineList);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", OFFLINE);
            jsonObject.put("msg", user.getName() + "已下线");
            jsonObject.put("onlineList", onlineListStr);
            chatgroup.writeAndFlush(new TextWebSocketFrame(jsonObject.toString()));
        }
    }

    /**
     * 初始化群聊的channelgroup
     *
     * @param groupids
     */
    public void initGroup(List<Integer> groupids) {
        for (Integer integer : groupids) {
            String groupid = String.valueOf(integer);
            ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
            chatgroupmap.put(groupid, group);
        }
    }

    /**
     * 返回群聊对应的channelgroup
     *
     * @param groupid
     * @return
     */
    public ChannelGroup getChatgroup(String groupid) {
        return chatgroupmap.get(groupid);
    }

    /**
     * 第一次登录用户初始化加入他所属的群聊Group
     *
     * @param userId
     */
    private void initUserToGroup(String userId, Channel channel) {
        GetGroupDao bean = SpringUtil.getBean(GetGroupDao.class);
        List<Integer> groups = bean.getGroup(userId);
        for (int groupid : groups) {
            String groupidStr = String.valueOf(groupid);
            chatgroupmap.get(groupidStr).add(channel);
            onlineToGroup(userId, groupidStr);
        }

    }

    /**
     * 选定用户退出所有群的channel集合
     *
     * @param user
     */
    private void deleteUserAllGroup(User user, Channel channel) {
        GetGroupDao bean = SpringUtil.getBean(GetGroupDao.class);
        List<Integer> groups = bean.getGroup(String.valueOf(user.getAccount()));
        for (int groupid : groups) {
            String groupidStr = String.valueOf(groupid);
            chatgroupmap.get(groupidStr).remove(channel);
            offlineToGroup(user, groupidStr);
        }
    }

    /**
     * 添加新的群聊进群聊集合中
     *
     * @param groupid
     */
    public void addGroup(String groupid) {
        ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        chatgroupmap.put(groupid, group);
    }

    /**
     * 解散群聊
     *
     * @param groupid
     */
    public void deleteGroup(String groupid) {
        chatgroupmap.remove(groupid);
    }


    public Collection<User> getOnLineUsers() {
        return userMap.values();
    }
    /**
     * 单例对象,静态内部类
     */
    private static class ChannelMessageHandlerHolder {

        private final static ChannelMessage channelMessage = new ChannelMessage();
    }

    public static ChannelMessage getChannelMessage() {
        return ChannelMessageHandlerHolder.channelMessage;
    }
}
