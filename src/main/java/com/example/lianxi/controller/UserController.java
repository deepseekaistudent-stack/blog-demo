package com.example.lianxi.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.lianxi.common.Result;
import com.example.lianxi.entity.User;
import com.example.lianxi.exception.BusinessException;
import com.example.lianxi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理 REST 接口：Dashboard 后台 CRUD 的数据源。
 * 所有接口统一使用 Result&lt;T&gt; 响应体。
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public Result<IPage<User>> list(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        IPage<User> userPage = userService.getUserPage(page, size);
        return Result.success(userPage);
    }

    @PostMapping
    public Result<User> register(@RequestBody User user) {
        User savedUser = userService.register(user);
        return Result.success(savedUser);
    }

    @PutMapping("/{id}")
    public Result<User> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userService.updateById(user);
        return Result.success(user);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success(null);
    }

    @PostMapping("/login")
    public Result<User> login(@RequestBody User user) {
        User authenticatedUser = userService.authenticate(user.getUsername(), user.getPassword());
        if (authenticatedUser != null) {
            authenticatedUser.setPassword(null);
            return Result.success(authenticatedUser);
        }
        throw new BusinessException(401, "用户名或密码错误");
    }

    @PutMapping("/password")
    public Result<Void> resetPassword(@RequestBody User user) {
        userService.forgotPassword(user.getUsername(), user.getPassword());
        return Result.success(null);
    }
}