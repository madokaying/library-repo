package com.ljh.library_spring.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljh.library_spring.entity.Result;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface BookService {

    /**
     * 获取书籍列表的分页数据。
     *
     * @param bookName 指定的书籍名称，可以为空，为空时查询所有书籍。
     * @param currentPage 当前页码，用于分页查询，默认为第1页。
     * @param pageSize 每页显示的记录数，默认为12条记录。
     * @return 返回书籍列表的分页对象，包含当前页的书籍数据。
     */
    public Page getBooksList(String bookName, Integer currentPage, Integer pageSize);


    /**
     * 根据书籍ID获取书籍详情。
     *
     * @param bookId 书籍的唯一标识符。
     * @return Result 包含书籍详情的对象。如果找不到书籍，可能返回一个错误码。
     */
    Result getBookDetailById(Integer bookId);

    /**
     * 根据书籍ID获取书籍的目录内容。
     *
     * @param bookId 书籍的唯一标识符。
     * @return Result 包含书籍目录内容的对象。
     * @throws IOException 如果在获取目录内容过程中发生IO异常。
     */
    Result getBookTableOfContentsById(Integer bookId) throws IOException;


    /**
     * 根据书的链接获取对应的章节内容。
     *
     * @param href 书籍章节的链接，用于定位特定章节的信息。
     * @return Result 包含章节内容的对象，如果失败可能返回错误信息。
     */
    Result getBookChapterByHref(String href,Integer bookId);

    /**
     * 根据书籍ID获取书籍的标签。
     *
     * @param bookId 书籍的唯一标识符。
     * @return Result 包含书籍标签信息的结果对象。成功时，结果对象中包含书籍的标签信息；失败时，结果对象中包含错误信息。
     */
    Result getTagsOfBookById(Integer bookId);

    Result collectBook(Integer bookId, Integer userId);

    Result cancelCollectBook(Integer bookId, Integer userId);

    Result isCollectedBook(Integer bookId, Integer userId);

    Result getMyCollectBooks(Integer userId);

    Result getTagList();

    Result getBookListByTag(Integer tagId, Integer currentPage, Integer pageSize);

    Result getBookRankingList(Integer currentPage, Integer pageSize);

    Result getSearchContent(Integer num);

    Result getBookCollectedNumber(Integer bookId);
}
