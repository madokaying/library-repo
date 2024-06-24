create table role_menu
(
    role_id bigint auto_increment comment '角色ID',
    menu_id bigint default 0 not null comment '菜单id',
    primary key (role_id, menu_id)
);

