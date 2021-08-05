package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController  extends BaseController {

    @Resource
    private RoleService roleService;

    /**
     * 查询所有用户列表
     * @return
     */
    @RequestMapping("queryAllRole")
    @ResponseBody
    public List<Map<String,Object>> queryAllRole(){
        return roleService.queryAllRole();
    }

}
