package com.ljh.library_spring;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ljh.library_spring.controller.BookController;
import com.ljh.library_spring.entity.TbBook;
import com.ljh.library_spring.entity.Result;
import com.ljh.library_spring.controller.UserController;
import com.ljh.library_spring.mapper.BookMapper;
import com.ljh.library_spring.mapper.MenuMapper;
import nl.siegmann.epublib.domain.*;
import nl.siegmann.epublib.epub.EpubReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

@SpringBootTest
class LibrarySpringApplicationTests {

    @Autowired
    private UserController userController;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private BookController bookController;
    @Autowired
    private MenuMapper menuMapper;

    @Test
    public void getList() {
        LambdaQueryWrapper<TbBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.ge(TbBook::getBookId, 2);
        List<TbBook> tbBooks = bookMapper.selectList(lambdaQueryWrapper);
        System.out.println(tbBooks);
    }

    //乐观锁测试
    @Test
    void testOptimisticLocker_failure() {
        //模拟多线程实现插队效果
        //线程1
        TbBook tbBook = bookMapper.selectById(1);
        tbBook.setBookName("大卫天龙");
        //线程2
        TbBook tbBook2 = bookMapper.selectById(1);
        tbBook2.setBookName("大威天龙！");
        bookMapper.updateById(tbBook2);//在这里插队

        bookMapper.updateById(tbBook);//如果没有乐观锁就会覆盖插队线程的值
    }

    //测试MybatisPlus分页插件
    @Test
    public void testMybatisPlus_Page() {
        Result booksList = bookController.getBooksList("s", 1, 16);
        System.out.println(booksList);
    }

    /**
     * 测试逻辑删除
     */
    @Test
    public void logicDeleteTest() {
        int i = bookMapper.deleteById(1);
        System.out.println(i);
    }

    /**
     * 测试插入数据能否自动插入当前时间
     */
    @Test
    public void insertTest() {
        TbBook tbBook = new TbBook();
        tbBook.setBookName("诡秘之主1");
        tbBook.setBookAuthor("乌贼");
        tbBook.setPublisher("北京出版社");
        tbBook.setPhysicalBookPrice(BigDecimal.valueOf(20));
        tbBook.setEbookPrice(BigDecimal.valueOf(2));
        bookMapper.insert(tbBook);
    }

    /**
     * 密码加密测试
     */
    @Test
    public void BCryptPasswordEncoderTest() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("2024");
        System.out.println(encode);
    }

    /**
     * 测试menu接口
     */
    @Test
    public void selectPermsByUserIdTest() {
        List<String> strings = menuMapper.selectPermsByUserId(0l);
        System.out.println(strings);
    }

    /**
     * 测试查询
     */
    @Test
    public void getUserInfoByUIDTest() {
        System.out.println(userController.getUserInfoByUID("7"));
    }

    @Value("${book.path}")
    private String bookPath;
    @Test
    public void getBookTableOfContentsByIdTest() {
        //合成得到书籍的实际存放路径
        String bookPath = this.bookPath + 45 + ".epub";
        //TODO 解析电子书，获取到内容，返回前端
        EpubReader epubReader = new EpubReader();
        FileInputStream in = null;
        try {
            in = new FileInputStream(bookPath);
            Book book = epubReader.readEpub(in);
            String href = "text00002.html#section";
            Resource resource = book.getResources().getByHref(href);
            byte[] chapter = resource.getData();
            // 将字节数组转换为字符串，假设它是 UTF-8 编码的
            String htmlContent = new String(chapter, StandardCharsets.UTF_8);
            // 将字节数组转换为InputStream，然后使用Jsoup解析
            Document doc = Jsoup.parse(htmlContent, "", org.jsoup.parser.Parser.xmlParser());

            // 提取HTML中的纯文本内容
            String chapterText = doc.body().text(); // 这里提取整个<body>标签内的文本内容，根据需要调整

            System.out.println("章节内容：" + chapterText);
//            InputStream inputStream = new ByteArrayInputStream(charpter);
//            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
//            BufferedReader bufferedReader = new BufferedReader(reader);
//            System.out.println(bufferedReader.readLine());
//            if (resource != null) {
//                byte[] data = resource.getData();
//                // 使用UTF-8编码将字节数组转换为字符串
//                try (InputStream inputStream = new ByteArrayInputStream(data);
//                     InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
//                     BufferedReader bufferedReader = new BufferedReader(reader)) {
//
//                    String line;
//                    while ((line = bufferedReader.readLine()) != null) {
//                        System.out.println(line); // 输出文本数据
//                    }
//                }
//            }
//            Spine spine = book.getSpine();
//            System.out.println("spine资源数量为："+spine.size());
//            //获取到书本的全部资源
//            Resources resources = book.getResources();
//            System.out.println("所有资源数量为："+resources.size());
//            //获取所有的资源数据
//            System.out.println("下面是data数据");
//            Collection<String> allHrefs = resources.getAllHrefs();
//            for (String href : allHrefs) {
//                Resource resource = resources.getByHref(href);
//                byte[] data = resource.getData();
//                // 使用UTF-8编码将字节数组转换为字符串
//                try (InputStream inputStream = new ByteArrayInputStream(data);
//                     InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
//                     BufferedReader bufferedReader = new BufferedReader(reader)) {
//
//                    String line;
//                    while ((line = bufferedReader.readLine()) != null) {
//                        System.out.println(line); // 输出文本数据
//                    }
//
//                } catch (IOException e) {
//                    throw new RuntimeException("Failed to read data from resource", e);
//                }
//            }
            //通过spine获取所有的数据
//            List<SpineReference> spineReferences = spine.getSpineReferences();
//            for (SpineReference spineReference : spineReferences) {
//                Resource resource = spineReference.getResource();
//                //data就是资源的内容数据，可能是css,html,图片等等
//                byte[] data = resource.getData();
//                // 获取到内容的类型  css,html,还是图片
//                MediaType mediaType = resource.getMediaType();
//            }
            //获取到书本的目录资源
            TableOfContents tableOfContents = book.getTableOfContents();
            System.out.println("目录资源数量为："+tableOfContents.size());
            //获取到目录对应的资源数据
            List<TOCReference> tocReferences = tableOfContents.getTocReferences();
            System.out.println("目录资源数据：");
            for (TOCReference tocReference : tocReferences) {
//                Resource resource = tocReference.getResource();
                //data就是资源的内容数据，可能是css,html,图片等等
//                byte[] data = resource.getData();
//                InputStream inputStream = new ByteArrayInputStream(data);
//                InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
//                BufferedReader bufferedReader = new BufferedReader(reader);

                String title = tocReference.getTitle();
                String completeHref = tocReference.getCompleteHref();
                System.out.println(title + " " + completeHref);
                // 获取到内容的类型  css,html,还是图片
//                MediaType mediaType = resource.getMediaType();
//                if(tocReference.getChildren().size()>0){
//                    //获取子目录的内容
//                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                //关闭资源
                if (in != null){
                    in.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
