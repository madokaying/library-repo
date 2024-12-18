package com.ljh.library_spring.service;


import com.ljh.library_spring.entity.Comment;
import com.ljh.library_spring.entity.Result;

/**
 * 评论(Comment)表服务接口
 *
 * @author makejava
 * @since 2024-05-22 12:15:18
 */
public interface CommentService {

    Result addComment(Comment comment);

    Result getCommentList(String commentTargetType, String commentTargetId);

    Result getUserComments(String uid,String type);

    Result deleteComment(Integer commentId);
}
