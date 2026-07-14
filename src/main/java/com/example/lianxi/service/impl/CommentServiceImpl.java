package com.example.lianxi.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.lianxi.entity.Comment;
import com.example.lianxi.exception.BusinessException;
import com.example.lianxi.mapper.CommentMapper;
import com.example.lianxi.service.CommentService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 评论业务实现：新增评论时驱逐对应文章的缓存（blogArticle::articleId）。
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Override
    public List<Comment> getCommentsByArticleId(Long articleId) {
        return baseMapper.selectByArticleId(articleId);
    }

    @Override
    @CacheEvict(value = "blogArticle", key = "#comment.articleId")
    public boolean addComment(Comment comment) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getArticleId, comment.getArticleId())
               .eq(Comment::getAuthor, comment.getAuthor())
               .eq(Comment::getContent, comment.getContent())
               .ge(Comment::getCreateTime,
                   java.time.LocalDateTime.now().minusSeconds(60));
        if (this.count(wrapper) > 0) {
            throw new BusinessException(4004, "请勿重复提交相同评论");
        }
        return save(comment);
    }

    @Override
    @CacheEvict(value = "blogArticle", key = "#result.articleId")
    public boolean deleteComment(Long commentId) {
        Comment comment = getById(commentId);
        if (comment == null) return false;
        return removeById(commentId);
    }
}
