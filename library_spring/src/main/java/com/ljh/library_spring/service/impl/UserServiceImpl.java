package com.ljh.library_spring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ljh.library_spring.entity.*;
import com.ljh.library_spring.mapper.*;
import com.ljh.library_spring.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private BookMapper bookMapper;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private TbBorrowMapper tbBorrowMapper;

    @Value("${upload.path}")
    private String imgURL;

    @Value("${upload.src}")
    private String imgSRC;

    /**
     * 实现注册相关逻辑,开启事务确保数据一致性
     * */
    @Transactional(rollbackFor = Exception.class)
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

    /**
     * 根据UID获取用户信息
     * */
    public User getUserInfoByUID(Integer userId){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getId,userId);
        User user = userMapper.selectOne(lambdaQueryWrapper);
        return user;
    }

    /**
     * 设置用户头像或者背景
     * */
    @Transactional
    public Result setUserImg(MultipartFile file,Integer UID,String fileType,String changeType){
        if (file.isEmpty()){
            return new Result(401,"文件不能为空");
        }
        // 生成唯一标识符作为文件名
        String uniqueFileName = UUID.randomUUID() + "." + fileType;
        try {
            //将文件保存指定目录
            file.transferTo(new File(imgURL + "img/" + uniqueFileName));
            //将当前用户的头像删除再存入新头像地址
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getId,UID);
            //根据UID查询该用户的头像地址（该地址是前端访问的地址，不是本地地址）
            User user = userMapper.selectOne(lambdaQueryWrapper);
            if ("avatar".equals(changeType)){
                String oldAvatarPath = user.getAvatar();
                //将取得的访问地址替换成本地地址
                String deleteAvatarPath = oldAvatarPath.replaceAll(imgSRC,imgURL);
                //如果头像是系统默认头像，则不执行删除
                String defaultAvatar = imgURL + "img/" + "defaultAvatar.jpg";
                if (!defaultAvatar.equals(deleteAvatarPath)){
                    File avatarFile = new File(deleteAvatarPath);
                    //若该路径下的是文件且存在，则删除
                    if (avatarFile.isFile() && avatarFile.exists()){
                        avatarFile.delete();
                    }
                }
                //前端访问的图片路径
                String src = imgSRC + "img/" + uniqueFileName;
                LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.set(User::getAvatar,src)
                        .eq(User::getId,UID);
                userMapper.update(lambdaUpdateWrapper);
                return new Result(200,"头像上传成功");
            }else if ("background".equals(changeType)){
                String oldBackgroundPath = user.getBackground();
                //将取得的访问地址替换成本地地址
                String deleteBackgroundPath = oldBackgroundPath.replaceAll(imgSRC,imgURL);
                //如果是系统默认背景则不删除
                String defaultBackground = imgURL + "img/" + "defaultBackground.jpg";
                if (!defaultBackground.equals(deleteBackgroundPath)){
                    File backgroundFile = new File(deleteBackgroundPath);
                    //若该路径下的是文件且存在，则删除
                    if (backgroundFile.isFile() && backgroundFile.exists()){
                        backgroundFile.delete();
                    }
                }
                //前端访问的图片路径
                String src = imgSRC + "img/" + uniqueFileName;
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

    /**
    * 更新密码
    * */
    public Result changePassword(PasswordForm passwordForm) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        //对比用户的密码，正确则执行更新密码操作，不正确则返回错误信息
        User user = userMapper.selectById(passwordForm.getUid());
        if (bCryptPasswordEncoder.matches(passwordForm.getOldPassword(),user.getPassword())){
            //将新密码加密用于存储
            String encodeNewPassword = bCryptPasswordEncoder.encode(passwordForm.getNewPassword());
            LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.set(User::getPassword,encodeNewPassword)
                    .eq(User::getId,passwordForm.getUid());
            userMapper.update(lambdaUpdateWrapper);
            return new Result(200,"密码更新成功,请重新登录");
        } else {
            return new Result(401,"密码错误，请确保原密码无误再提交修改");
        }
    }

    /**
    * 用户自身修改用户信息（只能修改自己的昵称，签名和邮箱）
    * */
    public Result changeInfoByUser(User user) {
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(User::getNickname,user.getNickname())
                .set(User::getSignature,user.getSignature())
                .set(User::getEmail,user.getEmail())
                .eq(User::getId,user.getId());
        //当数据修改成功，即影响数据库的数据量等于一
        if (userMapper.update(lambdaUpdateWrapper) == 1){
            return new Result(200,"数据修改成功");
        }
        return new Result(401,"数据修改失败，请联系管理员解决");
    }

    //通过用户id获取到用户四项基本数据的数量（评论/已借的书/帖子/收藏）
    public Result getCommonData(Integer UID) {
        //获取用户评论数
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getUserId,UID)
                .eq(Comment::getDeleteFlag,0);
        Integer myCommentCount = commentMapper.selectList(lambdaQueryWrapper).size();
        //获取我的借阅数
        LambdaQueryWrapper<TbBorrow> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(TbBorrow::getUserId,UID)
                .eq(TbBorrow::getState,1)
                .eq(TbBorrow::getDeleteFlag,0);
        Integer myBorrowCount = tbBorrowMapper.selectList(lambdaQueryWrapper1).size();
        //获取我的帖子数，目前先置为0
        //获取我的收藏数
        LambdaQueryWrapper<TbBook> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        Integer myCollectionCount = bookMapper.getMyCollectBooks(UID).size();
        CommonData commonData = new CommonData(myCommentCount,myBorrowCount,0,myCollectionCount);
        return new Result(200,"获取四项基础数值成功",commonData);
    }

    public Result borrowBook(Integer bookId, Integer userId) {
        //先判断还有没有书
        if (bookMapper.getBookNum(bookId) == 0){
            return new Result(401,"该书暂无库存，请过段时日后申请");
        } else {
            //借书表新增数据
            TbBorrow borrow = new TbBorrow();
            borrow.setBookId(bookId);
            borrow.setUserId(userId);
            tbBorrowMapper.insert(borrow);
            return new Result(200,"借书成功");
        }
    }

    @Override
    public Result judgeIsBorrowed(Integer bookId, Integer userId) {
        LambdaQueryWrapper<TbBorrow> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //0和1都属于不可再借的状态,2和3则可以借
        lambdaQueryWrapper.eq(TbBorrow::getBookId,bookId)
                .eq(TbBorrow::getDeleteFlag,0)
                .eq(TbBorrow::getUserId,userId);
        TbBorrow tbBorrow = tbBorrowMapper.selectOne(lambdaQueryWrapper);
        if (tbBorrow == null){
            return new Result(200,"该用户没有申请过该书，可以申请");
        } else {
            switch (tbBorrow.getState()){
                case 0:
                    return new Result(401,"该用户已申请过该书,且该请求仍在审核中，审核结束前不可再次申请");
                case 1:
                    return new Result(402,"该用户已借到该书，且仍未归还，不可再次申请");
                case 2:
                    return new Result(200,"该用户已申请过该书，但被拒绝，仍可继续申请");
                case 3:
                    return new Result(201,"该用户已申请过该书，且已归还，可以再次申请");
                default:
                    return new Result(403,"该用户申请过该书，但状态未知，不可申请");
            }
        }
    }

    @Override
    public Result getMyBorrowList(Integer userId) {
        List<TbBook> list = bookMapper.getMyBorrowList(userId);
        return new Result(200,"获取成功",list);
    }

}
