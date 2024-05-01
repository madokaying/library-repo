package com.ljh.library_spring.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface BookService {
    //分页返回图书列表
    public Page getBooksList(String bookName,Integer currentPage, Integer pageSize);
}
