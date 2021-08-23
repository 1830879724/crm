package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.Permission;

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
}