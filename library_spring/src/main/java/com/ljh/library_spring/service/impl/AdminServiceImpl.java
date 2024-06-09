package com.ljh.library_spring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljh.library_spring.entity.*;
import com.ljh.library_spring.mapper.BookMapper;
import com.ljh.library_spring.mapper.TbBorrowMapper;
import com.ljh.library_spring.mapper.UserMapper;
import com.ljh.library_spring.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    public Result getBorrowList() {
        //获取申请列表
        LambdaQueryWrapper<TbBorrow> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(TbBorrow::getDeleteFlag, 0,1);
        List<TbBorrow> tbBorrowList = tbBorrowMapper.selectList(lambdaQueryWrapper);
        List<TbBorrowWrapper> list = new ArrayList<>();
        //将TbBorrow封装为tbBorrowWrapper
        for (TbBorrow tbBorrow : tbBorrowList){
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
            //封装上此书下馆内仍有之书的编号
            tbBorrowWrapper.setBookIdentifierList(bookMapper.getIdentifierOfBook(tbBorrow.getBookId()));
            list.add(tbBorrowWrapper);
        }
        return new Result(200,"获取申请列表成功",list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateBorrow(TbBorrow tbBorrow) {
        //先判断管理员进行的操作是什么
        if (tbBorrow.getState() == 1){
            //更新借书表
            tbBorrow.setCheckedTime(LocalDateTime.now());
            tbBorrow.setDeleteFlag(1);
            tbBorrowMapper.updateById(tbBorrow);
            User user = userMapper.selectById(tbBorrow.getUserId());
            //用户最大可借书数-1
            user.setMaxBorrow(user.getMaxBorrow()-1);
            userMapper.updateById(user);
            //更新具体书的状态
            bookMapper.updateBookIdentifier(tbBorrow.getBookId(),tbBorrow.getBookIdentifier(),1);
            //书的借阅次数+1
            LambdaUpdateWrapper<TbBook> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            TbBook tbBook = bookMapper.selectById(tbBorrow.getBookId());
            lambdaUpdateWrapper.eq(TbBook::getBookId,tbBorrow.getBookId())
                            .set(TbBook::getBorrowedTimes,tbBook.getBorrowedTimes() + 1);
            bookMapper.update(lambdaUpdateWrapper);
            return new Result(200,"借书处理成功");
        } else if (tbBorrow.getState() == 2){
            //更新借书表
            tbBorrow.setCheckedTime(LocalDateTime.now());
            tbBorrow.setDeleteFlag(1);
            tbBorrowMapper.updateById(tbBorrow);
            return new Result(200,"拒绝处理成功");
        } else if(tbBorrow.getState() == 3){
            //处理还书的逻辑
            //更新借书表
            tbBorrow.setCheckedTime(LocalDateTime.now());
            tbBorrow.setDeleteFlag(1);
            tbBorrowMapper.updateById(tbBorrow);
            User user = userMapper.selectById(tbBorrow.getUserId());
            //用户最大可借书数+1，并且修改用户的needToPay
            user.setMaxBorrow(user.getMaxBorrow()+1);
            LocalDateTime now = LocalDateTime.now();
            long daysBetween = ChronoUnit.DAYS.between(tbBorrow.getCheckedTime(), now);
            user.setNeedToPay((int) (user.getNeedToPay() + daysBetween * 0.5));
            userMapper.updateById(user);
            //更新具体书的状态
            bookMapper.updateBookIdentifier(tbBorrow.getBookId(),tbBorrow.getBookIdentifier(),0);
            return new Result(200,"还书处理成功成功");
        } else {
            return new Result(400,"未知错误");
        }
    }

    @Override
    public Result getUserList(Integer userId, Integer currentPage, Integer pageSize) {
        Page<User> page = new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (userId != null){
            lambdaQueryWrapper.eq(User::getId,userId)
                    .eq(User::getDeleted,0);
        } else {
            lambdaQueryWrapper.eq(User::getDeleted,0);
        }
        userMapper.selectPage(page,lambdaQueryWrapper);
        return new Result(200,"获取用户列表成功",page);
    }

    @Override
    public Result banUser(Integer userId) {
        if (userMapper.deleteById(userId) == 1){
            return new Result(200,"封禁用户成功");
        }else{
            return new Result(400,"未知错误");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result updateUser(Integer id, String realName, String address, String phoneNumber, String idCardNumber) {
        //首先更新用户实名信息
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getId,id);
        User user = userMapper.selectById(id);
        user.setRealName(realName);
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);
        user.setIdCardNumber(idCardNumber);
        userMapper.update(user,lambdaUpdateWrapper);
        //修改角色为已实名用户
        userMapper.setAuthenticatedUser(id);
        return new Result(200,"用户实名认证成功");
    }
}
