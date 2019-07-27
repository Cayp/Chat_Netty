package com.Controller;


import com.Entity.RegisterEntity;
import com.Entity.User;
import com.Service.SendMailSerivce;
import com.Service.UserService;
import com.Utils.Const;
import com.Utils.RandomVerifyCode;
import com.Utils.Response;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author ljp
 */
@RestController
@RequestMapping("verify")
public class VerifyController {

    @Resource(name = "userServiceImp")
    private UserService userService;

    @Autowired
    private Response response;

    @Resource(name = "sendMailSeriveImpl")
    private SendMailSerivce sendMailSerivce;

    @RequestMapping(value = "/SendMail", method = RequestMethod.GET)
    public Response sendMail(HttpSession session, String pictureCode, String mail) {
        String code = (String) session.getAttribute(Const.PICTURECODEKEY);
        Integer lastSendTime = (Integer) session.getAttribute(Const.MAILTIME);
        //判断上次发送邮箱成功的时间是否过了设置期限
        if (lastSendTime != null) {
            int l = (int) (System.currentTimeMillis() / 1000);
            if (l - lastSendTime < Const.MAILEXPIRE) {
                return response.error("你发送频率太快");
            }
        }
        //判断图片验证码是否一致,一致则允许发送
        if (code != null && code.toLowerCase().equals(pictureCode.toLowerCase())) {
            String mailcode = RandomVerifyCode.randomCode();
            session.setAttribute(Const.MAILCODEKEY, mailcode);
            session.setAttribute(Const.MAILTIME, (int) (System.currentTimeMillis() / 1000));
            sendMailSerivce.sendMail(mail, mailcode);
            return response.success("发送成功");
        } else {
            return response.error("验证码错误");
        }

    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Response register(RegisterEntity registerEntity, HttpSession session) {
        String code = (String) session.getAttribute(Const.MAILCODEKEY);
        if (code != null && code.equals(registerEntity.getVerifycode())) {
            boolean register = userService.register(registerEntity);
            if (register) {
                return response.success("success");
            } else {
                return response.error("邮箱已注册");
            }
        } else {
            return response.error("验证码错误");
        }
    }

    @RequestMapping(value = "/checkMail", method = RequestMethod.GET)
    public Response checkMail(String mail) {
        User login = userService.login(mail);
        if (login != null) {
            return response.error("existed");
        } else {
            return response.success("available");
        }
    }
}
