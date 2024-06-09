package com.ljh.library_spring.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljh.library_spring.entity.Result;
import com.ljh.library_spring.entity.TbBook;
import com.ljh.library_spring.service.BookService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/getBookCollectedNumber")
    public Result getBookCollectedNumber(Integer bookId){
        return bookService.getBookCollectedNumber(bookId);
    }

    @PostMapping("/getTagList")
    public Result getTagList(){
        return bookService.getTagList();
    }

    @PostMapping("/getBookListByTag")
    public Result getBookListByTag(Integer tagId,Integer currentPage,Integer pageSize){
        return bookService.getBookListByTag(tagId,currentPage,pageSize);
    }

    @PostMapping("/getBookRankingList")
    public Result getBookRankingList(Integer currentPage,Integer pageSize){
        return bookService.getBookRankingList(currentPage, pageSize);
    }

    //获取搜索表搜索次数前n的内容并返回给前端
    @PostMapping("/getSearchContent")
    public Result getSearchContent(@RequestParam(defaultValue = "0")Integer num){
        return bookService.getSearchContent(num);
    }

    //管理员下架书籍
    //@PreAuthorize("@ex.hasAuthority('system:book:delete')")
    @PreAuthorize("hasAnyAuthority('admin')")
    @DeleteMapping("/withdraw")
    public Result withdraw(Integer bookId){
        return bookService.withdraw(bookId);
    }

    //管理员修改书籍信息
    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("/updateBook")
    public Result updateBook(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("bookId") Integer bookId,
            @RequestParam("bookName") String bookName,
            @RequestParam("bookAuthor") String bookAuthor,
            @RequestParam("bookSummary") String bookSummary,
            @RequestParam("publisher") String publisher,
            @RequestParam("physicalBookPrice") Double physicalBookPrice
    ){
        return bookService.updateBook(file, bookId, bookName, bookAuthor, bookSummary, publisher, physicalBookPrice);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("/addBook")
    public Result addBook(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("bookName") String bookName,
            @RequestParam("bookAuthor") String bookAuthor,
            @RequestParam("bookSummary") String bookSummary,
            @RequestParam("publisher") String publisher,
            @RequestParam("physicalBookPrice") Double physicalBookPrice
    ){
        return bookService.addBook(file, bookName, bookAuthor, bookSummary, publisher, physicalBookPrice);
    }

    @PostMapping("/getBooksByAuthor")
    public Result getBooksByAuthor(String author,Integer currentPage,Integer pageSize){
        return bookService.getBooksByAuthor(author,currentPage,pageSize);
    }

    @PostMapping("/getBooksByBookId")
    public Result getBooksByBookId(Integer bookId,Integer currentPage,Integer pageSize){
        return bookService.getBooksByBookId(bookId,currentPage,pageSize);
    }
}
