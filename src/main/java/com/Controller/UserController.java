package com.Controller;


import com.Entity.User;
import com.Interceptor.MySessionListener;
import com.Service.UserService;
import com.Utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author ljp
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Resource(name = "userServiceImp")
    private UserService userService;

    @Autowired
    private Response response;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @SuppressWarnings("unchecked")
    public Response login(int account, String password, HttpSession session) {
        User login = userService.login(account);
        if (login == null) {
            return response.error("没有这账号!");
        } else {
            if (login.getPassword().equals(password)) {
                session.setAttribute("userId",(Integer)login.getAccount());
                return response.successWithData("登录成功", login);
            } else {
                return response.error("登录失败");
            }
        }
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public Response getUser(String index) {
        List<User> userByIndex = userService.getUserByIndex(index);
        if (userByIndex.size() == 0) {
            return response.error("fail");
        } else {
            return response.successWithDataList("success", userByIndex);
        }
    }

    @RequestMapping(value = "/getFriends",method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public Response getFriends(HttpSession httpSession){
        Integer account = (Integer) httpSession.getAttribute("userId");
        List<User> userFriends = userService.getUserFriends(account);
      return response.successWithDataList("获取好友列表成功",userFriends);
    }

    @RequestMapping(value = "online",method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public Response getOnlineNu(){
        return response.successWithData("online",MySessionListener.getOnline());
    }

    @RequestMapping(value = "/checklogin",method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public Response checklogin(HttpSession httpSession){
        Integer userId = (Integer) httpSession.getAttribute("userId");
        if (userId==null){
            return response.error("haven't login");
        }else{
            return response.success("ok");
        }
    }

    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public Response logout(HttpSession httpSession){
        httpSession.invalidate();
        return response.success("success");
    }
}
