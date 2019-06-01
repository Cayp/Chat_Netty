package com.Utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * 进行红包的线段切割法工具类
 *
 * @author ljp
 */
public class CutPointUtils {


    /**
     * 根据红包的类型调用生成小红包的算法
     *
     * @param money
     * @param size
     * @param redPacket_type
     * @return
     */
    public static ArrayList<String> getRedPacketPartsByTypeId(double money, int size, int redPacket_type) {
        if (redPacket_type == Const.REDPACKET_AVERAGE) {
            return getRedPacketPartsForAverage(money, size);
        } else {
            return getRedPacketPartsForRandom(money, size);
        }
    }

    /**
     * 返回小红包的金额集合
     * 使用的是二倍均值法来分小红包
     *
     * @param money 总金额
     * @param size  红包个数
     * @return 每个小红包的金额保留2位小数
     */
    private static ArrayList<String> getRedPacketPartsForRandom(double money, int size) {
        ArrayList<String> resultStr = new ArrayList<>(size);
        String[] initStrs = new String[size];
        int cachemoney = (int) (money * 100);
        Random random = new Random();
        //公式N:初始金额,M:当前还剩人数 S:每一轮的小红包最大可取的值, S = N/M * 2   N = N - S
        for (int i = size; i > 0; i--) {
            int part;
            if (i == 1) {
                part = cachemoney;
            } else {
                int maxPart = cachemoney / i * 2;
                part = random.nextInt(maxPart);
                cachemoney -= part;
            }
            double partDou = (double) part / 100;
            initStrs[size - i] = Double.toString(partDou);
        }
        //对生成的小红包进行扰动处理,打乱分布
        for (int i = 0; i < size; i++) {
            int cacheIndex = random.nextInt(size);
            String cache = initStrs[i];
            initStrs[i] = initStrs[cacheIndex];
            initStrs[cacheIndex] = cache;
        }
        //将小红包录入list
        for (int i = 0; i < size; i++) {
            resultStr.add(initStrs[i]);
        }
        return resultStr;
    }

    /**
     * 均值红包,每个红包金额相同
     *
     * @param money
     * @param size
     * @return
     */
    private static ArrayList<String> getRedPacketPartsForAverage(double money, int size) {
        ArrayList<String> parts = new ArrayList<>(size);
        int partMoney = (int) (money * 100) / size;
        String partStr = Double.toString((double) partMoney / 100);
        for (int i = 0; i < size; i++) {
            parts.add(partStr);
        }
        return parts;
    }


}
