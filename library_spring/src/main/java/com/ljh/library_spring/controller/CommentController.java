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
@CrossOrigin
@RestController
@RequestMapping("comment")
public class CommentController {
    /**
     * 服务对象
     */
    @Resource
    private CommentService commentService;

    @PostMapping("/addComment")
    public Result addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }

    @PostMapping("/deleteComment")
    public Result deleteComment(Integer commentId){
        return commentService.deleteComment(commentId);
    }

    @PostMapping("/getParentCommentList")
    public Result getCommentList(String commentTargetType,String commentTargetId){
        return commentService.getCommentList(commentTargetType,commentTargetId);
    }

    //获取评论数据，接收用户UID，若UID为""，则返回所有的评论数据（给管理员获取所有评论使用）
    @PostMapping("/getUserComments")
    public Result getUserComments(@RequestParam(defaultValue = "")String UID){
        String comment = "comment";
        return commentService.getUserComments(UID,comment);
    }

    @PostMapping("/getMyMessage")
    public Result getMyMessage(@RequestParam(defaultValue = "")String UID){
        String message = "message";
        return commentService.getUserComments(UID,message);
    }
}

