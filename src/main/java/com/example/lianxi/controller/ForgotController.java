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
 * 忘记密码控制器：验证用户名存在后重置密码并 BCrypt 加密。
 */
@Controller
public class ForgotController {

    @Autowired
    private UserService userService;

    @GetMapping("/forgot-password")
    public String forgot(Model model, HttpSession session){
        model.addAttribute("_csrf", UUID.randomUUID().toString());
        session.setAttribute("csrf_token", model.getAttribute("_csrf"));
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String doForgot(@RequestParam String username,
                           @RequestParam String newPassword,
                           Model model){
        try {
            User updatedUser = userService.forgotPassword(username, newPassword);
            if (updatedUser == null) {
                model.addAttribute("error", "用户名不存在！");
            } else {
                model.addAttribute("success", "重置密码成功，请前往登录！");
            }
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "forgot-password";
    }
}
