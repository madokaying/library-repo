package com.ljh.library_spring.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljh.library_spring.entity.Result;

public interface BookService {
    //分页返回图书列表
    public Page getBooksList(String bookName,Integer currentPage, Integer pageSize);
    //通过图书id获取图书详细信息
    Result getBookDetailById(Integer bookId);
}
