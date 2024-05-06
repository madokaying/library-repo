package com.ljh.library_spring.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2024-03-30 16:19:16
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends Model<User> {
    //主键/编号
    @TableId(type = IdType.AUTO)
    private Long id;
    //用户名/账号
    private String username;
    //昵称
    private String nickname;
    //密码
    private String password;
    //真实姓名
    private String realName;
    //邮箱
    private String email;
    //住址
    private String address;
    //身份证号码
    private String idCardNumber;
    //头像
    private String avatar;
    //个性壁纸
    private String background;
    //个性签名
    private String signature;
    //电话号码
    private String phoneNumber;
    //最多可借书数
    private Integer maxBorrow;
    //待付金额
    private Integer needToPay;
    //状态：1.可用 2.停用/禁用
    private String state;
    @Version//乐观锁version注解
    private Integer version;
/*   逻辑删除：
     0.未删
     1.已删*/
    @TableLogic
    private Integer deleted;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    //修改时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

}

