create table post
(
    post_id      int auto_increment comment '帖子id'
        primary key,
    post_title   varchar(64)       not null comment '帖子标题',
    post_content varchar(2000)     not null comment '帖子内容',
    post_image   varchar(255)      null comment '帖子图片',
    created_time datetime          null comment '发帖时间',
    owner_id     int               not null comment '楼主id',
    delete_flag  tinyint default 0 not null comment '逻辑删除:0未删，1已删'
)
    comment '帖子';

