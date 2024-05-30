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
     * myBoughtCount: 购买数量
     * myIssueCount: 帖子数量
     * myCollectionCount: 收藏数量
     */
    private Integer myCommentCount; // 存储评论的数量
    private Integer myBoughtCount; // 存储购买书的数量
    private Integer myIssueCount; // 存储帖子的数量
    private Integer myCollectionCount; // 存储收藏的数量
}
