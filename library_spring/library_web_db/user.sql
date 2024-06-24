create table user
(
    id             bigint auto_increment comment '主键/编号'
        primary key,
    username       varchar(15)                                                                   null comment '用户名/账号',
    nickname       varchar(12)  default '书客β'                                                  null comment '昵称',
    password       varchar(256)                                                                  not null comment '密码',
    real_name      varchar(6)                                                                    null comment '真实姓名',
    email          varchar(64)                                                                   null comment '邮箱',
    address        varchar(64)                                                                   null comment '住址',
    id_card_number varchar(32)                                                                   null comment '身份证号码',
    avatar         varchar(128) default 'http://localhost:8080/static/img/defaultAvatar.jpg'     null comment '头像',
    background     varchar(128) default 'http://localhost:8080/static/img/defaultBackground.jpg' null comment '个性壁纸',
    signature      varchar(64)                                                                   null comment '个性签名',
    phone_number   varchar(11)                                                                   null,
    max_borrow     tinyint      default 5                                                        null comment '最多可借书数',
    need_to_pay    int          default 0                                                        null comment '待付金额',
    state          char         default '1'                                                      null comment '状态：1.可用 2.停用/禁用',
    version        tinyint      default 1                                                        null comment '乐观锁',
    deleted        tinyint      default 0                                                        null comment '逻辑删除：
0.未删
1.已删',
    create_time    datetime                                                                      null comment '创建时间',
    update_time    datetime                                                                      null comment '修改时间'
);

