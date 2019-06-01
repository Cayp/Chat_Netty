package com.Controller;


import com.Entity.PubRedPacket;
import com.Service.RedPacketByRedisService;
import com.Service.RedPacketBySqlService;
import com.Utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 红包相关的controller
 *
 * @author ljp
 */
@RestController
@RequestMapping(value = "redPacket")
public class RedPacketController {

    @Autowired
    Response response;

    @Resource(name = "redPacketByRedisServiceImpl")
    RedPacketByRedisService redPacketByRedisService;

    @Resource(name = "redPacketBySqlServiceImpl")
    RedPacketBySqlService redPacketBySqlService;

    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    public Response publishRedPacket(int userid, double money, int size, int groupid, @RequestParam("type") int redPakcetType) {
        int successCount = redPacketBySqlService.deductRedPacketMoney(userid, money);
        if (successCount > 0) {
            PubRedPacket pubRedPacket = redPacketByRedisService.publishRedPacket(userid, money, redPakcetType, groupid, size);
            return response;
        } else {
            return response.error("not_enough");
        }
    }


}
