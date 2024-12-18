package com.ljh.library_spring.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljh.library_spring.entity.PasswordForm;
import com.ljh.library_spring.entity.Result;
import com.ljh.library_spring.entity.User;
import com.ljh.library_spring.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController{
    @Resource
    private UserService userService;

    //注册
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        userService.register(user);
        return new Result(200,"注册成功");
    }

    //根据用户id获取用户信息
    @RequestMapping("/getUserInfoByUID")
    public Result getUserInfoByUID(String UID){
        Integer Uid = Integer.valueOf(UID);
        return new Result(200,"查询成功",userService.getUserInfoByUID(Uid));
    }

    //设置用户头像
    @PostMapping("/setUserImg/avatar")
    public Result setAvatar(@RequestParam(value = "file") MultipartFile file,@RequestParam(value = "UID") String userId,@RequestParam(value = "fileType") String fileType){
        Integer UID = Integer.valueOf(userId);
        String changeType = "avatar";
        return userService.setUserImg(file,UID,fileType,changeType);
    }

    //设置用户壁纸
    @PostMapping("/setUserImg/background")
    public Result setBackground(@RequestParam(value = "file") MultipartFile file,@RequestParam(value = "UID") String userId,@RequestParam(value = "fileType") String fileType){
        Integer UID = Integer.valueOf(userId);
        String changeType = "background";
        return userService.setUserImg(file,UID,fileType,changeType);
    }

    //用户修改密码
    @PostMapping("/changePassword")
    public Result changePassword(@RequestBody PasswordForm passwordForm){
        return userService.changePassword(passwordForm);
    }

    //用户修改自身信息
    @PostMapping("/changeInfoByUser")
    public Result changeInfoByUser(@RequestBody User user){
        return userService.changeInfoByUser(user);
    }

    //获取用户基础四项数据
    @PostMapping("/getCommonData")
    public Result getCommonData(Integer UID){
        return userService.getCommonData(UID);
    }

    //用户申请借书
    @PostMapping("/borrowBook")
    public Result borrowBook(Integer bookId,Integer userId){
        return userService.borrowBook(bookId,userId);
    }

    //判断是否已经发过借书申请但还没过审或者已经借了该书但还没归还书籍
    @PostMapping("/judgeIsBorrowed")
    public Result judgeIsBorrowed(Integer bookId,Integer userId){
        return userService.judgeIsBorrowed(bookId,userId);
    }

    //获取自身的申请表
    @PostMapping("/getMyBorrowList")
    public Result getMyBorrowList(Integer userId){
        return userService.getMyBorrowList(userId);
    }
}
