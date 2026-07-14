package com.example.lianxi.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.lianxi.entity.Category;
import com.example.lianxi.mapper.CategoryMapper;
import com.example.lianxi.service.CategoryService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 分类业务实现：全量查询缓存至 Redis（key=blogCategories::all）。
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Override
    @Cacheable(value = "blogCategories", key = "'all'")
    public List<Category> getAllCategories() {
        return list();
    }
}
