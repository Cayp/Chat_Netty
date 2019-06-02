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
    public static final String SUCCESS = "200";
    public static final String EXIST = "0";
    public static final String LOOT = "100";
    public static final String LASTONE = "300";
    public static final String HREDPACKEKEY = "HRedPacket_";
    public static final String PUBLISHREDPACKET_LUA =
            "local listkey = 'RedPacket_'..KEYS[1];\n" +
                    "for i = 1,#ARGV do " +
                    "redis.call('rpush',listkey,ARGV[i]); " +
                    "end\n"+
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
}
