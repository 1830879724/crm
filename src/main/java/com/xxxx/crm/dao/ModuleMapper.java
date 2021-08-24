package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.model.TreeModel;
import com.xxxx.crm.vo.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModuleMapper  extends BaseMapper<Module,Integer> {
    //查询所有的资源列表
    public List<TreeModel> queryAllModules();

    //查询所有的资源数据
    public List<Module> queryModuleList();
    //模块名称 moduleName 同一层级 名称唯一
    Module queryModuleByGradeModuleName(@Param("grade") Integer grade, @Param("moduleName") String moduleName);
    //地址 url 同一层级不可重复
    Module queryModuleByGradeAndUrl( @Param("grade") Integer grade,  @Param("url") String url);
    //权限码 optValue  不可重复
    Module queryModuleByOptValue(String optValue);
}