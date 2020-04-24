package com.Utils;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;
import java.util.Set;

/**
 * 有关对象转换成字符串类
 *
 * @author ljp
 */
public class StringObjectUtils {


    public static String ObjectToString(String userId, String redPacketId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userid", userId);
        jsonObject.put("redpackid", redPacketId);
        return jsonObject.toJSONString();
    }

    public static Set<Map.Entry<String, Object>> StringToObject(String json){
        JSONObject jsonObject = JSONObject.parseObject(json);
        return jsonObject.entrySet();
    }
}
