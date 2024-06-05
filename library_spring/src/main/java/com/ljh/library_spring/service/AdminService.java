package com.ljh.library_spring.service;

import com.ljh.library_spring.entity.Result;
import com.ljh.library_spring.entity.TbBorrow;

public interface AdminService {
    Result getBorrowList();

    Result updateBorrow(TbBorrow tbBorrow);
}
