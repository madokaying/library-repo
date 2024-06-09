package com.ljh.library_spring.controller;

import com.ljh.library_spring.entity.Result;
import com.ljh.library_spring.entity.TbBorrow;
import com.ljh.library_spring.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    //获取借阅列表
    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("/getBorrowList")
    public Result getBorrowList()
    {
        return adminService.getBorrowList();
    }

    //审批借阅申请
    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("/updateBorrow")
    public Result updateBorrow(@RequestBody TbBorrow tbBorrow)
    {
        return adminService.updateBorrow(tbBorrow);
    }

    //获取所有的用户列表
    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("/getUserList")
    public Result getUserList(Integer userId,Integer currentPage,Integer pageSize)
    {
        return adminService.getUserList(userId,currentPage,pageSize);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @DeleteMapping("/banUser")
    public Result banUser(Integer userId)
    {
        return adminService.banUser(userId);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("/updateUser")
    public Result updateUser(@RequestParam("id") Integer id,
                             @RequestParam("realName") String realName,
                             @RequestParam("address") String address,
                             @RequestParam("phoneNumber") String phoneNumber,
                             @RequestParam("idCardNumber") String idCardNumber
    )
    {
        return adminService.updateUser(id,realName,address,phoneNumber,idCardNumber);
    }
}
