create table tb_search
(
    search_id      int auto_increment comment '搜索id',
    search_content varchar(32)   not null comment '搜索内容',
    search_times   int default 1 null comment '搜索次数',
    primary key (search_id, search_content)
)
    comment '搜索表';

