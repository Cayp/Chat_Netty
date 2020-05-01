package com.Controller;


import com.Entity.*;
import com.NettyClasses.ChannelMessage;
import com.Service.GroupService;
import com.Service.UnReadService;
import com.Service.UserService;
import com.Utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/relation")
public class ChatController {


    @Autowired
    Response response;

    @Resource(name = "userServiceImp")
    private UserService userService;

    @Resource(name = "groupServiceImp")
    private GroupService groupService;

    @Resource(name = "unReadServiceImp")
    private
    UnReadService unReadService;

    @RequestMapping(value = "/leftItemList", method = RequestMethod.GET)
    public Response getChatToList(long userId) {
        List<User> userFriends = userService.getUserFriends(userId);
        List<GroupChat> groupList = groupService.getGroupList(userId);
        List<ChatToEntity> signalChats = userFriends.stream().map(user -> new ChatToEntity(ChannelMessage.SINGLE_CHAT, user.getAccount(), user.getName(), user.getIcon())).collect(Collectors.toList());
        List<ChatToEntity> groupChats = groupList.stream().map(groupChat -> new ChatToEntity(ChannelMessage.GROUP_CHAT, groupChat.getGroupId(), groupChat.getGroupName(), groupChat.getPicture())).collect(Collectors.toList());
        groupChats.addAll(signalChats);
        return response.successWithDataList("获取聊天列表成功", groupChats);
    }

    @RequestMapping(value = "/unReadChatList", method = RequestMethod.GET)
    public Response getUnReadChatList(long userId) {
        List<Noreadme> noRead = unReadService.getNoRead(userId);
        List<UnReadGroup> groupUnRead = unReadService.getGroupUnRead();
        List<ChatListEntity> signalChats = noRead.stream().map(noreadme -> new ChatListEntity(ChannelMessage.SINGLE_CHAT, noreadme.getSendid(), noreadme.getSendid(), noreadme.getSendName(), noreadme.getSendAvatar(), noreadme.getTime(), noreadme.getMessage())).collect(Collectors.toList());
        List<ChatListEntity> groupList = groupUnRead.stream().map(unReadGroup -> new ChatListEntity(ChannelMessage.GROUP_CHAT, unReadGroup.getGroupId(), unReadGroup.getSendId(), unReadGroup.getSendName(), unReadGroup.getSendAvatar(), unReadGroup.getTime(), unReadGroup.getMessage())).collect(Collectors.toList());
        groupList.addAll(signalChats);
        return response.successWithDataList("获取未读信息列表成功", groupList);
    }
}
