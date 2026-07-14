package com.example.lianxi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 评论实体，对应 blog_comment 表。
 * 提交前经 Jsoup 过滤 XSS，articleId 关联文章。
 */
@Data
@TableName("blog_comment")
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long articleId;
    private String author;
    private String content;
    private LocalDateTime createTime;
}
