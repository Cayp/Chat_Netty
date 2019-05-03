package com.NettyClasses;

import com.Entity.User;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ljp
 */
public class ChannelMessage {
    private static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static ChannelGroup chatgroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static ConcurrentHashMap<String, ChannelId> channelIds = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Integer> groupUsers = new ConcurrentHashMap<>();
    private static Logger logger = LoggerFactory.getLogger(ChannelMessage.class);
    public final static String SINGLE_CHAT = "1";
    public final static String GROUP_CHAT = "2";
    private final static String IN = "6";
    private final static String OFF = "7";
    private final static String ONLINE = "8";
    private final static String OFFLINE = "9";


    public static boolean addChannel(Channel channel) {
        logger.info("client from {}",channel.remoteAddress());
        return group.add(channel);
    }


    public static boolean removeChannel(Channel channel) {
        System.out.println("removeChannel");
        group.remove(channel);
        String groupChatKey = getGroupChatKey(channel.id());
        //若下线是群聊中的用户则通知群
        if(groupChatKey!=null){
            Integer integer = groupUsers.get(groupChatKey);
            if(integer!=null){
                chatgroup.remove(channel);
                channelIds.remove(groupChatKey);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type",OFFLINE);
                jsonObject.put("id",groupChatKey);
                jsonObject.put("time",new Date());
                chatgroup.writeAndFlush(new TextWebSocketFrame(jsonObject.toString()));
            }
        }
        return true;
    }


    public static Channel getChannel(String account) {
        System.out.println("get channel");
        ChannelId channelId = channelIds.get(account);
        if (channelId == null) {
            return null;
        } else {
            return group.find(channelId);
        }

    }


    public static void saveAccount(String userId, Channel channel) {
        if (!channelIds.containsValue(channel.id())) {
            channelIds.put(userId, channel.id());
            if (groupUsers.get(userId) != null) {
                chatgroup.add(channel);
                //通知群聊中的人，该用户上线
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type",ONLINE);
                jsonObject.put("id",userId);
                jsonObject.put("time",new Date());
                chatgroup.writeAndFlush(new TextWebSocketFrame(jsonObject.toString()));

            }
        }
    }

    public static void removeAccount(String account) {

        channelIds.remove(account);
    }

    //删除群聊中的人员
    public static boolean removeFromGroup(String account) {
        Integer integer = groupUsers.get(account);
        groupUsers.remove(account);
        if (integer != null) {
            ChannelId channelId = channelIds.get(account);
            if (channelId != null) {
                chatgroup.remove(group.find(channelId));
            }
            //通知群聊中的人，该用户被踢出群聊
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type",OFF);
            jsonObject.put("id",account);
            jsonObject.put("time",new Date());
            chatgroup.writeAndFlush(new TextWebSocketFrame(jsonObject.toString()));
            return  true;
        }else {
            return false;
        }

    }

    //往群聊添加
    public static boolean addToGroup(String account) {
        Integer integer = groupUsers.get(account);
        if (integer == null) {
            groupUsers.put(account, 1);
            if (channelIds.get(account) != null) {
                Channel channel = group.find(channelIds.get(account));
                if (channel != null) {
                    chatgroup.add(channel);
                }
            }
            //通知群聊中的人，该用户邀请进群聊
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type",IN);
            jsonObject.put("id",account);
            jsonObject.put("time",new Date());
            chatgroup.writeAndFlush(new TextWebSocketFrame(jsonObject.toString()));
            return true;
        } else {
            return false;
        }
    }

    public static void initGroup(List<Integer> users) {
        for (Integer integer : users) {
            groupUsers.put(Integer.toString(integer), 1);
        }
    }

    public static boolean checkInGroup(String account) {
        Integer integer = groupUsers.get(account);
        return integer != null;
    }

    public static ChannelGroup getChatgroup() {
        return chatgroup;
    }

    private static String getGroupChatKey(ChannelId channelId){
        Set<Map.Entry<String, ChannelId>> entries = channelIds.entrySet();
        for (Map.Entry entry:entries) {
            if(entry.getValue()==channelId){
                return entry.getKey().toString();
            }
        }
        return null;
    }
}
