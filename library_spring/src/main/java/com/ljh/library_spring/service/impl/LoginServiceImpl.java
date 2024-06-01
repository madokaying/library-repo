package com.ljh.library_spring.service.impl;

import com.ljh.library_spring.entity.LoginUser;
import com.ljh.library_spring.entity.Result;
import com.ljh.library_spring.entity.User;
import com.ljh.library_spring.mapper.UserMapper;
import com.ljh.library_spring.service.LoginService;
import com.ljh.library_spring.utils.JwtUtil;
import com.ljh.library_spring.utils.RedisCache;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;


@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisCache redisCache;
    @Resource
    private UserMapper userMapper;

    @Override
    public Result login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
        //用AuthenticationManager进行用户认证
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //认证没通过则抛出异常提示
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        /**，认证通过，使用userid生成jwtToken,并以result的方式返回，由于UserDetailsServiceImpl内调用的方法返回的是LoginUser，且已经将
         * 数据库内对应账号的信息（LoginUser对象）存入到了Principal内（JSON格式），所以loginUser内含登录用户的信息
        */
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        //获取用户id,用于之后生成token
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        String role = userMapper.getRoleByUserId(Long.valueOf(userId));
        //authenticate存入redis。userId作为键值对，用户信息作为键值
        redisCache.setCacheObject("login:"+userId,loginUser);
        //把token以及用户相应数据响应给前端
        HashMap<String,String> map = new HashMap<>();
        map.put("token",jwt);
        map.put("UID",userId);
        map.put("role",role);
        return new Result(200,"登陆成功",map);
    }

    @Override
    public Result logout() {
        //从SecurityContextHolder获取到userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        //通过userId删除redis内用户的信息(值)
        redisCache.deleteObject("login:"+userid);
        return new Result(200,"退出成功");
    }
}
