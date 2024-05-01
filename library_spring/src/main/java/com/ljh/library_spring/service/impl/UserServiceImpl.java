package com.ljh.library_spring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ljh.library_spring.entity.Result;
import com.ljh.library_spring.entity.User;
import com.ljh.library_spring.mapper.UserMapper;
import com.ljh.library_spring.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Value("${upload.path}")
    private String imgURL;

    @Value("${upload.src}")
    private String imgSRC;

    /**
     * 实现注册相关逻辑,开启事务确保数据一致性
     * */
    @Transactional
    public Result register(User insertUser){
        //加密密码
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode(insertUser.getPassword());
        User user = new User(insertUser.getUsername(),password);
        userMapper.insert(user);
        //查询注册完后用户的Id
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername,insertUser.getUsername());
        User toGetUserId = userMapper.selectOne(lambdaQueryWrapper);
        Long id = toGetUserId.getId();
        //将该用户的角色设置为用户
        userMapper.setUserRoleTable(id);
        return new Result(200,"注册成功");
    }

    public User getUserInfoByUsername(String username){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername,username);
        User user = userMapper.selectOne(lambdaQueryWrapper);
        return user;
    }

    public List<String> getUserRoleInfo(String principal) {

        return null;
    }

    public List<String> getUserPermissionInfo(List<String> roles) {
        return null;
    }

    public User getUserInfoByUID(Integer userId){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getId,userId);
        User user = userMapper.selectOne(lambdaQueryWrapper);
        return user;
    }

    @Transactional
    public Result setUserImg(MultipartFile file,Integer UID,String fileType,String changeType){
        if (file.isEmpty()){
            return new Result(401,"文件不能为空");
        }
        // 生成唯一标识符作为文件名
        String uniqueFileName = UUID.randomUUID().toString() + "." + fileType;
        try {
            //将文件保存指定目录
            file.transferTo(new File(imgURL + uniqueFileName));
            //将当前用户的头像删除再存入新头像地址
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getId,UID);
            //根据UID查询该用户的头像地址（该地址是前端访问的地址，不是本地地址）
            User user = userMapper.selectOne(lambdaQueryWrapper);
            if ("avatar".equals(changeType)){
                String oldAvatarPath = user.getAvatar();
                //将取得的访问地址替换成本地地址
                String deleteAvatarPath = oldAvatarPath.replaceAll(imgSRC,imgURL);
                File avatarFile = new File(deleteAvatarPath);
                //若该路径下的是文件且存在，则删除
                if (avatarFile.isFile() && avatarFile.exists()){
                    avatarFile.delete();
                }
                //前端访问的图片路径
                String src = imgSRC + uniqueFileName;
                LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.set(User::getAvatar,src)
                        .eq(User::getId,UID);
                userMapper.update(lambdaUpdateWrapper);
                return new Result(200,"头像上传成功");
            }else if ("background".equals(changeType)){
                String oldBackgroundPath = user.getBackground();
                //将取得的访问地址替换成本地地址
                String deleteBackgroundPath = oldBackgroundPath.replaceAll(imgSRC,imgURL);
                File backgroundFile = new File(deleteBackgroundPath);
                //若该路径下的是文件且存在，则删除
                if (backgroundFile.isFile() && backgroundFile.exists()){
                    backgroundFile.delete();
                }
                //前端访问的图片路径
                String src = imgSRC + uniqueFileName;
                LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.set(User::getBackground,src)
                        .eq(User::getId,UID);
                userMapper.update(lambdaUpdateWrapper);
                return new Result(200,"背景上传成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(401,"文件上传出现异常");

    }
}
