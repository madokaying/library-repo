package com.ljh.library_spring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ljh.library_spring.entity.Comment;
import com.ljh.library_spring.entity.ChildrenComment;
import com.ljh.library_spring.entity.Result;
import com.ljh.library_spring.mapper.CommentMapper;
import com.ljh.library_spring.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    /**
     * 添加评论
     * @param comment 评论对象，包含评论内容等信息
     * @return 返回操作结果，成功则返回状态码200和评论成功信息，失败则返回状态码400和评论失败信息
     */
    public Result addComment(Comment comment) {
        // 判断评论对象是否为空
        if (comment != null){
            // 将评论对象插入数据库
            Integer result = commentMapper.insert(comment);
            // 返回评论成功的结果
            if (result > 0){
                return new Result(200,"评论成功");
            } else {
                return new Result(500,"评论失败");
            }
        } else {
            // 返回评论失败的结果
            return new Result(400,"评论不能为空");
        }
    }

    public Result getCommentList(String commentTargetType, String commentTargetId, Integer currentPage, Integer pageSize) {
//        //TODO 要求获取到所有一级评论，包括评论者的头像和昵称，被回复者的昵称，所有的子评论以及子评论的数量
//        Integer id = Integer.parseInt(commentTargetId);
//        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        if ("book".equals(commentTargetType)){
//            lambdaQueryWrapper.eq(Comment::getCommentingTarget,1)
//                    .eq(Comment::getCommentingTargetId,id);
//            List<Comment> list = commentMapper.selectList(lambdaQueryWrapper);
//            //通过list里的commentId判断是否有子评论
//        } else if ("issue".equals(commentTargetType)){
//            lambdaQueryWrapper.eq(Comment::getCommentingTarget,2)
//                    .eq(Comment::getCommentingTargetId,id);
//        }
//        Page<Comment> page = new Page<>(currentPage, pageSize);
//        Integer type = Integer.parseInt(commentTargetType);
//        Integer id = Integer.parseInt(commentTargetId);
//        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(Comment::getCommentingTarget, type)
//                .eq(Comment::getCommentingTargetId, id);
//        commentMapper.selectPage(page, lambdaQueryWrapper);
//        return new Result(200,"获取评论成功",page);
        return null;
    }
}
