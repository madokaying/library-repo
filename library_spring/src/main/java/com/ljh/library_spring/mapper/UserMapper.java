package com.ljh.library_spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljh.library_spring.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    //注册时给予用户“用户”的角色
    @Insert("insert into user_role (user_id, role_id) values (#{userId},1)")
    public void setUserRoleTable(Long userId);

    //根据用户ID获取用户的角色
    String getRoleByUserId(Long userid);

}
