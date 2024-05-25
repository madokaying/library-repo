package com.ljh.library_spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParentComment {
    /**
     * 评论
     */
    private Comment comment;
    /**
     * 评论者昵称
     */
    private String userNickname;
    /**
     * 评论者头像
     */
    private String userAvatar;
    /**
     * 子评论的长度
     */
    private Integer childrenCommentLength;
    /**
     * 子评论的集合
     */
    private List<ChildrenComment> childrenCommentList;
}
