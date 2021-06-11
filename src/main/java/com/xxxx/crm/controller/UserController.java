package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.exceptions.ParamsException;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("user")
public class UserController extends BaseController  {
    @Resource
    private UserService userService;



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
}
