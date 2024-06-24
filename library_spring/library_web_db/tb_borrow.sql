create table tb_borrow
(
    borrow_id       int auto_increment comment '借书申请id'
        primary key,
    user_id         int               not null comment '借书用户的id',
    book_id         int               not null comment '所借书籍的编号',
    book_identifier int               null comment '书籍具体编号',
    state           tinyint default 0 null comment '状态：0为默认申请的状态，1为审批通过，书属于借出状态、2为审批不通过 、3为审批通过且书已归还',
    created_time    datetime          null comment '申请发起时间',
    checked_time    datetime          null comment '管理员审批完成时间',
    delete_flag     tinyint default 0 null comment '逻辑删除 0未删 1已删'
)
    comment '借书表';

