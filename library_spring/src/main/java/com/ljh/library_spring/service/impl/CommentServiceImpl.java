package com.ljh.library_spring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ljh.library_spring.entity.*;
import com.ljh.library_spring.mapper.CommentMapper;
import com.ljh.library_spring.mapper.UserMapper;
import com.ljh.library_spring.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

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

    public Result getCommentList(String commentTargetType, String commentTargetId) {
        Integer type = Integer.valueOf(commentTargetType);
        Integer id = Integer.valueOf(commentTargetId);
        List<ParentComment> parentCommentList = getParentComment(type,id);
        return new Result<>(200,"获取评论成功",parentCommentList);
    }

    public List<ParentComment> getParentComment(Integer commentTargetType, Integer commentTargetId){
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getCommentingTarget,commentTargetType)
                .eq(Comment::getCommentingTargetId,commentTargetId)
                .eq(Comment::getDeleteFlag,0)
                .orderByDesc(Comment::getCreatedTime);
        //获取到编号书籍下的所有未被删除的一级评论
        List<Comment> list = commentMapper.selectList(queryWrapper);
        List<ParentComment> parentCommentList = new ArrayList<>();
        for (Comment comment : list) {
            //children为二级评论
            LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Comment::getCommentingTarget,3)
                    .eq(Comment::getCommentingTargetId,comment.getCommentId());
            List<Comment> children = commentMapper.selectList(lambdaQueryWrapper);
            User user = userMapper.selectById(comment.getUserId());
            if (user!=null){
                ParentComment parentComment = new ParentComment();
                parentComment.setComment(comment);
                parentComment.setUserNickname(user.getNickname());
                parentComment.setUserAvatar(user.getAvatar());
                List<Comment> allChildrenList = new ArrayList<>(children);
                if (!children.isEmpty()){
                    //用递归得到二级评论下的所有评论,放到ParentComment的ChildrenCommentList中
                    List<ChildrenComment> childrenCommentList = getChildrenComment(children,allChildrenList);
                    parentComment.setChildrenCommentLength(childrenCommentList.size());
                    parentComment.setChildrenCommentList(childrenCommentList);
                } else {
                    parentComment.setChildrenCommentLength(0);
                    parentComment.setChildrenCommentList(null);
                }
                parentCommentList.add(parentComment);
            }
        }
        return parentCommentList;
    }

    public List<ChildrenComment> getChildrenComment(List<Comment> children,List<Comment> allChildrenList){
        List<Comment> list = new ArrayList<>();
        if (!children.isEmpty()) {
            for (Comment comment:children){
                LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(Comment::getCommentingTarget,3)
                        .eq(Comment::getCommentingTargetId,comment.getCommentId());
                List<Comment> lowerChildren =commentMapper.selectList(lambdaQueryWrapper);
                list.addAll(lowerChildren);
                allChildrenList.addAll(lowerChildren);
            }
            return getChildrenComment(list,allChildrenList);
        } else {
            List<ChildrenComment> childrenCommentList = new ArrayList<>();
            for(Comment comment:allChildrenList){
                if (comment.getDeleteFlag() == 0){
                    ChildrenComment childrenComment = new ChildrenComment();
                    User user = userMapper.selectById(comment.getUserId());
                    LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                    lambdaQueryWrapper.eq(Comment::getCommentingTarget,3)
                            .eq(Comment::getCommentId,comment.getCommentingTargetId());
                    Comment commentator = commentMapper.selectOne(lambdaQueryWrapper);
                    if (user!=null){
                        childrenComment.setComment(comment);
                        childrenComment.setUserNickname(user.getNickname());
                        childrenComment.setUserAvatar(user.getAvatar());
                        if (commentator!=null){
                            User commentatorUser = userMapper.selectById(commentator.getUserId());
                            if (commentatorUser!=null){
                                childrenComment.setCommentTargetUserNickname(commentatorUser.getNickname());
                            }
                        }
                    }
                    childrenCommentList.add(childrenComment);
                }
            }
            return childrenCommentList;
        }
    }
}
