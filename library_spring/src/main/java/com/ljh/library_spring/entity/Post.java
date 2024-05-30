package com.ljh.library_spring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 帖子(Issue)实体类
 *
 * @author makejava
 * @since 2024-05-30 14:01:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    /**
     * 帖子id
     */
    @TableId(type = IdType.AUTO)
    private Integer postId;
    /**
     * 发帖时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createdTime;
    /**
     * 楼主id
     */
    private Integer ownerId;
    /**
     * 帖子内容
     */
    private String postContent;
    /**
     * 帖子标题
     */
    private String postTitle;
    /**
     * 帖子图片
     */
    private String postImage;
    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleteFlag;
}

