package com.ljh.library_spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljh.library_spring.entity.Menu;

import java.util.List;
 
 
public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermsByUserId(Long userid);
}