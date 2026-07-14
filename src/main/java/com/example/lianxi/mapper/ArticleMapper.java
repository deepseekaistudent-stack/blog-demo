package com.example.lianxi.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.lianxi.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 文章 Mapper：自定义分页连表查询（LEFT JOIN blog_category），
 * 内置浏览量自增 SQL。
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    @Select("SELECT a.*, c.name AS category_name FROM blog_article a LEFT JOIN blog_category c ON a.category_id = c.id ORDER BY a.create_time DESC")
    IPage<Article> selectPageWithCategory(Page<Article> page);

    @Select("SELECT a.*, c.name AS category_name FROM blog_article a LEFT JOIN blog_category c ON a.category_id = c.id WHERE a.category_id = #{categoryId} ORDER BY a.create_time DESC")
    IPage<Article> selectPageByCategory(Page<Article> page, @Param("categoryId") Long categoryId);

    @Select("SELECT a.*, c.name AS category_name FROM blog_article a LEFT JOIN blog_category c ON a.category_id = c.id WHERE a.id = #{id}")
    Article selectByIdWithCategory(@Param("id") Long id);

    @Update("UPDATE blog_article SET view_count = view_count + 1 WHERE id = #{id}")
    int incrementViewCount(@Param("id") Long id);
}
