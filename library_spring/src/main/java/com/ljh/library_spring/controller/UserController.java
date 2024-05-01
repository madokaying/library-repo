package com.ljh.library_spring.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    @PostMapping("/register")
    public Result register(@RequestBody User user){
        userService.register(user);
        return new Result(200,"注册成功");
    }

    @RequestMapping("/getUserInfoByUID")
    public Result getUserInfoByUID(String UID){
        Integer Uid = Integer.valueOf(UID);
        return new Result(200,"查询成功",userService.getUserInfoByUID(Uid));
    }

    @PostMapping("/setUserImg/avatar")
    public Result setAvatar(@RequestParam(value = "file") MultipartFile file,@RequestParam(value = "UID") String userId,@RequestParam(value = "fileType") String fileType){
        Integer UID = Integer.valueOf(userId);
        String changeType = "avatar";
        return userService.setUserImg(file,UID,fileType,changeType);
    }

    @PostMapping("/setUserImg/background")
    public Result setbackground(@RequestParam(value = "file") MultipartFile file,@RequestParam(value = "UID") String userId,@RequestParam(value = "fileType") String fileType){
        Integer UID = Integer.valueOf(userId);
        String changeType = "background";
        return userService.setUserImg(file,UID,fileType,changeType);
    }
}
