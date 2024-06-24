create table menu
(
    id          bigint auto_increment
        primary key,
    menu_name   varchar(64)  default 'NULL' not null comment '菜单名',
    path        varchar(200)                null comment '路由地址',
    component   varchar(255)                null comment '组件路径',
    visible     char         default '0'    null comment '菜单状态（0显示 1隐藏）',
    status      char         default '0'    null comment '菜单状态（0正常 1停用）',
    perms       varchar(100)                null comment '权限标识',
    icon        varchar(100) default '#'    null comment '菜单图标',
    create_by   bigint                      null,
    create_time datetime                    null,
    update_by   bigint                      null,
    update_time datetime                    null,
    del_flag    int          default 0      null comment '是否删除（0未删除 1已删除）',
    remark      varchar(500)                null comment '备注'
)
    comment '菜单表';

