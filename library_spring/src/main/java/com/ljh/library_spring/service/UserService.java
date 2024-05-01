package com.ljh.library_spring.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljh.library_spring.entity.Result;
import com.ljh.library_spring.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    //登录查询用户信息
    public User getUserInfoByUsername(String username);
    //查询用户角色信息
    List<String> getUserRoleInfo(String principal);
    //通过角色查询权限信息
    List<String> getUserPermissionInfo(List<String> roles);
    //注册
    Result register(User user);
    //通过UID查询用户信息
    User getUserInfoByUID(Integer userId);
    //存放图片并修改数据库中的路径(头像)
    Result setUserImg(MultipartFile file,Integer UID,String fileType,String changeType);
}
