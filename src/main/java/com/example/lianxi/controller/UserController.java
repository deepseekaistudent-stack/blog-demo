package com.example.lianxi.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.lianxi.common.Result;
import com.example.lianxi.entity.User;
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


}
