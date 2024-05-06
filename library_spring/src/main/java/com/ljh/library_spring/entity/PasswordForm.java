package com.ljh.library_spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordForm {
    /**
     * 原密码
     * */
    private String oldPassword;
    /**
     * 新密码
     * */
    private String newPassword;
    /**
     * UID
     * */
    private String uid;
}
