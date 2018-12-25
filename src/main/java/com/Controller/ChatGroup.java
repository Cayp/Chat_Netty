package com.Controller;


import com.Entity.User;
import com.NettyClasses.ChannelMessage;
import com.Service.GroupService;
import com.Utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
    public Response deleteOne(int id) {
        int i = groupService.deleteOne(id);
        boolean b = ChannelMessage.removeFromGroup(Integer.toString(id));
        if (b && i > 0) {
            return response.success("踢出成功");
        } else {
            if (i > 0) {
                return response.success("踢出成功");
            } else {
                return response.error("踢人失败");
            }

        }
    }

    @RequestMapping(value = "/addOne", method = RequestMethod.GET)
    public Response addOne(int id) {
        Integer one = groupService.getOne(id);
        if (one != null) {
            return response.error("该用户已在群聊中!");
        }
        groupService.addOne(id);
        boolean b = ChannelMessage.addToGroup(Integer.toString(id));
        if (b) {
            return response.success("邀请成功");
        } else {
            return response.error("邀请失败");
        }
    }

}
