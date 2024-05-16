package com.ljh.library_spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljh.library_spring.entity.TbBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
//extends BaseMapper是mybatis-plus的内容
public interface BookMapper extends BaseMapper<TbBook> {

}
