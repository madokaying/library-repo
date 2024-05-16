package com.ljh.library_spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChaptersOfBook {
    //epub书籍中获取的章节名
    private String chapter;
    //章节链接
    private String href;
}
