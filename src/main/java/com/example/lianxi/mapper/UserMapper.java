package com.example.lianxi.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.lianxi.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper：继承 MyBatis-Plus BaseMapper，CRUD 由框架自动生成。
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
