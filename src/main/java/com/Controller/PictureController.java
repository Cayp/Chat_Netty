package com.Controller;


import com.Utils.Const;
import com.Utils.RandomVerifyCode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author ljp
 */
@RestController
@RequestMapping("/picture")
public class PictureController {


    @RequestMapping(value = "verifycode", method = RequestMethod.GET)
    public void getVerifyCode(HttpSession session, HttpServletResponse response) {
        RandomVerifyCode.getRandomImage(session, response, Const.PICTURECODEKEY);
    }
}
