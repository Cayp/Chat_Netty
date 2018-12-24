package com.NettyClasses;

import com.Entity.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ljp
 */
public class ChannelMessage {
    private static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static ChannelGroup chatgroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static ConcurrentHashMap<String, ChannelId> channelIds = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Integer> groupUsers = new ConcurrentHashMap<>();
    public final static String SINGLE_CHAT = "1";
    public final static String GROUP_CHAT = "2";

    public static boolean addChannel(Channel channel) {
        System.out.println("addChannel" + channel.id());
        return group.add(channel);
    }


    public static boolean removeChannel(Channel channel) {
        System.out.println("removeChannel");

        return group.remove(channel);
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
            }
        }
    }

    public static void removeAccount(String account) {
        channelIds.remove(account);
    }

    public static void removeFromGroup(String account) {
        ChannelId channelId = channelIds.get(account);
        if (channelId != null) {
            chatgroup.remove(group.find(channelId));
        }

    }

    public static void addToGroup(String account) {
        if (channelIds.get(account) != null) {
            Channel channel = group.find(channelIds.get(account));
            if (channel != null) {
                chatgroup.add(channel);
            }
        }
    }

    public static void initGroup(List<Integer> users) {
        for (Integer integer : users) {
            groupUsers.put(Integer.toString(integer), 1);
        }
    }

    public static ChannelGroup getChatgroup() {
        return chatgroup;
    }
}
