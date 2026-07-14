package com.example.lianxi.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.lianxi.entity.Article;
import com.example.lianxi.mapper.ArticleMapper;
import com.example.lianxi.service.ArticleService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

/**
 * 文章业务实现：分页查询走自定义 Mapper 的连表 SQL，详情页浏览量自增。
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Override
    public IPage<Article> getArticlePage(int pageNum, int pageSize) {
        return baseMapper.selectPageWithCategory(new Page<>(pageNum, pageSize));
    }

    @Override
    public IPage<Article> getArticlesByCategory(int pageNum, int pageSize, Long categoryId) {
        return baseMapper.selectPageByCategory(new Page<>(pageNum, pageSize), categoryId);
    }

    @Override
    @CachePut(value = "blogArticle", key = "#id")
    public Article getArticleDetail(Long id) {
        baseMapper.incrementViewCount(id);
        return baseMapper.selectByIdWithCategory(id);
    }
}
