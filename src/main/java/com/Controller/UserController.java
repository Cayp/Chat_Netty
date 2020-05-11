package com.Controller;


import com.Entity.*;
import com.Interceptor.MySessionListener;
import com.Service.UserService;
import com.Utils.Const;
import com.Utils.FileUtils;
import com.Utils.HashStrUtil;
import com.Utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

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

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @SuppressWarnings("unchecked")
    public Response login(@RequestBody LoginData loginData, HttpSession session, HttpServletResponse servletResponse) {
        String account = loginData.getAccount();
        String password = loginData.getPassword();
        User login = userService.login(account);
        if (login == null) {
            return response.error("没有这账号!");
        } else {
            if (login.getPassword().equals(password)) {
                session.setAttribute("userId", login.getAccount());
                //写入验证字段,防止csrf攻击
                int authorizationNum = (int) (Math.random() * 10000000);
                String authorization = HashStrUtil.hash(String.valueOf(authorizationNum), "MD5");
                session.setAttribute("authorization", authorization);
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
        Long userId = (Long) httpSession.getAttribute("userId");
        List<User> userFriends = userService.getUserFriends(userId);
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
        Long userId = (Long) httpSession.getAttribute("userId");
        if (userId == null) {
            return response.error("haven't login");
        } else {
            return response.success("ok");
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public Response logout(HttpSession httpSession) {
        long userId = (long) httpSession.getAttribute("userId");
        int logout = userService.logout(userId, (int) (System.currentTimeMillis() / 1000));
        httpSession.invalidate();
        if (logout > 0) {
            return response.success("success");
        } else {
            return response.error("fail");
        }

    }

    @RequestMapping(value = "/addFriend", method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public Response addFriend(HttpSession httpSession, long id) {
        Long userId = (Long) httpSession.getAttribute("userId");
        if (userId == id) {
            return response.error("不能添加自己!");
        }
        User login = userService.findUser(id);
        if (login == null) {
            return response.error("没有该用户");
        } else {
            Friend i = userService.checkFriend(userId, id);
            if (i != null) {
                return response.error("已添加该好友");
            } else {
                int i1 = userService.addFriend(id, userId);
                if (i1 > 1) {
                    return response.successWithData("添加好友成功!", login);
                }
                return response.error("fail");
            }
        }
    }

    @RequestMapping(value = "/deleteFriend", method = RequestMethod.POST)
    @SuppressWarnings("unchecked")
    public Response deleteFriend(HttpSession httpSession, long friendId) {
        Long userId = (Long) httpSession.getAttribute("userId");
        int i = userService.deleteFriend(userId, friendId);
        if (i > 1) {
            return response.success("删除成功！");
        } else {
            return response.error("fail");
        }
    }

    @RequestMapping(value = "/changePassWord", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Response changePassWord(HttpSession httpSession, @RequestBody LoginData loginData) {
        long userId = (long) httpSession.getAttribute("userId");
        int i = userService.changePasswordByid(userId, loginData.getPassword());
        if (i > 0) {
            return response.success("密码修改成功");
        } else {
            return response.error("密码修改失败");
        }
    }

    @RequestMapping(value = "/avatar", method = RequestMethod.PUT)
    public Response upLoadAvatar(MultipartFile picture, HttpSession httpSession) throws IOException {
        long userId = (long) httpSession.getAttribute("userId");
        String avatar = System.currentTimeMillis() + "_" + userId + ".jpg";
        boolean upLoadResult = FileUtils.upLoadPicture(picture, UpLoadType.avatar, avatar);
        if (upLoadResult) {
            userService.updateAvatar(userId, avatar);
            return response.successWithData("上传成功!", new UpLoadEntity(avatar));
        } else {
            return response.error("上传失败!");
        }
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public Response getUsers() {
        List<User> users = userService.getUsers();
        List<PersonEntity> personEntityList = users.stream().map(user -> new PersonEntity(user.getAccount(), user.getName(), user.getIcon())).collect(Collectors.toList());
        return response.successWithDataList("success", personEntityList);
    }

    @RequestMapping(value = "/request/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Response addRequest(@RequestBody AddRequest addRequest, HttpSession httpSession) {
        long userId = (long) httpSession.getAttribute("userId");
        addRequest.setUserId(userId);
        addRequest.setTime(System.currentTimeMillis() / 1000);
        Friend friend = userService.checkFriend(addRequest.getUserId(), addRequest.getToId());
        if (friend !=null) {
            return response.error("已经是你的好友");
        }
        if (addRequest.getToId() == addRequest.getUserId()) {
            return response.error("你不能添加自己");
        }
        AddRequest request = userService.findRequest(userId, addRequest.getToId());
        if (request != null) {
            return response.error("该好友申请正在待处理");
        } else {
            int res = userService.addRequest(addRequest);
            if (res > 0) {
                return response.success("发送好友申请成功!");
            } else {
                return response.error("发送好友申请失败");
            }
        }
    }

    @RequestMapping(value = "/request/operate", method = RequestMethod.POST)
    public Response operateRequest(long requestId, int type) {
        boolean res = userService.operateRequest(requestId, type);
        if (res) {
            return response.success("操作成功");
        }
        return response.error("操作失败");
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Response searchUser(String searchString, HttpSession httpSession) {
        long userId = (long) httpSession.getAttribute("userId");
        List<User> users = userService.searchUsers(searchString, userId);
        return response.successWithDataList("获取成功", users);
    }

    @RequestMapping(value = "/requests", method = RequestMethod.GET)
    public Response getRequests(HttpSession httpSession) {
        long userId = (long) httpSession.getAttribute("userId");
        List<AddRequest> addRequest = userService.getAddRequest(userId);
        return response.successWithDataList("获取成功", addRequest);
    }

}
