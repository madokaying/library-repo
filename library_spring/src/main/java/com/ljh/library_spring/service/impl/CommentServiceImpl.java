package com.ljh.library_spring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ljh.library_spring.entity.*;
import com.ljh.library_spring.mapper.BookMapper;
import com.ljh.library_spring.mapper.CommentMapper;
import com.ljh.library_spring.mapper.PostMapper;
import com.ljh.library_spring.mapper.UserMapper;
import com.ljh.library_spring.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Resource
    private BookMapper bookMapper;

    @Resource
    private PostMapper postMapper;

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

    /**
     * 获取指定目标类型和目标ID下的父级评论列表。
     *
     * @param commentTargetType 评论目标类型，例如书籍、文章等的类型标识。
     * @param commentTargetId 评论目标的ID，例如书籍ID、文章ID等。
     * @return ParentComment 列表，包含每个父级评论及其子评论信息。
     */
    public List<ParentComment> getParentComment(Integer commentTargetType, Integer commentTargetId){
        // 构建查询条件，查询指定目标类型、目标ID且未被删除的一级评论，并按创建时间降序排序
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getCommentingTarget,commentTargetType)
                .eq(Comment::getCommentingTargetId,commentTargetId)
                .eq(Comment::getDeleteFlag,0)
                .orderByDesc(Comment::getCreatedTime);
        List<Comment> list = commentMapper.selectList(queryWrapper);

        List<ParentComment> parentCommentList = new ArrayList<>();
        for (Comment comment : list) {
            // 查询该父级评论下的所有子评论
            LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Comment::getCommentingTarget,3)
                    .eq(Comment::getCommentingTargetId,comment.getCommentId());
            List<Comment> children = commentMapper.selectList(lambdaQueryWrapper);

            User user = userMapper.selectById(comment.getUserId());
            if (user!=null){
                // 构建父级评论对象，包括用户昵称、头像以及子评论信息
                ParentComment parentComment = new ParentComment();
                parentComment.setComment(comment);
                parentComment.setUserNickname(user.getNickname());
                parentComment.setUserAvatar(user.getAvatar());

                List<Comment> allChildrenList = new ArrayList<>(children);
                if (!children.isEmpty()){
                    // 递归获取所有子评论，设置到ParentComment中
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

    /**
     * 获取子评论列表及其相关信息。
     *
     * @param children 直接子评论列表，用于递归查找所有子评论。
     * @param allChildrenList 所有子评论的集合，递归过程中会不断更新这个列表。
     * @return 返回一个封装了评论及其用户信息的列表，只包含未被删除的评论。
     */
    public List<ChildrenComment> getChildrenComment(List<Comment> children, List<Comment> allChildrenList) {
        List<Comment> list = new ArrayList<>();
        if (!children.isEmpty()) {
            // 遍历直接子评论，查询它们的下一级评论，并递归查找所有子评论
            for (Comment comment : children) {
                LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(Comment::getCommentingTarget, 3)
                        .eq(Comment::getCommentingTargetId, comment.getCommentId());
                List<Comment> lowerChildren = commentMapper.selectList(lambdaQueryWrapper);
                list.addAll(lowerChildren);
                allChildrenList.addAll(lowerChildren);
            }
            // 递归查找更深层次的子评论
            return getChildrenComment(list, allChildrenList);
        } else {
            List<ChildrenComment> childrenCommentList = new ArrayList<>();
            // 遍历所有子评论，构建ChildrenComment对象列表，只包含未被删除的评论
            for (Comment comment : allChildrenList) {
                if (comment.getDeleteFlag() == 0) {
                    ChildrenComment childrenComment = new ChildrenComment();
                    User user = userMapper.selectById(comment.getUserId());
                    // 查询评论的目标用户信息
                    LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                    lambdaQueryWrapper.eq(Comment::getCommentingTarget, 3)
                            .eq(Comment::getCommentId, comment.getCommentingTargetId());
                    Comment commentator = commentMapper.selectOne(lambdaQueryWrapper);
                    if (user != null) {
                        childrenComment.setComment(comment);
                        childrenComment.setUserNickname(user.getNickname());
                        childrenComment.setUserAvatar(user.getAvatar());
                        // 如果存在评论的目标用户，则设置其昵称
                        if (commentator != null) {
                            User commentatorUser = userMapper.selectById(commentator.getUserId());
                            if (commentatorUser != null) {
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

    public Result getUserComments(String userId) {
        List<Comment> comments;
        //返回全部的评论
        if ("".equals(userId)){
            LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.orderByDesc(Comment::getCreatedTime);
            comments = commentMapper.selectList(lambdaQueryWrapper);
        }else {
            //只获取需要的用户的评论
            Integer UID = Integer.parseInt(userId);
            LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Comment::getUserId,UID)
                    .eq(Comment::getDeleteFlag,0)
                    .orderByDesc(Comment::getCreatedTime);;
            comments = commentMapper.selectList(lambdaQueryWrapper);
        }
        //对评论进行封装，获取到评论者的昵称头像以及评论对象的名字
        List<ChildrenComment> list = new ArrayList<>();
        for (Comment comment : comments){
            ChildrenComment childrenComment = new ChildrenComment();
            User user = userMapper.selectById(comment.getUserId());

            childrenComment.setComment(comment);
            childrenComment.setUserNickname(user.getNickname());
            childrenComment.setUserAvatar(user.getAvatar());
            if (comment.getCommentingTarget() == 1){
                TbBook book  = bookMapper.selectById(comment.getCommentingTargetId());
                if (book != null){
                    childrenComment.setCommentTargetUserNickname(book.getBookName());
                } else {
                    childrenComment.setCommentTargetUserNickname("评论对象不存在");
                }
            }else if (comment.getCommentingTarget() == 2){
                Post post = postMapper.selectById(comment.getCommentingTargetId());
                if (post != null){
                    childrenComment.setCommentTargetUserNickname(post.getPostTitle());
                } else {
                    childrenComment.setCommentTargetUserNickname("评论对象不存在");
                }
            }else if (comment.getCommentingTarget() == 3){
                Comment beCommentedComment = commentMapper.selectById(comment.getCommentingTargetId());
                if (beCommentedComment != null){
                    User beCommentedUser = userMapper.selectById(beCommentedComment.getUserId());
                    String fixedTarget = beCommentedUser.getNickname() + ":" + beCommentedComment.getContent();
                    childrenComment.setCommentTargetUserNickname(fixedTarget);
                } else {
                    childrenComment.setCommentTargetUserNickname("评论对象不存在");
                }
            }
            list.add(childrenComment);
        }
        return new Result(200,"获取评论成功",list);
    }

    public Result deleteComment(Integer commentId) {
        //TODO 此处还能优化，若是删除的是一级评论，也应该把该评论下的所有子评论删除，有时间再做吧
        if (commentMapper.deleteById(commentId) > 0){
            return new Result(200,"删除成功");
        }
        return new Result(400,"删除成失败");
    }
}
