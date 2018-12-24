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

    @RequestMapping(value = "/deleteOne", method = RequestMethod.POST)
    public Response deleteOne(int id) {
        int i = groupService.deleteOne(id);
        ChannelMessage.removeFromGroup(Integer.toString(id));
        if (i > 0) {
            return response.success("删除成功");
        } else {
            return response.error("删除失败");
        }
    }

    @RequestMapping(value = "/addOne", method = RequestMethod.POST)
    public Response addOne(int id) {
        int i = groupService.addOne(id);
        ChannelMessage.addToGroup(Integer.toString(id));
        if (i > 0) {
            return response.success("添加成功");
        } else {
            return response.error("添加失败");
        }
    }

}
