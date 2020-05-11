package com.Controller;


import com.Entity.PubRedPacket;
import com.Entity.RedPacketLog;
import com.Entity.UserRedPacket;
import com.Entity.Wallet;
import com.Service.RedPacketByRedisService;
import com.Service.RedPacketBySqlService;
import com.Utils.Const;
import com.Utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.HTML;
import java.util.List;

/**
 * 红包相关的controller
 *
 * @author ljp
 */
@RestController
@RequestMapping(value = "redpacket")
public class RedPacketController {

    @Autowired
    Response response;

    @Resource(name = "redPacketByRedisServiceImpl")
    private
    RedPacketByRedisService redPacketByRedisService;

    @Resource(name = "redPacketBySqlServiceImpl")
    private
    RedPacketBySqlService redPacketBySqlService;

    @RequestMapping(value = "/publish", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Response publishRedPacket(@RequestBody PubRedPacket pubRedPacket1, HttpSession httpSession) {
        long time = System.currentTimeMillis() / 1000;
        long userId = (long) httpSession.getAttribute("userId");
        int successCount = redPacketBySqlService.deductRedPacketMoney(userId, pubRedPacket1.total_money, time);
        if (successCount > 0) {
            PubRedPacket pubRedPacket = redPacketByRedisService.publishRedPacket(userId, pubRedPacket1.total_money, pubRedPacket1.redPacket_type, pubRedPacket1.redPacket_size, time);
            if (pubRedPacket != null) {
                return response.successWithData("发布成功",pubRedPacket.redPacketId);
            }
            return response.error("发布失败");
        } else {
            return response.error("你的钱包余额不足");
        }
    }

    @RequestMapping(value = "/grap", method = RequestMethod.POST)
    public Response grapRedPacket(long redPacketId, HttpSession httpSession) {
        long userId = (long) httpSession.getAttribute("userId");
        UserRedPacket redPacket = redPacketByRedisService.getRedPacket(redPacketId, userId);
        if (redPacket != null) {
            switch (redPacket.getStatus()) {
                case Const.SUCCESS:
                case Const.LASTONE:
                    redPacketBySqlService.addRedPacketDetail(redPacket);
                    return response.successWithData("success", redPacket);

                case Const.LOOT:
                    return response.error("没抢成功~");

                case Const.EXIST:
                    return response.error("你已经抢过了");
                default:
                    break;
            }
        }
        return response.error("红包已过期");
    }

    @RequestMapping(value = "/redpackets", method = RequestMethod.GET)
    public Response getRedPackets() {
        List<PubRedPacket> redPackets = redPacketBySqlService.getRedPackets();
        return response.successWithDataList("获取红包列表成功", redPackets);
    }

    @RequestMapping(value = "/graps", method = RequestMethod.GET)
    public Response getRedPacketRraps(long redpacketId) {
        List<UserRedPacket> redPacketGraps = redPacketBySqlService.getRedPacketGraps(redpacketId);
        return response.successWithDataList("获取红包已抢信息", redPacketGraps);
    }

    @RequestMapping(value = "/logs", method = RequestMethod.GET)
    public Response getRedPacketLogs(HttpSession httpSession) {
        long userId = (long) httpSession.getAttribute("userId");
        List<RedPacketLog> logs = redPacketBySqlService.getRedPacketLogByUserId(userId);
        return response.successWithDataList("获取红包日志成功", logs);
    }

    @RequestMapping(value = "/wallet", method = RequestMethod.GET)
    public Response getWallet(HttpSession httpSession) {
        long userId = (long) httpSession.getAttribute("userId");
        Wallet wallet = redPacketBySqlService.getWallet(userId);
        return response.successWithData("获取钱包成功", wallet);
    }
}
