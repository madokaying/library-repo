create table user_role
(
    user_id bigint auto_increment comment '用户id',
    role_id tinyint default 1 not null comment '角色id',
    primary key (user_id, role_id)
);

