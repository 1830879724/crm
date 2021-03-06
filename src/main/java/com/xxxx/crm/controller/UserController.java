package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.query.UserQuery;
import com.xxxx.crm.service.UserService;

import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

        //调用Service层userLogin方法
        UserModel userModel= userService.userLogin(userName, userPwd);
        //设置ResultInfo的result的值（讲数据返回给请求）
        resultInfo.setResult(userModel);
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
        //获取cookie中用户的ID
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //调用Server层修改密码方法
        userService.updatePassWord(userId,oldPassWord,newPassWord,repeatPassWord);
        return resultInfo;
    }

    /**
     *进入修改密码的页面
     * @return
     */

    @RequestMapping("toPasswordPage")
    public  String toPasswordPage(){
        return "user/password";
    }

    /**
     *基本信息的页面
     * @return
     */

    @RequestMapping("toSettingPage")
    public  String toSettingPage(){
        return  "setting";
    }


    /**
     * 查询所有的销售人员
     * @return
     */
    @GetMapping("queryAllSale")
    @ResponseBody
    public List<Map<String, Object>> queryAllSale(){
        return userService.queryAllSale();
    }


    /**
     * 分页多条件查询用户列表
     * @param userQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object>  selectByParams(UserQuery userQuery){
        return userService.queryByParamsForTable(userQuery);

    }

    /**
     * 进入用户列表界面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "user/user";

    }

    /**
     * 用户添加
     * @param user
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addUser(User user){
        userService.addUser(user);
        return success("用户添加成功");
    }

    /**
     * 打开添加或修改用户界面
     * @return
     */
    @RequestMapping("toAddOrUpdateUserPage")
    public String toAddOrUpdateUserPage(Integer id,HttpServletRequest request){
        //判断id是否为空不为空表示更新操作
        if (id!=null){
            //通过id查询用户对象
            User user=userService.selectByPrimaryKey(id);
            //将数据设置到请求域中
            request.setAttribute("userInfo",user);
        }
        return "user/add_update";
    }
    /**
     * 用户更新
     * @param user
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("用户更新成功");
    }

    /**
     * 删除操作
     * @param ids
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteByids(ids);
        return success("用户删除成功");
    }

}
