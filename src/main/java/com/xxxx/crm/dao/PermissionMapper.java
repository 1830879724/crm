package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper  extends BaseMapper<Permission,Integer> {
    /**
     * 通过角色ID查询对应的权限记录
     * @param roleId
     * @return
     */
    Integer getByRoleId(Integer roleId);

    /**
     * 删除权限记录
     * @param roleId
     */
    void deleteByRoleId(Integer roleId);

    /**
     * 查询指定角色已经授权过的资源列表
     * @param roleId
     * @return
     */
    List<Integer> queryRoleHasModuleByRoleId(Integer roleId);
}