package com.Controller;


import com.Entity.PubRedPacket;
import com.Entity.UserRedPacket;
import com.Service.RedPacketByRedisService;
import com.Service.RedPacketBySqlService;
import com.Utils.Const;
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
    Response<UserRedPacket> response;

    @Resource(name = "redPacketByRedisServiceImpl")
    RedPacketByRedisService redPacketByRedisService;

    @Resource(name = "redPacketBySqlServiceImpl")
    RedPacketBySqlService redPacketBySqlService;

    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    public Response publishRedPacket(int userid, double money, int size, int groupid, @RequestParam("type") int redPakcetType) {
        int successCount = redPacketBySqlService.deductRedPacketMoney(userid, money);
        if (successCount > 0) {
            PubRedPacket pubRedPacket = redPacketByRedisService.publishRedPacket(userid, money, redPakcetType, groupid, size);
            if (pubRedPacket != null) {
                return response.success("success");
            }
            return response.error("fail");
        } else {
            return response.error("not_enough_fail");
        }
    }

    @RequestMapping(value = "/grap", method = RequestMethod.POST)
    public Response grapRedPacket(int userid, long redPacketId) {
        UserRedPacket redPacket = redPacketByRedisService.getRedPacket(redPacketId, userid);
        if (redPacket != null) {
            switch (redPacket.getStatus()) {
                case Const.SUCCESS:
                case Const.LASTONE:
                    redPacketBySqlService.addMoneyToUser(userid, Double.valueOf(redPacket.money));
                    return response.successWithData("success", redPacket);

                case Const.LOOT:
                    return response.error("you_grap_fail");

                case Const.EXIST:
                    return response.error("you_had_grapped");
                default:
                    break;
            }
        }
        return response.error("fail");
    }


}
