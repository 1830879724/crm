package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.SaleChance;

import java.util.List;
import java.util.Map;

public interface SaleChanceMapper extends BaseMapper<SaleChance,Integer> {
    /**
     * 多条件查询的接口不需要单独的定义
     * 由于多个模块都有多条件查询 统一放在BaseQuery里 继承即可
     */



}