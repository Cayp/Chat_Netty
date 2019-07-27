package com.Utils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;

/**
 * 验证码生成类
 *
 * @author ljp
 */

public class RandomVerifyCode {


    private static String randomString = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static int width = 100;//图片宽
    private static int height = 35;//图片高
    private static int lineNum = 25;//干扰线数量
    private static int codeNum = 5;//验证码长度
    private static int textSize = 23;//字号
    private static int randomCodeSize = 6;

    private static Font getFont() {
        return new Font("Clanner", Font.BOLD, textSize);
    }


    private static Color getRandColor(int fc, int bc, Random random) {
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc - 16);
        int g = fc + random.nextInt(bc - fc - 14);
        int b = fc + random.nextInt(bc - fc - 18);
        return new Color(r, g, b);
    }

    private static String drawString(Graphics g, String randomString, int i, Random random) {
        g.setFont(getFont());
        g.setColor(new Color(random.nextInt(101),
                random.nextInt(111), random.nextInt(121)));
        g.translate(random.nextInt(3), random.nextInt(3));
        g.drawString(randomString, 17 * i + 3, 25);
        return randomString;
    }

    private static void drawLine(Graphics g, Random random) {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(13);
        int yl = random.nextInt(15);
        g.setColor(new Color(random.nextInt(101),
                random.nextInt(111), random.nextInt(121)));
        g.drawLine(x, y, x + xl, y + yl);
    }

    public static void getRandomImage(HttpSession session, HttpServletResponse response, String key) {
        StringBuilder stringbuiler = new StringBuilder();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        Random random = new Random();
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        g.setColor(getRandColor(110, 133, random));
        for (int i = 0; i <= lineNum; i++) {
            drawLine(g, random);
        }
        for (int i = 0; i < codeNum; i++) {
            stringbuiler.append(drawString
                    (g, String.valueOf
                            (randomString.charAt(random.nextInt(randomString.length()))), i, random));
        }
        String randomString = stringbuiler.toString();
        session.setAttribute(key, randomString);
        g.dispose();
        try {
            ByteArrayOutputStream tmp = new ByteArrayOutputStream();
            ImageIO.write(image, "png", tmp);
            tmp.close();
            int contentLength = tmp.size();
            response.setHeader("content-length", contentLength + "");
            response.getOutputStream().write(tmp.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static String randomCode() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < randomCodeSize; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }
}
