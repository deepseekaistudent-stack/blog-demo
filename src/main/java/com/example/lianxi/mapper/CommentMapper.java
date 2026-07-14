package com.example.lianxi.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.lianxi.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 评论 Mapper：按文章ID升序查评论列表。
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    @Select("SELECT * FROM blog_comment WHERE article_id = #{articleId} ORDER BY create_time ASC")
    List<Comment> selectByArticleId(@Param("articleId") Long articleId);
}
