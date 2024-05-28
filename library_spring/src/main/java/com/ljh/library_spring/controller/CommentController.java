package com.ljh.library_spring.controller;

import com.ljh.library_spring.entity.Comment;
import com.ljh.library_spring.entity.Result;
import com.ljh.library_spring.service.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 评论(Comment)表控制层
 *
 * @author makejava
 * @since 2024-05-22 12:14:40
 */
@RestController
@RequestMapping("comment")
public class CommentController {
    /**
     * 服务对象
     */
    @Resource
    private CommentService commentService;

    @PostMapping("/addComment")
    public Result addComment(Comment comment){
        return commentService.addComment(comment);
    }

    @PostMapping("/getParentCommentList")
    public Result getCommentList(String commentTargetType,String commentTargetId){
        return commentService.getCommentList(commentTargetType,commentTargetId);
    }
}

