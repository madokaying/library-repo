package com.ljh.library_spring.service.impl;

import ch.qos.logback.classic.Logger;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljh.library_spring.entity.ChaptersOfBook;
import com.ljh.library_spring.entity.TbBook;
import com.ljh.library_spring.entity.Result;
import com.ljh.library_spring.mapper.BookMapper;
import com.ljh.library_spring.service.BookService;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.domain.TableOfContents;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.domain.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BookServiceImpl implements BookService {
    @Value("${book.path}")
    private String bookPath;
    @Autowired
    private BookMapper bookMapper;

    public Page getBooksList(String bookName, Integer currentPage, Integer pageSize) {
/*        使用mybatis-plus的分页插件，内含records（分页查询返回的列表）,"total": 总记录数,
        "size": 每页记录数,"current": 当前页,"pages":总页数 */
        Page<TbBook> page = new Page<>(currentPage, pageSize);
        if (Objects.equals(bookName, "")) {
            bookMapper.selectPage(page, null);
        } else {
            LambdaQueryWrapper<TbBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.like(TbBook::getBookName, bookName);
            bookMapper.selectPage(page, lambdaQueryWrapper);
        }
        return page;
    }

    //通过图书id获取图书详细信息
    public Result getBookDetailById(Integer bookId) {
        LambdaQueryWrapper<TbBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(TbBook::getBookId,bookId);
        TbBook tbBookDetail = bookMapper.selectOne(lambdaQueryWrapper);
        if (tbBookDetail != null){
            return new Result(200,"获取图书信息成功", tbBookDetail);
        }
        return new Result(501,"获取图书信息失败，请联系管理员解决");
    }

    //将书籍的目录以及目录链接传给前端
    public Result getBookTableOfContentsById(Integer bookId) throws IOException {
        // 合成得到书籍的实际存放路径
        String bookFilePath = this.bookPath + bookId + ".epub";

        try (FileInputStream in = new FileInputStream(bookFilePath)) {
            EpubReader epubReader = new EpubReader();
            Book book = epubReader.readEpub(in);
            TableOfContents tableOfContents = book.getTableOfContents();
            List<ChaptersOfBook> chapters = createChapters(tableOfContents);
            return new Result(200, "获取图书目录成功", chapters);
        } catch (IOException e) {
            return new Result(501, "获取图书目录失败，请联系管理员解决");
        }
    }
    /**
     * 根据提供的目录信息（TableOfContents）创建并返回一个包含各章信息（ChaptersOfBook）的列表。
     *
     * @param tableOfContents 包含目录引用的TableOfContents对象。
     * @return 包含从tableOfContents解析出的各章信息的List。
     */
    private List<ChaptersOfBook> createChapters(TableOfContents tableOfContents) {
        // 初始化一个空列表来存储章节信息
        List<ChaptersOfBook> chapters = new ArrayList<>();
        // 遍历tocReferences，为每一项创建一个ChaptersOfBook对象并添加到列表中
        for (TOCReference tocReference : tableOfContents.getTocReferences()) {
            String title = tocReference.getTitle();
            String completeHref = tocReference.getCompleteHref();
            // 创建ChaptersOfBook对象并填充标题和完整href
            ChaptersOfBook chaptersOfBook = new ChaptersOfBook(title, completeHref);
            chapters.add(chaptersOfBook);
        }
        return chapters;
    }

    public Result getBookChapterByHref(String href,Integer bookId) {
        if (href != null) {
            // 合成得到书籍的实际存放路径
            String bookFilePath = this.bookPath + bookId + ".epub";

            try (FileInputStream in = new FileInputStream(bookFilePath)) {
                EpubReader epubReader = new EpubReader();
                Book book = epubReader.readEpub(in);
                Resource resource = book.getResources().getByHref(href);
                if (resource != null){
                    String chapterContent = new String(resource.getData());
                    return new Result(200, "获取章节内容成功",chapterContent);
                } else {
                    return new Result(502, "该章节不存在");
                }
            } catch (IOException e) {
                return new Result(501, "获取图书章节内容失败，请联系管理员解决");
            }
        }
        return null;
    }

    public Result getTagsOfBookById(Integer bookId) {
        List<String> tags = bookMapper.getTagsOfBookById(bookId);
        return new Result(200,"获取图书标签成功",tags);
    }

    public Result collectBook(Integer bookId, Integer userId) {
        if (bookMapper.collectBook(bookId,userId) == 1){
            return new Result(200,"收藏成功");
        }
        return new Result(400,"收藏失败，请联系管理员");
    }

    public Result cancelCollectBook(Integer bookId, Integer userId) {
        if (bookMapper.cancelCollectBook(bookId,userId) == 1){
            return new Result(200,"取消收藏成功");
        }
        return new Result(400,"取消收藏失败，请联系管理员");
    }

    public Result isCollectedBook(Integer bookId, Integer userId) {
        if (bookMapper.judgeBookIsCollected(bookId,userId) == 1){
            return new Result(200,"已收藏");
        }
        return new Result(400,"未收藏");
    }

    public Result getMyCollectBooks(Integer userId) {
        List<TbBook> myCollectBooks = bookMapper.getMyCollectBooks(userId);
        return new Result<>(200,"获取我的收藏成功",myCollectBooks);
    }


}
