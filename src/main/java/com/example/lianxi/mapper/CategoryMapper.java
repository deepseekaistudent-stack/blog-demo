package com.example.lianxi.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.lianxi.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分类 Mapper：继承 MyBatis-Plus BaseMapper，无自定义SQL。
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
