package com.example.lianxi.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.lianxi.entity.Article;

/**
 * 文章业务接口：分页列表、按分类筛选、详情（含浏览量递增）。
 */
public interface ArticleService extends IService<Article> {
    IPage<Article> getArticlePage(int pageNum, int pageSize);
    IPage<Article> getArticlesByCategory(int pageNum, int pageSize, Long categoryId);
    Article getArticleDetail(Long id);
}
