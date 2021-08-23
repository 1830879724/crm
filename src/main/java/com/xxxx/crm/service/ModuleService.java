package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.ModuleMapper;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.model.TreeModel;
import com.xxxx.crm.vo.Module;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ModuleService extends BaseService<Module,Integer> {

    @Resource
    private ModuleMapper moduleMapper;

    @Resource
    private PermissionMapper permissionMapper;
    /**
     * 查询所有的资源列表
     * @return
     */
    public List<TreeModel> queryAllModules(Integer roleId){
        //查询所有的资源列表
        List<TreeModel> treeModelList =moduleMapper.queryAllModules();
        //查询指定角色已经授权过的资源列表
        List<Integer> lists=permissionMapper.queryRoleHasModuleByRoleId(roleId);
        //判断角色是否拥有的资源ID
        if (lists !=null && lists.size()>0){
            //循环所有的资源列表判断用户拥有的资源ID中是否有匹配的，有则是指为true
            treeModelList.forEach(treeModel -> {
                //判断当前用户拥有的每一个ID中是否有当前遍历的资源ID
                if (lists.contains(treeModel.getId())){
                    //如果包含则说明角色授权过 则为true
                    treeModel.setChecked(true);
                }
            });
        }
        return treeModelList;

    }
}
