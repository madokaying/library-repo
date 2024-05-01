package com.ljh.library_spring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljh.library_spring.entity.Book;
import com.ljh.library_spring.mapper.BookMapper;
import com.ljh.library_spring.service.BookService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BookServiceImpl implements BookService {
    @Resource
    private BookMapper bookMapper;

    public Page getBooksList(String bookName, Integer currentPage, Integer pageSize) {
/*        使用mybatis-plus的分页插件，内含records（分页查询返回的列表）,"total": 总记录数,
        "size": 每页记录数,"current": 当前页,"pages":总页数 */
        Page<Book> page = new Page<>(currentPage, pageSize);
        if (bookName == "") {
            bookMapper.selectPage(page, null);
        } else {
            LambdaQueryWrapper<Book> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.like(Book::getBookName, bookName);
            bookMapper.selectPage(page, lambdaQueryWrapper);
        }
        return page;
    }
}