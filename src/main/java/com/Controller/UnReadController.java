package com.Controller;


import com.Entity.Noreadme;
import com.Service.UnReadService;
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
@RequestMapping("/record/unRead")
public class UnReadController {


    @Autowired
    Response response;


    @Resource(name = "unReadServiceImp")
    private
    UnReadService unReadService;


    @RequestMapping(value = "/getUnRead", method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public Response getUnread(HttpSession httpSession) {
        Integer toid = (Integer) httpSession.getAttribute("userId");
        List<Noreadme> noRead = unReadService.getNoRead(toid);
        unReadService.deleteUnRead(toid);
        return response.successWithDataList("success", noRead);
    }


    @RequestMapping(value = "/getUnReadAcc", method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public Response getUnreadAcc(int toid) {
        List<Integer> unReadAcc = unReadService.getUnReadAcc(toid);
        return response.successWithDataList("success", unReadAcc);
    }
}
