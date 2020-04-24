package com.Controller;

import com.Dao.TestDao;
import com.Service.ServiceImp.TestServiceImpl;
import com.Utils.Const;
import com.Utils.CutPointUtils;
import com.rabbitmq.client.Channel;
import com.sun.java.accessibility.util.java.awt.TextComponentTranslator;
import io.netty.buffer.CompositeByteBuf;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author ljp
 */
@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    JedisPool jedisPool;

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Autowired
    TestDao testDao;

    @Resource
    TestServiceImpl testService;

    @Autowired
    RabbitTemplate rabbitTemplate;
    public static AtomicInteger integer = new AtomicInteger(1);

    public TestController() {
        System.out.println("TestController bulid");
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() throws InterruptedException {
        System.out.println("Thread -name:" + Thread.currentThread().getName());
        return "success hi";
    }

    @RequestMapping(value = "/test1", method = RequestMethod.GET)
    public String test1() {
        ArrayList<String> keyList = new ArrayList<>();
        ArrayList<String> argvList = CutPointUtils.getRedPacketPartsByTypeId(100, 10, 1);
        keyList.add("2");
        Jedis jedis = jedisPool.getResource();
        String s = jedis.scriptLoad(Const.PUBLISHREDPACKET_LUA);
        Object eval = jedis.evalsha(s, keyList, argvList);
        System.out.println((String) eval);
        jedis.close();
        return "success";
    }

    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    public String test2() {
        String Key = "1";
        String userid = "1";
        String time = "2";
        Jedis jedis = jedisPool.getResource();
        Object eval = jedis.eval(Const.GETREDPACKET_LUA, 2, Key, "", userid, time);
        System.out.println((String) eval);
        return "success";
    }

    @RequestMapping(value = "/test3", method = RequestMethod.GET)
    public String test3() throws SQLException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Connection connection = sqlSession.getConnection();
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement("insert into wallet(money) values(1000)");
        long time = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            preparedStatement.addBatch();
        }
        int[] ints = preparedStatement.executeBatch();
        sqlSession.commit();
        connection.setAutoCommit(true);
        sqlSession.close();
        System.out.println(System.currentTimeMillis() - time);
//        System.out.println(ints.length);
        return "success";
    }

    @RequestMapping(value = "/testMq", method = RequestMethod.POST)
    public String test4(String args) {
        MessagePostProcessor messagePostProcessor = message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setContentEncoding("utf-8");
            messageProperties.setExpiration("10000");
            return message;
        };
        rabbitTemplate.convertAndSend("Test_DEADLETTER_EXCHANGE", "TEST_DIRECT_ROUTING_KEY", args, messagePostProcessor);
        return "success";
    }

    @RequestMapping(value = "/testMq1", method = RequestMethod.POST)
    public String test5(String args) {
        MessagePostProcessor messagePostProcessor = message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setContentEncoding("utf-8");
            messageProperties.setExpiration("20000");
            return message;
        };
        rabbitTemplate.convertAndSend("Test_DEADLETTER_EXCHANGE", "TEST_DIRECT_ROUTING_KEY", args, messagePostProcessor);
        return "success";
    }

    @RequestMapping(value = "testredis", method = RequestMethod.GET)
    public void test6() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            Jedis resource = jedisPool.getResource();
        }
        Thread.sleep(100000);

    }

    @RequestMapping(value = "test7", method = RequestMethod.GET)
    public void test7() {

        testService.insertTest("test");
    }

}
