create table tb_collection
(
    book_id int not null comment '书籍编号',
    user_id int not null comment '用户编号',
    primary key (book_id, user_id)
)
    comment '用户书籍收藏表';

