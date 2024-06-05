package com.ljh.library_spring.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbBorrowWrapper {
    private TbBorrow tbBorrow;
    private String bookName;
    private String bookCover;
    private String userNickname;
    private String userAvatar;
    private List<Integer> bookIdentifierList;//tbBorrow中bookId对应的书籍下所有在馆可借的书籍的具体编号
}
