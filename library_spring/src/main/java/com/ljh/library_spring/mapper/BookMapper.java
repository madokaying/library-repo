package com.ljh.library_spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljh.library_spring.entity.Tag;
import com.ljh.library_spring.entity.TbBook;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
//extends BaseMapper是mybatis-plus的内容
public interface BookMapper extends BaseMapper<TbBook> {
    List<String> getTagsOfBookById(Integer bookId);

    Integer collectBook(Integer bookId,Integer userId);

    Integer cancelCollectBook(Integer bookId,Integer userId);

    Integer judgeBookIsCollected(Integer bookId,Integer userId);

    List<TbBook> getMyCollectBooks(Integer userId);

    List<Tag> getTagList();

    List<TbBook> getBookListByTag(Integer tagId, Integer start, Integer pageSize);

    List<TbBook> getBookRankingList(Integer start, Integer pageSize);

    Integer getBookCollectedNumber(Integer bookId);

    Integer getBookNum(Integer bookId);
}
