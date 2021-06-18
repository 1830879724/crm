package com.xxxx.crm.controller;


import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.SaleChaceQuery;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.utils.CookieUtil;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;


    /**
     *营销机会数据查询（分页多条件查询）
     * @param saleChaceQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> querySaleChaceByParams(SaleChaceQuery saleChaceQuery){
        return saleChanceService.querySaleChaceByParams(saleChaceQuery);

    }

    /**
     * 添加视图
     * 营销机会管理界面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return  "saleChance/sale_chance";
    }

    /**
     * 营销机会数据添加
     * @param request
     * @param saleChance
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addSaleChance(HttpServletRequest request,SaleChance saleChance){
        //从cookie中获取用户姓名
        String userName = CookieUtil.getCookieValue(request,"userName");
        //设置用户名到营销机会对象
        saleChance.setCreateMan(userName);
        //添加营销机会的数据
        saleChanceService.addSaleChance(saleChance);
        return success("营销机会数据添加成功!");
    }

    /**
     * 更新营销机会
     * @param saleChance
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance){
        //更新营销机会的数据
        saleChanceService.updateSaleChance(saleChance);
        return success("营销机会数据更新成功!");
    }


    /**
     * 进入添加页面 或者修改 页面
     * @return
     */
    @RequestMapping("toSaleChancePage")
    public String toSaleChancePage(Integer  saleChanceId,HttpServletRequest request){
        //判断saleChanceId是否为空
        if (saleChanceId !=null){
            SaleChance saleChance =saleChanceService.selectByPrimaryKey(saleChanceId);
            //将数据设置到请求域中
            request.setAttribute("saleChance",saleChance);
        }
        return "saleChance/add_update";
    }
}
