package com.Utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * hash工具类
 *
 * @author ljp
 */
public class HashStrUtil {
    public static String hash(String data, String algorithm) {
        try {
            MessageDigest instance = MessageDigest.getInstance(algorithm);
            byte[] digest = instance.digest(data.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (int hashByte : digest) {
                if (hashByte < 0) {
                    hashByte += 256;
                }
                if (hashByte < 16) {
                    stringBuilder.append("0");
                }
                stringBuilder.append(Integer.toHexString(hashByte));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
