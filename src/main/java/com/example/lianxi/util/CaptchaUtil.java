package com.example.lianxi.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 纯 Java 验证码生成工具，不依赖第三方库
 */
public class CaptchaUtil {

    private static final String CHAR_POOL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int WIDTH = 120;
    private static final int HEIGHT = 40;
    private static final int CHAR_COUNT = 4;
    private static final int FONT_SIZE = 28;
    private static final Random RANDOM = new Random();

    public static String generateText() {
        // 从字符池随机抽取4个字符组成验证码文本
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CHAR_COUNT; i++) {
            sb.append(CHAR_POOL.charAt(RANDOM.nextInt(CHAR_POOL.length())));
        }
        return sb.toString();
    }

    /**
     * 生成验证码图片
     * @param text 验证码文本
     * @return 验证码图片
     */
    public static BufferedImage generateImage(String text) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        g.setColor(new Color(240, 245, 250));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(new Color(180, 200, 220));
        for (int i = 0; i < 5; i++) {
            int x1 = RANDOM.nextInt(WIDTH), y1 = RANDOM.nextInt(HEIGHT);
            int x2 = RANDOM.nextInt(WIDTH), y2 = RANDOM.nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }

        for (int i = 0; i < 40; i++) {
            image.setRGB(RANDOM.nextInt(WIDTH), RANDOM.nextInt(HEIGHT),
                    new Color(100 + RANDOM.nextInt(100), 100 + RANDOM.nextInt(100), 150 + RANDOM.nextInt(100)).getRGB());
        }

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font[] fonts = {
                new Font("Arial", Font.BOLD, FONT_SIZE),
                new Font("Courier New", Font.BOLD, FONT_SIZE),
                new Font("Times New Roman", Font.BOLD, FONT_SIZE)
        };

        for (int i = 0; i < text.length(); i++) {
            g.setFont(fonts[RANDOM.nextInt(fonts.length)]);
            g.setColor(new Color(20 + RANDOM.nextInt(60), 40 + RANDOM.nextInt(80), 100 + RANDOM.nextInt(100)));
            double angle = (RANDOM.nextDouble() - 0.5) * 0.5;
            g.rotate(angle, 15 + i * 25, 28);
            g.drawString(String.valueOf(text.charAt(i)), 10 + i * 25, 30);
            g.rotate(-angle, 15 + i * 25, 28);
        }

        g.dispose();
        return image;
    }
}
