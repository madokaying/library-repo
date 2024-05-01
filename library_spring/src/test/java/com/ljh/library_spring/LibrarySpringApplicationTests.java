package com.ljh.library_spring;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ljh.library_spring.controller.BookController;
import com.ljh.library_spring.entity.Book;
import com.ljh.library_spring.entity.Result;
import com.ljh.library_spring.controller.UserController;
import com.ljh.library_spring.mapper.BookMapper;
import com.ljh.library_spring.mapper.MenuMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
class LibrarySpringApplicationTests {

    @Resource
    private UserController userController;
    @Resource
    private BookMapper bookMapper;
    @Resource
    private BookController bookController;
    @Resource
    private MenuMapper menuMapper;

    @Test
    public void getList(){
        LambdaQueryWrapper<Book> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.ge(Book::getBookId,2);
        List<Book> books = bookMapper.selectList(lambdaQueryWrapper);
        System.out.println(books);
    }

    //乐观锁测试
    @Test
    void testOptimisticLocker_failure() {
        //模拟多线程实现插队效果
        //线程1
        Book book = bookMapper.selectById(1);
        book.setBookName("大卫天龙");
        //线程2
        Book book2 = bookMapper.selectById(1);
        book2.setBookName("大威天龙！");
        bookMapper.updateById(book2);//在这里插队

        bookMapper.updateById(book);//如果没有乐观锁就会覆盖插队线程的值
    }

    //测试MybatisPlus分页插件
    @Test
    public void testMybatisPlus_Page(){
        Result booksList = bookController.getBooksList("s",1, 16);
        System.out.println(booksList);
    }

    /**
     * 测试逻辑删除
     * */
    @Test
    public void logicDeleteTest(){
        int i = bookMapper.deleteById(1);
        System.out.println(i);
    }

    /**
     * 测试插入数据能否自动插入当前时间
     * */
    @Test
    public void insertTest(){
        Book book = new Book();
        book.setBookName("诡秘之主1");
        book.setBookAuthor("乌贼");
        book.setPublisher("北京出版社");
        book.setPhysicalBookPrice(BigDecimal.valueOf(20));
        book.setEbookPrice(BigDecimal.valueOf(2));
        bookMapper.insert(book);
    }

    /**
     * 密码加密测试
     * */
    @Test
    public void BCryptPasswordEncoderTest(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("2024");
        System.out.println(encode);
    }

    /**
     * 测试menu接口
     * */
    @Test
    public void selectPermsByUserIdTest(){
        List<String> strings = menuMapper.selectPermsByUserId(0l);
        System.out.println(strings);
    }

    /**
    * 测试查询
    * */
    @Test
    public void getUserInfoByUIDTest(){
        System.out.println(userController.getUserInfoByUID("7"));
    }
}
