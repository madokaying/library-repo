package com.ljh.library_spring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ljh.library_spring.entity.LoginUser;
import com.ljh.library_spring.entity.User;
import com.ljh.library_spring.mapper.MenuMapper;
import com.ljh.library_spring.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
 
    @Resource
    private UserMapper userMapper;
    @Resource
    private MenuMapper menuMapper;
 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,username);
        User user = userMapper.selectOne(wrapper);
        //如果查询不到数据就通过抛出异常来给出提示
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误");
        }
        //根据用户查询权限信息 添加到LoginUser中
        List<String> permissionKeyList =  menuMapper.selectPermsByUserId(user.getId());
        //封装成UserDetails对象返回 
        return new LoginUser(user,permissionKeyList);
    }
}