package com.example.lianxi.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.lianxi.entity.Comment;
import java.util.List;

/**
 * 评论业务接口：按文章ID查评论、新增评论。
 */
public interface CommentService extends IService<Comment> {
    List<Comment> getCommentsByArticleId(Long articleId);
    boolean addComment(Comment comment);
    boolean deleteComment(Long commentId);
}
