package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.service.PermissionService;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class lndexController extends BaseController {
    @Resource
    private UserService userService;

    @Resource
    private PermissionService permissionService;

    /**
     * 系统登录页
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }


    // 系统界面欢迎页
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }

    /**
     * 后端管理主页面
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request){
        //获取cookie中的用户ID
        Integer userId=LoginUserUtil.releaseUserIdFromCookie(request);
        //查询用户对象  设置session作用域
        User user =userService.selectByPrimaryKey(userId) ;
        request.getSession().setAttribute("user",user);
        //通过用户ID判断当前登录用户所拥有的菜单列表（查询对应资源授权码）
        List<String> lists=permissionService.queryUserHasPermissionByUserId(userId);
        //将集合设置到session作用域中
        request.getSession().setAttribute("lists",lists);
        return "main";
    }
}
