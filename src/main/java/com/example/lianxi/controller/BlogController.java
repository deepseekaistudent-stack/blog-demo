package com.example.lianxi.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.lianxi.entity.Article;
import com.example.lianxi.entity.Category;
import com.example.lianxi.entity.Comment;
import com.example.lianxi.service.ArticleService;
import com.example.lianxi.service.CategoryService;
import com.example.lianxi.service.CommentService;
import com.example.lianxi.util.HtmlSanitizer;
import com.example.lianxi.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 博客前台控制器：文章列表、详情、评论提交。
 * 使用 MyBatis-Plus 分页查询，Jsoup 做评论 XSS 过滤。
 */
@Controller
public class BlogController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "1") int page,
                        @RequestParam(required = false) Long categoryId,
                        Model model) {
        int pageSize = 5;
        IPage<Article> articlePage;
        if (categoryId != null) {
            articlePage = articleService.getArticlesByCategory(page, pageSize, categoryId);
            model.addAttribute("currentCategoryId", categoryId);
        } else {
            articlePage = articleService.getArticlePage(page, pageSize);
        }
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("articles", articlePage.getRecords());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articlePage.getPages());
        model.addAttribute("categories", categories);
        return "list";
    }

    @GetMapping("/article/{id}")
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        Article article = articleService.getArticleDetail(id);
        if (article == null) {
            return "redirect:/";
        }
        List<Comment> comments = commentService.getCommentsByArticleId(id);
        model.addAttribute("article", article);
        model.addAttribute("comments", comments);
        model.addAttribute("_csrf", java.util.UUID.randomUUID().toString());
        session.setAttribute("csrf_token", model.getAttribute("_csrf"));
        return "detail";
    }

    @PostMapping("/comment")
    public String addComment(@RequestParam Long articleId,
                             @RequestParam(defaultValue = "匿名") String author,
                             @RequestParam String content,
                             RedirectAttributes redirectAttributes) {
        Comment comment = new Comment();
        comment.setArticleId(articleId);
        comment.setAuthor(author);
        comment.setContent(HtmlSanitizer.cleanComment(content));
        comment.setCreateTime(LocalDateTime.now());
        
        try {
            commentService.addComment(comment);
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/article/" + articleId;
    }
}
