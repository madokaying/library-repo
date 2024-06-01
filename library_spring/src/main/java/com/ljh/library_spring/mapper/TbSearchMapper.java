package com.ljh.library_spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljh.library_spring.entity.TbSearch;
import java.util.List;
public interface TbSearchMapper extends BaseMapper<TbSearch> {
    List<TbSearch> getSearchContent(Integer num);
}
