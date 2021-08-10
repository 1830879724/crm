package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.ModuleMapper;
import com.xxxx.crm.model.TreeModel;
import com.xxxx.crm.vo.Module;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ModuleService extends BaseService<Module,Integer> {

    @Resource
    private ModuleMapper moduleMapper;

    /**
     * 查询所有的资源列表
     * @return
     */
    public List<TreeModel> queryAllModules(){
        return moduleMapper.queryAllModules();
    }
}
