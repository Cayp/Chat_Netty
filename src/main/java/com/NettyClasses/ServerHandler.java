package com.NettyClasses;

import com.Dao.GetGroupDao;
import com.Entity.ChatMessage;
import com.Service.GroupService;
import com.Service.ServiceImp.UnReadServiceImp;
import com.Service.UnReadService;
import com.Utils.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;


import java.util.Date;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;

/**
 * @author ljp
 */
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler {

    private WebSocketServerHandshaker handshaker;


    private UnReadService unReadService;

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebsocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

    }


    private void handleWebsocketFrame(ChannelHandlerContext ctx, WebSocketFrame msg) {
        //关闭链路指令
        if (msg instanceof CloseWebSocketFrame) {
            //remove该用户的channel
            ChannelMessage.getChannelMessage().removeChannel(ctx.channel());
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) msg.retain());
            return;
        }

        //PING 消息
        if (msg instanceof PingWebSocketFrame) {
            ctx.write(new PongWebSocketFrame(msg.content().retain()));
            return;
        }

        //非文本
        if (!(msg instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format("%s frame type not support", msg.getClass().getName()));

        }
        //应答消息
        String requset = ((TextWebSocketFrame) msg).text();
        JSONObject jsonObject = JSONObject.parseObject(requset);
        int time = (int) (System.currentTimeMillis() / 1000);
        jsonObject.put("time", time);
        String toid = jsonObject.get("toid").toString();
        String type = jsonObject.get("type").toString();
        String fromid = jsonObject.get("fromid").toString();
        //判断是否是群聊 singlechat为一对一
        if (ChannelMessage.SINGLE_CHAT.equals(type)) {
            Channel channel = ChannelMessage.getChannelMessage().getChannel(toid);
            //对方用户没有上线
            if (channel == null) {
                if (unReadService == null) {
                    unReadService = SpringUtil.getBean(UnReadServiceImp.class);
                }
                unReadService.setUnRead(Integer.parseInt(toid), Integer.parseInt(fromid), time, Integer.parseInt(type), jsonObject.get("textone").toString());

            } else {
                channel.writeAndFlush(new TextWebSocketFrame(requset));
            }

            //群聊处理逻辑
        } else {
            ChannelGroup chatgroup = ChannelMessage.getChannelMessage().getChatgroup(toid);
            if (chatgroup != null) {
                GetGroupDao bean = SpringUtil.getBean(GetGroupDao.class);
                //信息持久化到数据库
                bean.addChatRecGroup(Integer.parseInt(toid), time, Integer.parseInt(fromid),jsonObject.get("textone").toString(), Integer.parseInt(type));
                chatgroup.writeAndFlush(new TextWebSocketFrame(jsonObject.toString()));
            } else {
                //不是群聊中的成员返回处理
                jsonObject.put("type", "4");
                ctx.channel().writeAndFlush(new TextWebSocketFrame(jsonObject.toString()));
            }
        }
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest msg) {

        //HTTP 请异常
        if (!msg.getDecoderResult().isSuccess() || !"websocket".equals(msg.headers().get("Upgrade"))) {
            sendHttpResponse(ctx, msg, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        //握手
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:10000/websocket", null, false);
        handshaker = wsFactory.newHandshaker(msg);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());

        } else {
            handshaker.handshake(ctx.channel(), msg);
            String uri = msg.getUri();
            String userid = uri.substring(11, uri.length());
            ChannelMessage.getChannelMessage().saveAccount(userid, ctx.channel());
        }
    }

    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest msg, FullHttpResponse resp) {

        //响应
        if (resp.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(resp.getStatus().toString(), CharsetUtil.UTF_8);
            resp.content().writeBytes(buf);
            buf.release();
            setContentLength(resp, resp.content().readableBytes());
        }

        //非Keep-Alive,关闭链接
        ChannelFuture future = ctx.channel().writeAndFlush(resp);
        if (!isKeepAlive(resp) || resp.getStatus().code() != 200) {
            future.addListener(ChannelFutureListener.CLOSE);
        }


    }
}

