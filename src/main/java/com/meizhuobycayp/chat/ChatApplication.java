package com.meizhuobycayp.chat;

import com.NettyClasses.Server;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author ljp
 */

@ComponentScan({"com.Controller","com.Service","com.Utils","com.Interceptor","com.NettyClasses","com.Config"})
@MapperScan("com.Dao")
@SpringBootApplication
public class ChatApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);
		Server.bind(10000);
	}

}

