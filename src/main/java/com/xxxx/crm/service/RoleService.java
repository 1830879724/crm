package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.RoleMapper;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role,Integer> {
    @Resource
    private RoleMapper roleMapper;


    /**
     * 查询所有用户列表
     * @return
     */
    public List<Map<String,Object>> queryAllRole(Integer userId){
        return roleMapper.queryAllRole(userId);
    }


    /**
     * 添加用户角色
     *  1.参数校验
     *      角色名称非空且唯一
     *  2.设置参数默认值
     *         是否有效
     *         创建时间
     *         修改时间
     *  3.执行添加操作 判断受影响的行数
     *
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void  addRole(Role role){
        //1.参数校验
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名称不能为空!");
        //通过角色名查询角色记录
        Role temp= roleMapper.selectByRoleName(role.getRoleName());
        //判断用户角色是否存在（添加操作是，角色记录存在则表示名称不可用）
        AssertUtil.isTrue(temp!=null,"角色名称以存在，请重新输入");
        //2.设置参数默认值
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        //3.执行添加操作 判断受影响的行数
        AssertUtil.isTrue(roleMapper.insertSelective(role) <1,"用户添加失败!");
    }


    /**
     * 更新角色
     *  1.参数校验
     *      角色id非空判断且数据存在
     *      角色名称非空且唯一
     *  2.设置参数的默认值
     *      更新时间
     *  3.执行更新操作判断受影响的行数
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role){
        /*1.参数校验*/
        //角色id非空判断且数据存在
        AssertUtil.isTrue(null==role.getId(),"待更新操作不存在!");
        //通过角色名查询角色记录
        Role temp= roleMapper.selectByPrimaryKey(role.getId());
        //判断用户角色是否存在（添加操作是，角色记录存在则表示名称不可用）
        AssertUtil.isTrue(null==temp,"角色名称以存在，请重新输入");
        //角色名称非空且唯一
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名称不能为空!");
        //通过就是名称查询角色记录
        temp=roleMapper.selectByRoleName(role.getRoleName());
        //判断角色记录是否存在（如果不存在，则表示可用  且角色ID与当前更新的角色ID不一致，表示角色名称不可用）
        AssertUtil.isTrue(null!=temp && (!temp.getId().equals(role.getId())),"角色名称已存在，请重新输入!");
        /*2.设置参数的默认值*/
        role.setUpdateDate(new Date());
        /*3.执行更新操作判断受影响的行数*/
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)<1,"修改角色失败!");
    }
}
