package com.ljh.library_spring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
/*
  @TableName，当该bean与表名不符时，可以通过该注解指定对应的表
 */
//@TableName("tb_user")
public class TbBook {
    //对应数据库中的主键（UUID、自增id、雪花算法、redis、zookeeper）
    @TableId(type = IdType.AUTO)
    private Integer bookId;//书本编号
    private String bookName;//书名
    private String bookAuthor;//作者
    private String bookSummary;//简介
    private String publisher;//出版社
    private BigDecimal physicalBookPrice;//实体书价格
    private Integer bookNumber;//书本数
    private String bookCover;//封面地址
    private Integer borrowedTimes;//书本的借阅次数
    @Version//乐观锁version注解
    private Integer version;//乐观锁
    @TableLogic//逻辑删除
    private Integer deleted;
    /*注解填充字段*/
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;//创建/上架时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;//修改时间
}
