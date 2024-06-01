package com.ljh.library_spring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljh.library_spring.entity.*;
import com.ljh.library_spring.mapper.BookMapper;
import com.ljh.library_spring.mapper.TbBorrowMapper;
import com.ljh.library_spring.mapper.UserMapper;
import com.ljh.library_spring.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private TbBorrowMapper tbBorrowMapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private UserMapper userMapper;
    @Override
    public Result getBorrowList(Integer currentPage, Integer pageSize) {
        //获取申请列表
        Page<TbBorrow> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<TbBorrow> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(TbBorrow::getState, 0)
                .eq(TbBorrow::getDeleteFlag, 0);
        tbBorrowMapper.selectPage(page, lambdaQueryWrapper);
        List<TbBorrowWrapper> list = new ArrayList<>();
        //将TbBorrow封装为tbBorrowWrapper
        for (TbBorrow tbBorrow : page.getRecords()){
            TbBorrowWrapper tbBorrowWrapper = new TbBorrowWrapper();
            tbBorrowWrapper.setTbBorrow(tbBorrow);
            //获取借的书的书名封面
            TbBook book = bookMapper.selectById(tbBorrow.getBookId());
            tbBorrowWrapper.setBookName(book.getBookName());
            tbBorrowWrapper.setBookCover(book.getBookCover());
            //获取借书用户的头像昵称
            User user = userMapper.selectById(tbBorrow.getUserId());
            tbBorrowWrapper.setUserNickname(user.getNickname());
            tbBorrowWrapper.setUserAvatar(user.getAvatar());
            list.add(tbBorrowWrapper);
        }
        return new Result(200,"获取申请列表成功",list);
    }
}
