create table tb_book_identifier
(
    tb_book_id    int               not null comment '书籍编号',
    tb_identifier int               not null comment '具体书籍的编号',
    book_state    tinyint default 0 null comment '书籍状态:0为在馆，1为借出，2为卖出',
    delete_flag   tinyint default 0 null comment '逻辑删除:0为未删，1，为已删',
    primary key (tb_book_id, tb_identifier)
)
    comment '每种书下具体的书籍对应的编号';

