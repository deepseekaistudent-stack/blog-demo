package com.example.lianxi.controller;

import com.example.lianxi.common.Result;
import com.example.lianxi.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class AdminCommentController {

    @Autowired
    private CommentService commentService;

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(commentService.deleteComment(id));
    }
}
