package com.ljh.library_spring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbBorrow {
    @TableId(type = IdType.AUTO)
    private Integer borrowId;
    private Integer userId;
    private Integer bookId;
    private Integer bookIdentifier;
    private Integer state;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    private LocalDateTime checkedTime;
    @TableLogic
    private Integer deleteFlag;
}
