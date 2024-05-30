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

    @PostMapping("/getTagsOfBookById")
    public Result getTagsOfBookById(Integer bookId){
        return bookService.getTagsOfBookById(bookId);
    }

    //收藏书籍
    @PostMapping("/collectBook")
    public Result collectBook(Integer bookId,Integer userId){
        return bookService.collectBook(bookId,userId);
    }

    //取消收藏
    @PostMapping("/cancelCollectBook")
    public Result cancelCollectBook(Integer bookId,Integer userId){
        return bookService.cancelCollectBook(bookId,userId);
    }

    //判断是否已经收藏
    @PostMapping("/isCollectedBook")
    public Result isCollectedBook(Integer bookId,Integer userId){
        return bookService.isCollectedBook(bookId,userId);
    }

    //获取用户的收藏书籍列表
    @PostMapping("/getMyCollectBooks")
    public Result getMyCollectBooks(Integer userId){
        return bookService.getMyCollectBooks(userId);
    }
}
