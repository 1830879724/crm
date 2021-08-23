package com.xxxx.crm.controller;


import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.model.TreeModel;
import com.xxxx.crm.service.ModuleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("module")
public class ModuleController  extends BaseController {

    @Resource
    private ModuleService moduleService;


    /**
     * 查询所有的资源列表
     * @return
     */
    @ResponseBody
    @RequestMapping("queryAllModules")
    public List<TreeModel> queryAllModules(){
        return moduleService.queryAllModules();
    }

    /**
     * 打开授权界面
     * @return
     */
    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId, HttpServletRequest req){
        System.out.println("小鬼子");
        //将需要授权的角色Id设置到请求域中
        req.setAttribute("roleId",roleId);
        return "role/grant";

    }
}
