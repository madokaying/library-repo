package com.ljh.library_spring.service;

import com.ljh.library_spring.entity.Result;
import com.ljh.library_spring.entity.User;

public interface LoginServcie {
    public Result login(User user);
    public Result logout();
}
