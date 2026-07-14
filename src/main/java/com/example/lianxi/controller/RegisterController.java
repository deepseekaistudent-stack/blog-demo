package com.example.lianxi.controller;

import com.example.lianxi.entity.User;
import com.example.lianxi.exception.BusinessException;
import com.example.lianxi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * 用户注册控制器，调用 UserService.register() 完成 BCrypt 加密入库。
 */
@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registerPage(Model model, HttpSession session) {
        model.addAttribute("_csrf", UUID.randomUUID().toString());
        session.setAttribute("csrf_token", model.getAttribute("_csrf"));
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam(required = false) String confirmPassword,
                             @RequestParam String gender,
                             @RequestParam(required = false) String addr,
                             Model model) {
        if (confirmPassword != null && !confirmPassword.equals(password)) {
            model.addAttribute("error", "两次输入的密码不一致！");
            return "register";
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setGender(gender);
        user.setAddr(addr != null ? addr : "");

        try {
            userService.register(user);
            model.addAttribute("success", "注册成功，请前往登录！");
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "register";
    }
}
