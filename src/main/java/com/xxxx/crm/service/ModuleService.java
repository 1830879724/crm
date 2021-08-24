package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.ModuleMapper;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.model.TreeModel;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 查询所有的资源数据
     * @return
     */
    public Map<String,Object> queryModuleList(){
        Map<String,Object> map =new HashMap<>();
        //查询资源列表
        List<Module> modulesList=moduleMapper.queryModuleList();
        map.put("code",0);
        map.put("msg","");
        map.put("count",modulesList.size());
        map.put("data",modulesList);
        return map;
    }

    /**
     * 添加资源
     * 1.参数校验
     *      1.模块名称 moduleName 非空
     *          同一层级 名称唯一
     *      2.地址 url
     *          二级菜单非空 grade=1 且不可重复
     *      3.父级菜单 parentId
     *          一级菜单（目录 grade=0） null
     *          二级|三级菜单（菜单|按钮 grade=1或2） 非空，父级菜单必须存在
     *      4.层级 grade
     *          非空 0|1|2
     *      5.权限码 optValue
     *           非空 不可重复
     * 2.设置参数的默认值
     *          是否有效  isValid =1
     *          创建时间|更新时间  系统当前时间
     * 3.执行添加操作，判断受影响的行数
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addModule(Module module){
        //1.参数校验
        //层级 grade 非空 0|1|2
        Integer grade=module.getGrade();
        AssertUtil.isTrue(null==grade || !(grade==0 || grade==1 || grade==2),"菜单层级不合法!");
        //1.模块名称 moduleName 非空
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"模块名称不能为空!");
        //模块名称 moduleName 同一层级 名称唯一
        AssertUtil.isTrue(null !=moduleMapper.queryModuleByGradeModuleName(grade,module.getModuleName()),"模块名称已存在");
        //如果是二级菜单（grade=1）
        if (grade==1){
            // 2.地址 url 二级菜单非空
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"URL不能为空!");
            // 2.地址 url 同一层级不可重复
            AssertUtil.isTrue(null!=moduleMapper.queryModuleByGradeAndUrl(grade,module.getUrl()),"URL不能重复");
        }
        //3.父级菜单 parentId 一级菜单（目录 grade=0） null
        if (grade==0){
            module.setParentId(null);
        }
        //3.父级菜单 parentId 二级|三级菜单（菜单|按钮 grade=1或2） 非空，父级菜单必须存在
        if (grade!=0){
            //非空
            AssertUtil.isTrue(null ==module.getParentId(),"父级菜单不能为空!");
            //父级菜单必须存在 将父级菜单ID作为主键 查询资源记录
            AssertUtil.isTrue(null == moduleMapper.selectByPrimaryKey(module.getParentId()),"请指定的父级菜单");
        }
        //权限码 optValue 非空
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"权限码不可为空!");
        //权限码 optValue  不可重复
        AssertUtil.isTrue(null!=moduleMapper.queryModuleByOptValue(module.getOptValue()),"权限码已存在");

        //2.设置参数的默认值
        module.setIsValid((byte) 1);
        module.setUpdateDate(new Date());
        module.setCreateDate(new Date());
        //3.执行添加操作，判断受影响的行数
        AssertUtil.isTrue(moduleMapper.insertSelective(module)<1,"添加资源失败!");
    }
}
