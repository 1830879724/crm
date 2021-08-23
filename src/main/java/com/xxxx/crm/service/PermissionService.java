package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.vo.Permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionService extends BaseService<Permission,Integer> {

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 通过用户ID查询用户拥有的角色资源，当前登录用户所拥有的菜单列表（查询对应资源授权码）
     * @param userId
     * @return
     */
    public List<String> queryUserHasPermissionByUserId(Integer userId) {
        return permissionMapper.queryUserHasPermissionByUserId(userId);
    }
}
