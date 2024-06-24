create table comment
(
    comment_id           int auto_increment comment '评论列表'
        primary key,
    content              varchar(600)      not null comment '评论内容',
    created_time         datetime          null comment '评论时间',
    commenting_target_id int               not null comment '评论对象的id',
    user_id              bigint            not null comment '评论用户id',
    commenting_target    tinyint           not null comment '评论对象:1、对书的评论    2、对帖子的评论     3、对用户的评论（回复）',
    delete_flag          tinyint default 0 not null comment '逻辑删除：0 未删/ 1 删除'
)
    comment '评论';

