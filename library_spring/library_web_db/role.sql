create table role
(
    id          bigint auto_increment
        primary key,
    name        varchar(128)     null,
    role_key    varchar(100)     null comment '角色权限字符串',
    status      char default '0' null comment '角色状态（0正常 1停用）',
    del_flag    int  default 0   null comment 'del_flag',
    create_by   bigint           null,
    create_time datetime         null,
    update_by   bigint           null,
    update_time datetime         null,
    remark      varchar(500)     null comment '备注'
)
    comment '角色表';

