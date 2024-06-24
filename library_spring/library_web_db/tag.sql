create table tag
(
    tag_id   int auto_increment comment '标签的编号'
        primary key,
    tag_name varchar(8) not null comment '标签名'
)
    comment '书籍的标签';

