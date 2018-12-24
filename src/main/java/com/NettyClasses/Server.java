package com.NettyClasses;

import com.Entity.User;
import com.Service.GroupService;
import com.Utils.SpringUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author ljp
 */

public class Server  {



    public static void bind(int port) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                EventLoopGroup boosGroup =new NioEventLoopGroup();
                EventLoopGroup workerGroup =new NioEventLoopGroup();
                try {
                    ServerBootstrap serverBootstrap =  new ServerBootstrap();
                    serverBootstrap.group(boosGroup,workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .option(ChannelOption.SO_BACKLOG, 1024)
                            //有数据立即发送
                            .option(ChannelOption.TCP_NODELAY, true)
                            //保持连接
                            .childOption(ChannelOption.SO_KEEPALIVE, true)
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    ChannelPipeline pipeline = socketChannel.pipeline();
                                    pipeline.addLast("http-codec",new HttpServerCodec())
                                            .addLast("aggregator",new HttpObjectAggregator(65536))
                                            .addLast("http-chunked",new ChunkedWriteHandler())
                                            .addLast("handler",new ServerHandler());
                                }
                            });
                    //初始化有关群聊
                    GroupService groupService = SpringUtil.getBean(GroupService.class);
                    List<Integer> users = groupService.initGroup();
                    ChannelMessage.initGroup(users);
                    ChannelFuture sync = serverBootstrap.bind(port).sync();
                    if(sync.isSuccess()){
                        System.out.println("server start success");
                    }else{
                        System.out.println("server start fail");
                    }
                  sync.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    boosGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                }
            }
        });
        thread.start();
    }
}
