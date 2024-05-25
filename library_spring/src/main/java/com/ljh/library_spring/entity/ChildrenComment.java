package com.ljh.library_spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChildrenComment {
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
     * 评论对象的昵称
     */
    private String commentTargetUserNickname;
}
