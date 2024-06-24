create table book_tag
(
    book_id int not null comment '书籍的编号',
    tag_id  int not null comment '标签的编号',
    primary key (book_id, tag_id)
)
    comment '书籍和标签的关联表';

