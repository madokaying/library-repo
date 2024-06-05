package com.ljh.library_spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonData {
    /**
     * 该类用于存储各种计数信息。
     * <p>
     * myCommentCount: 评论数量
     * myBorrowCount: 借阅数量
     * myPostCount: 帖子数量
     * myCollectionCount: 收藏数量
     */
    private Integer myCommentCount; // 存储评论的数量
    private Integer myBorrowCount; // 借阅书的数量
    private Integer myPostCount; // 存储帖子的数量
    private Integer myCollectionCount; // 存储收藏的数量
}
