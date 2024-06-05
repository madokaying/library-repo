package com.ljh.library_spring.controller;

import com.ljh.library_spring.entity.Result;
import com.ljh.library_spring.entity.TbBorrow;
import com.ljh.library_spring.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    //获取借阅列表
    @PostMapping("/getBorrowList")
    public Result getBorrowList()
    {
        return adminService.getBorrowList();
    }

    //审批借阅申请
    @PostMapping("/updateBorrow")
    public Result updateBorrow(@RequestBody TbBorrow tbBorrow)
    {
        return adminService.updateBorrow(tbBorrow);
    }
}
