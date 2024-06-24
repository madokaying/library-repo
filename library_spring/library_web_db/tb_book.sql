create table tb_book
(
    book_id             int auto_increment comment '同名图书的编号，作为主键，一种书一个编号，与另一张表（保存每本书的序列号）是一对多的关系，用逻辑外键联系，一种书有多本书，每本书都有一个唯一的序列号
一种书只有一个id，一本书下的每本书都有不同的序列号'
        primary key,
    book_name           varchar(12)                       not null comment '书名',
    book_author         varchar(20)                       not null comment '作者',
    book_summary        varchar(255) default '暂无简介'   null comment '简介/内容摘要',
    publisher           varchar(12)  default '暂无出版社' null comment '出版社',
    physical_book_price decimal(5, 2) unsigned            not null comment '实体书价格，五位数，其中两位为小数',
    book_cover          varchar(120) default ''           null comment '图书的封面',
    borrowed_times      int          default 0            null comment '借阅次数',
    version             tinyint      default 1            null comment '乐观锁',
    deleted             tinyint      default 0            null comment '逻辑删除，0.未删 1.已逻辑删除',
    create_time         datetime                          null comment '创建时间',
    update_time         datetime                          null comment '修改时间',
    constraint book_book_name_uindex
        unique (book_name)
)
    comment '存放图书信息';

