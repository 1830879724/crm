package com.xxxx.crm.service;


import com.github.pagehelper.util.StringUtil;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.Md5Util;
import com.xxxx.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService extends BaseService<User,Integer> {
    @Resource
    private UserMapper userMapper;

    /**
     * 用户登录
         1、参数判断，判断用户姓名密码是否为空  如果参数为空抛出异常(异常会被Controller层捕获并处理)
         2、调用数据访问层，通过用户名查询用户记录、返回用户对象
         3、判断用户对象是否为空 如果参数为空抛出异常(异常会被Controller层捕获并处理)
         4、判断用户密码是否正确，对比客户端传递的密码与数据库中查询的密码是否相同 如果密码不正确抛出异常(异常会被Controller层捕获并处理)
         5、如果密码正确，登录成功
     *
     * @param userName
     * @param userPwd
     */

    public UserModel userLogin(String userName,String userPwd){
        //1.参数判断，判断用户姓名密码是否为空
        checkLoginparams(userName,userPwd);
        //2、调用数据访问层，通过用户名查询用户记录、返回用户对象
        User user= userMapper.queryUserByName(userName);
        //3、判断用户对象是否为空
        AssertUtil.isTrue(user==null,"用户姓名不存在");
        // 4、判断用户密码是否正确，对比客户端传递的密码与数据库中查询的密码是否相同
        checkUserPwd(userPwd,user.getUserPwd());
        //返回构建用户的对象
        return buildUserInfo(user);

    }

    /**
     * 构建需要返回给客户端的用户对象
     * @param user
     */
    private UserModel buildUserInfo(User user) {
        UserModel userModel=new UserModel();
        userModel.setUserId(user.getId());
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /**
     * 密码判断
     * 先将客户端传递的密码加密，在与数据库中查询的密码对比
     * @param userPwd
     * @param pwd
     */
    private void checkUserPwd(String userPwd, String pwd) {
        //将客户端传递的密码加密
        userPwd= Md5Util.encode(userPwd);
        //判断密码是否相等
        AssertUtil.isTrue(!userPwd.equals(pwd),"用户密码不正确");
    }


    /**
     * 参数判断，判断用户姓名密码是否为空
     * @param userName
     * @param userPwd
     */
    private void checkLoginparams(String userName, String userPwd) {
        //验证用户姓名
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户姓名不能为空");
        //验证用户密码
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空");
    }
}
