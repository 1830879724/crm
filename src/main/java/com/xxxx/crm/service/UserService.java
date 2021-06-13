package com.xxxx.crm.service;


import com.github.pagehelper.util.StringUtil;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.Md5Util;
import com.xxxx.crm.utils.UserIDBase64;
import com.xxxx.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
        //userModel.setUserId(user.getId());
        //设置加密的用户ID
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
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

    /**
     * 修改密码
     *  接收四个参数（用户ID、用户密码、新密码、确认密码）
     *                 1、通过用户ID获取用户记录，返回一个用户对象
     *                 2、参数校验
     *                      1.待更新用户记录是否存在 （用户对象是否为空）
     *                      2.原始密码是否为空
     *                      3.原始密码是否正确（查询用户的对象中的用户对象是否与原始密码一致）
     *                      4.判断新密码是否为空
     *                      5.判断新密码是否与原始密码一致（不允许新密码与原始密码一致）
     *                      6.判断新密码是否为空你
     *                      7.判断确认密码是否与新密码一致
     *                 3、设置用户的新密码
     *                      需要将用户的新密码通过指定的算法进行加密（md5加密）
     *                 4、执行更新操作，判断受影响的行数
     *
     */
    //更新操作加上事务 ，在抛异常的时候执行 rollbackFor = Exception.class  （除了查询，增删改都加上事务）
    @Transactional(propagation = Propagation.REQUIRED)
    public  void  updatePassWord(Integer userId,String oldPwd ,String newPwd,String repeatPwd){
        // 1、通过用户ID获取用户记录，返回一个用户对象
        User user=userMapper.selectByPrimaryKey(userId);
        //判断用户记录是否存在
        AssertUtil.isTrue(null == user,"用户记录不存在!");
        //参数校验
        checkpasswordParams(user,oldPwd,newPwd,repeatPwd);
        //设置用户的新密码
        user.setUserPwd(Md5Util.encode(newPwd));
        //执行更新操作，判断受影响的行
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改密码失败!");
    }


    /**
     * 修改密码参数校验
             2.原始密码是否为空
             3.原始密码是否正确（查询用户的对象中的用户对象是否与原始密码一致）
             4.判断新密码是否为空
             5.判断新密码是否与原始密码一致（不允许新密码与原始密码一致）
             6.判断新密码是否为空
             7.判断确认密码是否与新密码一致
     * @param user
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    private void checkpasswordParams(User user,String oldPwd, String newPwd, String repeatPwd) {
        //原始密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd),"原始密碼不能為空！");
        //原始密码是否正确（查询用户的对象中的用户对象是否与原始密码一致）
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)),"原始密码不一致!");
        //判断新密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码不能为空!");
        //判断新密码是否与原始密码一致（不允许新密码与原始密码一致）
        AssertUtil.isTrue(oldPwd.equals(newPwd),"新密码不能原始密码一致");
        //判断新密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd),"新密码不能为空");
        //判断确认密码是否与新密码一致
        AssertUtil.isTrue(!newPwd.equals(repeatPwd),"两次密码不一致，请重新输入!");

    }
}
