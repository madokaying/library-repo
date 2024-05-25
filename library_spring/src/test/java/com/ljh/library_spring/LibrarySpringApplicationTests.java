package com.ljh.library_spring;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ljh.library_spring.controller.BookController;
import com.ljh.library_spring.entity.*;
import com.ljh.library_spring.controller.UserController;
import com.ljh.library_spring.mapper.BookMapper;
import com.ljh.library_spring.mapper.CommentMapper;
import com.ljh.library_spring.mapper.MenuMapper;
import com.ljh.library_spring.mapper.UserMapper;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SpringBootTest
class LibrarySpringApplicationTests {

    @Autowired
    private UserController userController;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private BookController bookController;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private CommentMapper commentMapper;

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

    @Test
    public void TestParentComment(){
        /*
         思路：
          1.只获取一级评论，通过一级评论的评论者id，获取到评论者的头像和昵称，再通过评论id获取到有没有子评论，有的话有几条，封装到成一个
          实体类，以列表的形式返回给前端
          2.前端若点击某评论的查看回复，则通过评论id获取到其底下所有的子评论，并通过子评论的评论者id，获取到评论者的头像和昵称，通过子
          评论评论对象的id获取到被评论者的id再获取被评论者的昵称，封装到子评论实体类中，再封装到一级评论实体类中，再返回给前端
         */
//        String commentTargetType = null;
//        String commentTargetId = null;
//        Integer currentPage = null;
//        Integer pageSize = null;

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getCommentingTarget,1)
                .eq(Comment::getCommentingTargetId,45)
                .orderByDesc(Comment::getCreatedTime);
        //获取到45编号书籍下的所有一级评论
        List<Comment> list = commentMapper.selectList(queryWrapper);
        List<ParentComment> parentCommentList = new ArrayList<>();
        for (Comment comment : list) {
            //children为二级评论
            LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Comment::getCommentingTarget,3)
                    .eq(Comment::getCommentingTargetId,comment.getCommentId());
            List<Comment> children = commentMapper.selectList(lambdaQueryWrapper);
            User user = userMapper.selectById(comment.getUserId());
            if (user!=null){
                ParentComment parentComment = new ParentComment();
                parentComment.setComment(comment);
                parentComment.setUserNickname(user.getNickname());
                parentComment.setUserAvatar(user.getAvatar());
                List<Comment> allChildrenList = new ArrayList<>(children);
                if (!children.isEmpty()){
                    //用递归得到二级评论下的所有评论,放到ParentComment的ChildrenCommentList中
                    List<ChildrenComment> childrenCommentList = TestChildrenComment(children,allChildrenList);
                    parentComment.setChildrenCommentLength(childrenCommentList.size());
                    parentComment.setChildrenCommentList(childrenCommentList);
                } else {
                    parentComment.setChildrenCommentLength(0);
                    parentComment.setChildrenCommentList(null);
                }
                parentCommentList.add(parentComment);
            }
        }
        System.out.println("查询结果如下：");
        System.out.println(parentCommentList);
    }

    //children为某一一级评论下的二级评论
    //children用于查询，allChildrenList用于返回总结果
    public List<ChildrenComment> TestChildrenComment(List<Comment> children,List<Comment> allChildrenList){
        //通过递归获取到所有子评论，通过子评论的评论者id获取到评论者的头像和昵称，通过评论对象的id获取到被评论者的id进而获取到被评论者的昵称，封装到childrenComment中，最后返回这样的一个list
        //获取当前级子评论的所有子评论，用于下一次递归查找
        //如果children不为空，则递归继续找子评论，若不为空说明所有的子评论全部找到了，则按需求封装到List<ChildrenComment>中返回
        List<Comment> list = new ArrayList<>();
        if (!children.isEmpty()) {
            for (Comment comment:children){
                LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(Comment::getCommentingTarget,3)
                        .eq(Comment::getCommentingTargetId,comment.getCommentId());
                List<Comment> lowerChildren =commentMapper.selectList(lambdaQueryWrapper);
                list.addAll(lowerChildren);
                //将每个子评论下的下一级所有子评论加入到allChildrenList中
                allChildrenList.addAll(lowerChildren);
                System.out.println("测试数据");
            }
            System.out.println("测试数据");
            return TestChildrenComment(list,allChildrenList);
        } else {
            List<ChildrenComment> childrenCommentList = new ArrayList<>();
            for(Comment comment:allChildrenList){
                ChildrenComment childrenComment = new ChildrenComment();
                //获取评论用户的头像和昵称
                User user = userMapper.selectById(comment.getUserId());
                //获取被评论者的昵称
                LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(Comment::getCommentingTarget,3)
                        .eq(Comment::getCommentId,comment.getCommentingTargetId());
                Comment commentator = commentMapper.selectOne(lambdaQueryWrapper);
                if (user!=null){
                    childrenComment.setComment(comment);
                    childrenComment.setUserNickname(user.getNickname());
                    childrenComment.setUserAvatar(user.getAvatar());
                    if (commentator!=null){
                        User commentatorUser = userMapper.selectById(commentator.getUserId());
                        if (commentatorUser!=null){
                            childrenComment.setCommentTargetUserNickname(commentatorUser.getNickname());
                        }
                    }
                }
                childrenCommentList.add(childrenComment);
            }
            return childrenCommentList;
        }
    }
}
