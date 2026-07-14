package com.example.lianxi.controller;

import com.example.lianxi.util.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 验证码接口：生成文本存入 Redis（2分钟有效），输出 JPEG 图片流。
 */
@Controller
public class CaptchaController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse response, String uuid) throws IOException {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        String captchaText = CaptchaUtil.generateText();
        BufferedImage image = CaptchaUtil.generateImage(captchaText);

        stringRedisTemplate.opsForValue().set("captcha:" + uuid, captchaText, 2, TimeUnit.MINUTES);

        try (ServletOutputStream out = response.getOutputStream()) {
            ImageIO.write(image, "jpg", out);
            out.flush();
        }
    }
}
