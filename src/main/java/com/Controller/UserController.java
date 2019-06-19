package com.Controller;


import com.Entity.Friend;
import com.Entity.User;
import com.Interceptor.MySessionListener;
import com.Service.UserService;
import com.Utils.HashStrUtil;
import com.Utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    public Response login(int account, String password, HttpSession session, HttpServletResponse servletResponse) {
        User login = userService.login(account);
        if (login == null) {
            return response.error("没有这账号!");
        } else {
            if (login.getPassword().equals(password)) {
                session.setAttribute("userId", login.getAccount());
                //写入验证字段,防止csrf攻击
                int authorizationNum = (int) (Math.random()*10000000);
                String authorization = HashStrUtil.hash(String.valueOf(authorizationNum), "MD5");
                session.setAttribute("authorization",authorization);
                Cookie cookie = new Cookie("authorization", authorization);
                cookie.setPath("/");
                cookie.setHttpOnly(false);
                servletResponse.addCookie(cookie);
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

    @RequestMapping(value = "/getFriends", method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public Response getFriends(HttpSession httpSession) {
        Integer account = (Integer) httpSession.getAttribute("userId");
        List<User> userFriends = userService.getUserFriends(account);
        return response.successWithDataList("获取好友列表成功", userFriends);
    }

    @RequestMapping(value = "online", method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public Response getOnlineNu() {
        return response.successWithData("online", MySessionListener.getOnline());
    }

    @RequestMapping(value = "/checklogin", method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public Response checklogin(HttpSession httpSession) {
        Integer userId = (Integer) httpSession.getAttribute("userId");
        if (userId == null) {
            return response.error("haven't login");
        } else {
            return response.success("ok");
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public Response logout(HttpSession httpSession) {
        httpSession.invalidate();
        return response.success("success");
    }

    @RequestMapping(value = "/addFriend", method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public Response addFriend(HttpSession httpSession, String id) {
        Integer userId = (Integer) httpSession.getAttribute("userId");
        int toid = Integer.parseInt(id);
        if(toid==userId){
            return response.error("不能添加自己!");
        }
        User login = userService.login(toid);
        if (login == null) {
            return response.error("没有该用户");
        } else {
            Friend i = userService.checkFriend(userId, toid);
            if (i !=null) {
                return response.error("已添加该好友");
            } else {
                userService.addFriend(toid,userId);
                return response.successWithData("添加好友成功!",login);
            }
        }
    }
    @RequestMapping(value = "/deleteFriend", method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public Response deleteFriend(HttpSession httpSession, String id) {
        Integer userId = (Integer) httpSession.getAttribute("userId");
        int toid = Integer.parseInt(id);
        int i = userService.deleteFriend(userId, toid);
        if(i==1){
            return response.success("删除成功！");
        }else{
            return response.error("fail");
        }

    }

}
