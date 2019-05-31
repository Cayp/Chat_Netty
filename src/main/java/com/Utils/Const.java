package com.Utils;

/**
 * 各种的常量
 *
 * @author ljp
 */
public class Const {
    public static final byte REDPACKET_AVERAGE = 0;
    public static final byte REDPACKET_UNAVERAGE = 1;
    public static final int REDPACKET_MAXSIZE = 100;
    public static final String SUCCESS = "200";
    public static final String EXIST = "0";
    public static final String LOOT = "100";
    public static final String LASTONE = "300";
    public static final String PUBLISHREDPACKET_LUA =
            "local listkey = 'RedPacket_'..KEYS[1];\n" +
                    "for i = 1,#ARGV do\n" +
                    "redis.call('rpush',listkey,ARGV[i]);\n" +
                    "end";
    public static final String GETREDPACKET_LUA =
            "local redbacetHkey = 'HRedPacket_'..KEYS[1];\n" +
                    "local userid = ARGV[1];\n" +
                    "local haveGet = redis.call('hget',redbacetHkey,userid);\n" +
                    "if haveGet != nil then\n" +
                    "\treturn 0..'-';\n" +
                    "end\n" +
                    "local money = redis.call('lpop',listkey);\n" +
                    "if money == nil then \n" +
                    "\treturn 1..'-';\n" +
                    "end\n" +
                    "if redis.call('hset',redbacetHkey,userid,money)==1 then\n" +
                    "if redis.call('llen',listkey) == 0 then\n" +
                    "\treturn 2..'-'..money;\n" +
                    "end\n" +
                    "    return 3..'-'..money;\n" +
                    "end\n";
}
