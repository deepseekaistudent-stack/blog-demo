package com.example.lianxi.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.lianxi.entity.Category;
import java.util.List;

/**
 * 分类业务接口：获取全部分类列表。
 */
public interface CategoryService extends IService<Category> {
    List<Category> getAllCategories();
}
