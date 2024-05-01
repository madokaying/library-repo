package com.ljh.library_spring.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljh.library_spring.entity.Result;
import com.ljh.library_spring.service.BookService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/book")
public class BookController {
    @Resource
    private BookService bookService;

    //返回书的数据，并返回到result的data中
    @GetMapping("/getBooksList")
//    @PreAuthorize("hasAnyAuthority('system:book:list')")
    public Result getBooksList(@RequestParam(defaultValue = "")String bookName,@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "16") Integer pageSize){
        Page page = bookService.getBooksList(bookName,currentPage,pageSize);
        return new Result(200,page);
    }
}
