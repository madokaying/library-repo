package com.ljh.library_spring.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbBorrowWrapper {
    private TbBorrow tbBorrow;
    private String bookName;
    private String bookCover;
    private String userNickname;
    private String userAvatar;
}
