package com.ljh.library_spring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 评论(Comment)实体类
 *
 * @author makejava
 * @since 2024-05-22 12:14:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    /**
     * 评论id
     */
    @TableId(type = IdType.AUTO)
    private Integer commentId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    /**
     * 评论对象的id
     */
    private Integer commentingTargetId;
    /**
     * 评论用户id
     */
    private Long userId;
    /**
     * 评论对象：
     * 1、对书的评论
     * 2、对帖子的评论
     * 3、对用户的评论（回复）
     */
    private Integer commentingTarget;
    /**
     * 标记删除状态的字段。
     * 使用@TableLogic注解来定义该字段为逻辑删除标识。
     */
    @TableLogic
    private Integer deleteFlag;
}

