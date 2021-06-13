package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.exceptions.ParamsException;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.service.UserService;

import com.xxxx.crm.utils.LoginUserUtil;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("user")
public class UserController extends BaseController  {
    @Resource
    private UserService userService;


    /**
     * 用户登录
     * @param userName
     * @param userPwd
     * @return
     */

    @PostMapping("login")
    @ResponseBody
    public ResultInfo userLogin(String userName,String userPwd){

        ResultInfo resultInfo=new ResultInfo();

        //通过try catch捕获Service异常，如果Service捕获到异常则登录,失败否则登录成功
        try{
            //调用Service层userLogin方法
            UserModel userModel= userService.userLogin(userName, userPwd);
            //设置ResultInfo的result的值（讲数据返回给请求）
            resultInfo.setResult(userModel);
        }catch (ParamsException p) {
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        }catch(Exception e){
            resultInfo.setCode(500);
            resultInfo.setMsg("登录失败");
        }
        return resultInfo;
    }

    /**
     * 用户修改密码
     * @param request
     * @param oldPassWord
     * @param newPassWord
     * @param repeatPassWord
     * @return
     */
    @PostMapping("updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassWord(HttpServletRequest request,
       String oldPassWord,String newPassWord,String repeatPassWord){

        ResultInfo resultInfo=new ResultInfo();
        try{
            //获取cookie中用户的ID
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            //调用Server层修改密码方法
            userService.updatePassWord(userId,oldPassWord,newPassWord,repeatPassWord);
            //捕获异常
        }catch (ParamsException e){
            //打印异常
            e.printStackTrace();
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
        }catch (Exception e){
            //打印异常
            e.printStackTrace();
            //捕获异常
            resultInfo.setCode(500);
            resultInfo.setMsg("用户修改密码失败!");
        }
        return resultInfo;
    }

    /**
     *进入修改密码的页面
     * @return
     */

    @RequestMapping("toPasswordPage")
    public  String toPasswordPage(){
        return "user/passWord";
    }
}
