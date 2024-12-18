package com.ljh.library_spring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbSearch {
    @TableId(type = IdType.AUTO)
    private Integer searchId;
    private String searchContent;
    private Integer searchTimes;
}
