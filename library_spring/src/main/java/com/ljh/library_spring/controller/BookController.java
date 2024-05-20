package com.ljh.library_spring.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljh.library_spring.entity.Result;
import com.ljh.library_spring.service.BookService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/book")
public class BookController {
    @Resource
    private BookService bookService;

    //返回书的数据，并返回到result的data中
    @GetMapping("/getBooksList")
//    @PreAuthorize("hasAnyAuthority('system:book:list')")
    public Result getBooksList(@RequestParam(defaultValue = "")String bookName,@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "12") Integer pageSize){
        Page page = bookService.getBooksList(bookName,currentPage,pageSize);
        return new Result(200,page);
    }

    @PostMapping("/getBookDetailById")
    public Result getBookDetailById(Integer bookId){
        return bookService.getBookDetailById(bookId);
    }

    @PostMapping("/getBookTableOfContentsById")
    public Result getBookTableOfContentsById(Integer bookId) throws IOException {
        return bookService.getBookTableOfContentsById(bookId);
    }

    @PostMapping("/getBookChapterByHref")
    public Result getBookChapterByHref(String href,Integer bookId) throws IOException {
        return bookService.getBookChapterByHref(href,bookId);
    }
}
