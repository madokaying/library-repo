package com.ljh.library_spring.service;

import com.ljh.library_spring.entity.Result;

public interface AdminService {
    Result getBorrowList(Integer currentPage, Integer pageSize);
}
