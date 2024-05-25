package com.ljh.library_spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljh.library_spring.entity.TbBook;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
//extends BaseMapper是mybatis-plus的内容
public interface BookMapper extends BaseMapper<TbBook> {
    List<String> getTagsOfBookById(Integer bookId);
}
