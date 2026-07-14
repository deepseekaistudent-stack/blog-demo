package com.example.lianxi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.lianxi.entity.User;
import com.example.lianxi.exception.BusinessException;
import com.example.lianxi.mapper.UserMapper;
import com.example.lianxi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 用户业务实现。
 * 密码全程 BCrypt 处理，增删改查配合 Redis 缓存，统一使用注入的 BCryptPasswordEncoder。
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    private UserService self;

    @Override
    public User register(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new BusinessException(4002, "用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new BusinessException(4003, "密码不能为空");
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        if (this.count(wrapper) > 0) {
            throw new BusinessException(4001, "用户名已存在，请更换");
        }
        self.saveUser(user);
        return user;
    }

    @Override
    public User authenticate(String username, String rawPassword) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = this.getOne(wrapper);

        if (user == null) {
            return null;
        }

        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public User forgotPassword(String username, String newPassword) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new BusinessException(4003, "新密码不能为空");
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = this.getOne(wrapper);
        if (user == null) {
            return null;
        }

        String encodedPwd = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPwd);

        self.updateById(user);
        return user;
    }

    // 分页查询用户
    @Override
    public IPage<User> getUserPage(int page, int size) {
        return baseMapper.selectPage(
            new Page<>(page, size),
            new LambdaQueryWrapper<User>().orderByDesc(User::getId)
        );
    }

    @Override
    @Cacheable(value = "user", key = "#id")
    public User getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    @CachePut(value = "user", key = "#result.id")
    public User saveUser(User entity) {
        if (entity.getUsername() == null || entity.getUsername().trim().isEmpty()) {
            throw new BusinessException(4002, "用户名不能为空");
        }
        if (entity.getPassword() == null || entity.getPassword().trim().isEmpty()) {
            throw new BusinessException(4003, "密码不能为空");
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, entity.getUsername());
        if (this.count(wrapper) > 0) {
            throw new BusinessException(4001, "用户名已存在，请更换");
        }
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        super.save(entity);
        return entity;
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    @CachePut(value = "user", key = "#entity.id")
    public boolean updateById(User entity) {
        if (entity.getUsername() != null && !entity.getUsername().trim().isEmpty()) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getUsername, entity.getUsername())
                   .ne(User::getId, entity.getId());
            if (this.count(wrapper) > 0) {
                throw new BusinessException(4001, "用户名已被其他用户占用，请更换");
            }
        }
        if (entity.getPassword() != null && !entity.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        }
        return super.updateById(entity);
    }

    @Override
    @CacheEvict(value = {"user", "users"}, allEntries = true)
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }
}