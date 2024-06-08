package com.ljh.library_spring.service.impl;

import ch.qos.logback.classic.Logger;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljh.library_spring.entity.*;
import com.ljh.library_spring.mapper.BookMapper;
import com.ljh.library_spring.mapper.TbSearchMapper;
import com.ljh.library_spring.service.BookService;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.domain.TableOfContents;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.domain.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
    @Autowired
    private TbSearchMapper tbSearchMapper;

    /**
     * 存储图像文件的源路径。
     * 同样通过@Value注解从应用配置中动态加载值，强调了配置管理的灵活性。
     * 这个变量用于标识图像文件在服务器上的存储位置，便于后端代码访问和处理这些文件。
     */
    @Value("${upload.path}")
    private String imgURL;

    /**
     * 存储图像文件的URL路径。
     * 通过@Value注解从应用配置中动态加载值，确保了配置的灵活性和可维护性。
     * 这个变量的作用是提供图像文件在服务器上的访问路径，以便在前端页面上展示这些图像。
     */
    @Value("${upload.src}")
    private String imgSRC;


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
            /*
            *新增功能，若搜索不为空，即认为是有用户搜索查询，则此处判断搜索内容是否已经在搜索表内存在，存在则对应内容搜索次数+1
            * 若不存在，则添加一条记录，并设置搜索次数为1
            * */
            LambdaQueryWrapper<TbSearch> lambdaQueryWrapperTbSearch = new LambdaQueryWrapper<>();
            lambdaQueryWrapperTbSearch.eq(TbSearch::getSearchContent,bookName);
            if (tbSearchMapper.selectOne(lambdaQueryWrapperTbSearch) != null){
                TbSearch tbSearch = tbSearchMapper.selectOne(lambdaQueryWrapperTbSearch);
                LambdaUpdateWrapper<TbSearch> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(TbSearch::getSearchId,tbSearch.getSearchId())
                        .set(TbSearch::getSearchTimes,tbSearch.getSearchTimes()+1);
                tbSearchMapper.update(lambdaUpdateWrapper);
            }
            else {
                TbSearch tbSearch = new TbSearch();
                tbSearch.setSearchContent(bookName);
                tbSearchMapper.insert(tbSearch);
            }
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

    public Result getTagList() {
        List<Tag> tagList = bookMapper.getTagList();
        if (tagList != null){
            return new Result<>(200,"获取标签列表成功",tagList);
        }
        return new Result(400,"获取标签列表失败，请联系管理员");
    }

    public Result getBookListByTag(Integer tagId, Integer currentPage, Integer pageSize) {
        if (tagId != null){
            Integer start = (currentPage - 1) * pageSize;
            List<TbBook> list = bookMapper.getBookListByTag(tagId,start,pageSize);
            return new Result(200,"获取标签下的图书成功",list);
        }
        return new Result(400,"获取标签下的图书失败，请联系管理员");
    }

    public Result getBookRankingList(Integer currentPage, Integer pageSize) {
        Integer start = (currentPage - 1) * pageSize;
        List<TbBook> list = bookMapper.getBookRankingList(start, pageSize);
        return new Result(200,"获取图书排行榜成功",list);
    }

    public Result getSearchContent(Integer num) {
        //如果num为0，则返回搜索表所有的内容
        //获取TbSearch内数据的总数
        List<TbSearch> searchList = tbSearchMapper.selectList(null);
        if (num > searchList.size()) {
            num = searchList.size();
        }
        return new Result(200,"获取搜索内容成功",tbSearchMapper.getSearchContent(num));
    }

    public Result getBookCollectedNumber(Integer bookId) {
        return new Result(200,"获取图书收藏人数成功",bookMapper.getBookCollectedNumber(bookId));
    }

    @Override
    public Result withdraw(Integer bookId) {
        if (bookMapper.deleteById(bookId) == 1){
            return new Result(200,"删除图书成功");
        }
        return new Result<>(501,"删除图书失败，请联系管理员");
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result updateBook(MultipartFile file, Integer bookId, String bookName, String bookAuthor, String bookSummary, String publisher, Double physicalBookPrice) {
        /*
         * 通过book的bookId获取到tbBook，进而得到cover地址，若地址存在文件则删除，然后将图片存到原cover的位置（同名）
        * */
        LambdaUpdateWrapper<TbBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TbBook::getBookId,bookId)
                .set(TbBook::getBookName,bookName)
                .set(TbBook::getBookAuthor,bookAuthor)
                .set(TbBook::getBookSummary,bookSummary)
                .set(TbBook::getPublisher,publisher)
                .set(TbBook::getPhysicalBookPrice,physicalBookPrice);
        int result = bookMapper.update(wrapper);
        if (result == 0){
            return new Result(400,"更新图书失败");
        } else {
            //return new Result(200,"更新图书成功");
            if (file != null){
                String oldCover = bookMapper.selectById(bookId).getBookCover().replaceAll(imgSRC,imgURL);
                File coverFile = new File(oldCover);
                //若该路径下的是文件且存在，则删除
                if (coverFile.isFile() && coverFile.exists()){
                    coverFile.delete();
                }
                //将文件保存指定目录
                try{
                    file.transferTo(new File(oldCover));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return new Result(200,"更新图书成功");
        }
    }
}
