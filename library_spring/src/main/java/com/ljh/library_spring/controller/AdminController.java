package com.ljh.library_spring.controller;

import com.ljh.library_spring.entity.Result;
import com.ljh.library_spring.service.AdminService;
import com.ljh.library_spring.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/getBorrowList")
    public Result getBorrowList(Integer currentPage, Integer pageSize)
    {
        return adminService.getBorrowList(currentPage,pageSize);
    }
}
