package com.ljh.library_spring.service;

import com.ljh.library_spring.entity.Result;
import com.ljh.library_spring.entity.TbBorrow;

public interface AdminService {
    Result getBorrowList();

    Result updateBorrow(TbBorrow tbBorrow);

    Result getUserList(Integer userId, Integer currentPage, Integer pageSize);

    Result banUser(Integer userId);

    Result updateUser(Integer id, String realName, String address, String phoneNumber, String idCardNumber);
}
