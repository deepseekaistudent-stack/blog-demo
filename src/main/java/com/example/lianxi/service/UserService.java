package com.example.lianxi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.lianxi.entity.User;

/**
 * 用户业务接口：
 * authenticate 登录认证，register 注册并加密，
 * forgotPassword 重置密码，deleteUser 物理删除。
 */
public interface UserService extends IService<User> {
    User authenticate(String username, String rawPassword);
    User saveUser(User entity);
    User register(User user);
    User forgotPassword(String username, String newPassword);
    IPage<User> getUserPage(int page, int size);
}
