package com.Utils;

/**
 * 各种的常量
 *
 * @author ljp
 */
public class Const {
    public static final int REDPACKET_AVERAGE = 0;
    public static final int REDPACKET_UNAVERAGE = 1;
    public static final int REDPACKET_MAXSIZE = 100;
    public static final int MAIL_REGISTER = 1001;
    public static final int MAIL_FINDPWBACK = 1002;
    //最大心跳时间
    public static final int MAX_PONG = 30;
    public static final String PICTURECODEKEY = "CODE";
    public static final String MAILCODEKEY = "MAIL";
    //邮箱验证码过期时间,秒
    public static final String MAILTIME= "MAILTIMEKEY";
    public static final int MAILEXPIRE = 60;
    public static final String DLEXCHANE = "DEADLETTER_EXCHANGE";
    public static final String DLQUEUEROUTINGKEY = "DIRECT_ROUTING_KEY";
    //设置过期红包时间 单位为毫秒
    public static final String REDPACKETOUTTIME = "60000";
    public static final String SUCCESS = "200";
    public static final String EXIST = "0";
    public static final String LOOT = "100";
    public static final String LASTONE = "300";
    public static final int NORIGHT = 1000;
    public static final String DEFAULT_PICTURE = "";
    public static final String HREDPACKEKEY = "HRedPacket_";
    public static final String LREDPACKETKEY = "RedPacket_";
    public static final String PUBLISHREDPACKET_LUA =
            "local listkey = 'RedPacket_'..KEYS[1];\n" +
                    "local redbacketHkey = 'HRedPacket_'..KEYS[1];\n" +
                    "local size = KEYS[2]" +
                    "local money = KEYS[3]" +
                    "for i = 1,#ARGV do " +
                    "redis.call('rpush',listkey,ARGV[i]); " +
                    "end\n" +
                    "redis.call('hset',redbacketHkey,'redpacketsize',size);\n" +
                    "redis.call('hset',redbacketHkey,'money',money);\n" +
                    "return '200'";
    public static final String GETREDPACKET_LUA =
            "local listkey = 'RedPacket_'..KEYS[1];\n" +
                    "local redbacetHkey = 'HRedPacket_'..KEYS[1];\n" +
                    "local userid = ARGV[1];\n" +
                    "local time = ARGV[2];\n" +
                    "local haveGet = redis.call('hget',redbacetHkey,userid);\n" +
                    "if haveGet  then\n" +
                    "return '0-';\n" +
                    "end\n" +
                    "local money = redis.call('lpop',listkey);\n" +
                    "if not money  then \n" +
                    "return '100-';\n" +
                    "end\n" +
                    "if redis.call('hset',redbacetHkey,userid,money..'-'..time) == 1 then\n" +
                    "if redis.call('llen',listkey) == 0 then\n" +
                    "return '300-'..money;\n" +
                    "end\n" +
                    "return '200-'..money;\n" +
                    "end";
    public static final String MAILCODE_LUA =
                    "local mailaccount = KEYS[1];\n" +
                    "local code = ARGV[1];\n" +
                    "local oldcode = redis.call('get',mailaccount)\n" +
                    "if oldcode then \n" +
                    "return oldcode;\n" +
                    "end\n" +
                    "redis.call('set',mailaccount,code)\n" +
                    "redis.call('expire',mailaccount,"+Const.MAILEXPIRE+")\n" +
                    "return code;\n";

}
