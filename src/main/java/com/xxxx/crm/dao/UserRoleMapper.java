package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {
    /**
     * 通过用户id查询角色记录
     * @param userId
     * @return
     */
    Integer countUserRoleByUserId(Integer userId);

    /**
     * 根据拥挤ID删除用户角色记录
     * @param userId
     * @return
     */
    Integer deleteUserRoleByUserId(Integer userId);
}