package com.ljh.library_spring.controller;

import com.ljh.library_spring.entity.Result;
import com.ljh.library_spring.entity.User;
import com.ljh.library_spring.service.LoginServcie;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class LoginController {

    @Resource
    private LoginServcie loginServcie;

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        return loginServcie.login(user);
    }

    @RequestMapping("/logout")
    public Result logout() {
        return loginServcie.logout();
    }

    //给前端访问判断用户登录是否还合法的,该接口需要用户认证了才能访问，要是token不合法会返回401未认证信息
    @RequestMapping("/testToken")
    public Result testToken() {
        return new Result(200, "token合法");
    }
}
