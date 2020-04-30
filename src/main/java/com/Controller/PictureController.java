package com.Controller;


import com.Entity.UpLoadEntity;
import com.Entity.UpLoadType;
import com.Utils.Const;
import com.Utils.FileUtils;
import com.Utils.RandomVerifyCode;
import com.Utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author ljp
 */
@RestController
@RequestMapping("/picture")
public class PictureController {

    @Autowired
    private Response response;

    @RequestMapping(value = "verifycode", method = RequestMethod.GET)
    public void getVerifyCode(HttpSession session, HttpServletResponse response) {
        RandomVerifyCode.getRandomImage(session, response, Const.PICTURECODEKEY);
    }

    @RequestMapping(value = "/chat", method = RequestMethod.PUT)
    public Response upLoadPicture(MultipartFile picture) throws IOException {
        String name =  System.currentTimeMillis() + "" + picture.getOriginalFilename();
        boolean upLoadResult = FileUtils.upLoadPicture(picture, UpLoadType.picture, name);
        if (upLoadResult) {
            UpLoadEntity upLoadEntity = new UpLoadEntity();
            upLoadEntity.setUrl("/chat/images/picture/" + name);
            return  response.successWithData("上传成功!", upLoadEntity);
        } else {
            return  response.error("上传失败!");
        }
    }
}
