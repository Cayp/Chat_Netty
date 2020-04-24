package com.Controller;


import com.Entity.Groupchat;
import com.NettyClasses.ChannelMessage;
import com.Service.GroupService;
import com.Utils.Const;
import com.Utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * @author ljp
 */
@RestController
@RequestMapping(value = "/group")
public class ChatGroup {

    @Autowired
    Response response;


    @Resource(name = "groupServiceImp")
    private
    GroupService groupService;

    @RequestMapping(value = "/deleteOne", method = RequestMethod.GET)
    public Response deleteOne(int groupid, long userid) {
        int i = groupService.deleteOne(groupid, userid);
        ChannelMessage.getChannelMessage().removeFromGroup(String.valueOf(userid), String.valueOf(groupid));
        if (i > 0) {
            return response.success("踢出成功");
        } else {
            return response.error("踢人失败");
        }
    }

    @RequestMapping(value = "/addOne", method = RequestMethod.GET)
    public Response addOne(int groupid, long addid) {
        int one = groupService.addOne(groupid, addid);
        ChannelMessage.getChannelMessage().addToGroup(String.valueOf(addid), String.valueOf(groupid));
        if (one == 0) {
            return response.error("该用户已在群聊中!");
        }
        ChannelMessage.getChannelMessage().addToGroup(String.valueOf(addid), String.valueOf(groupid));
        return response.success("邀请成功");

    }


    @RequestMapping(value = "/deleteGroup", method = RequestMethod.GET)
    public Response deleteGroup(int groupid, long userid) {
        int i = groupService.deleteGroup(groupid, userid);
        ChannelMessage.getChannelMessage().deleteGroup(String.valueOf(groupid));
        if (i == Const.NORIGHT) {
            return response.error("你没有权限");
        } else {
            return response.success("删除成功!");
        }
    }

    @RequestMapping(value = "/changeOwner", method = RequestMethod.GET)
    public Response changeOwner(int groupid, long ownerid) {
        int i = groupService.changeOwner(groupid, ownerid);
        if (i == Const.NORIGHT) {
            return response.error("你没有权限");
        } else {
            return response.success("移交成功!");
        }
    }

    @RequestMapping(value = "/buildGroup", method = RequestMethod.POST)
    public Response buildGroup(long userid, String groupname) {
        Groupchat groupchat = groupService.buildGroup(userid, groupname);
        if (groupchat == null) {
            return response.error("创建失败");
        }
        ChannelMessage.getChannelMessage().addGroup(String.valueOf(groupchat.getGroupid()));
        return response.successWithData("创建成功", groupchat);
    }
}
