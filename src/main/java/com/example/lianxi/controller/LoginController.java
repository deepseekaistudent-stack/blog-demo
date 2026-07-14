package com.example.lianxi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.lianxi.entity.User;
import com.example.lianxi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.core.metadata.IPage;
import javax.servlet.http.HttpSession;

/**
 * 登录与后台管理控制器。
 * 验证码校验依赖 Redis，用户认证走 BCrypt 密码比对。
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/login")
    public String loginPage(Model model, HttpSession session) {
        model.addAttribute("uuid", java.util.UUID.randomUUID().toString());
        model.addAttribute("_csrf", java.util.UUID.randomUUID().toString());
        session.setAttribute("csrf_token", model.getAttribute("_csrf"));
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          @RequestParam String captcha,
                          @RequestParam String uuid,
                          HttpSession session,
                          Model model) {
        if (username == null || username.trim().isEmpty()
            || password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "用户名和密码不能为空！");
            model.addAttribute("uuid", java.util.UUID.randomUUID().toString());
            return "login";
        }
        String key = "captcha:" + uuid;
        String storedCaptcha = stringRedisTemplate.opsForValue().get(key);
        stringRedisTemplate.delete(key);

        if (storedCaptcha == null || !storedCaptcha.equalsIgnoreCase(captcha)) {
            model.addAttribute("error", "验证码错误或已过期！");
            model.addAttribute("uuid", java.util.UUID.randomUUID().toString());
            return "login";
        }

        User user = userService.authenticate(username, password);

        if (user != null) {
            session.setAttribute("username", user.getUsername());
            return "redirect:/admin/dashboard";
        } else {
            model.addAttribute("error", "用户名或密码错误！");
            model.addAttribute("uuid", java.util.UUID.randomUUID().toString());
            return "login";
        }
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(HttpSession session, Model model,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int size) {
        Object username = session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username.toString());
        if (userService.count(wrapper) == 0) {
            session.invalidate();
            return "redirect:/login";
        }
        model.addAttribute("username", username.toString());
        IPage<User> userPage = userService.getUserPage(page, size);
        model.addAttribute("page", userPage);
        return "admin/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
