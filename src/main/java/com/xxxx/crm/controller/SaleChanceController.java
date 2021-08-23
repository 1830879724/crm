package com.xxxx.crm.controller;


import com.xxxx.crm.annoation.RequiredPermission;
import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.enums.StateStatus;
import com.xxxx.crm.query.SaleChaceQuery;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.utils.CookieUtil;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
     *营销机会数据查询（分页多条件查询）101001
     * 如果flag的值不为空，值为1 则表示当前查询的客户开发计划,否则为营销机会数据
     * @param saleChaceQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> querySaleChaceByParams(SaleChaceQuery saleChaceQuery ,
                                                     Integer flag,HttpServletRequest request){
        if (flag !=null && flag == 1){
            //当前查询的是客户开发计划
            //设置分配计划
            saleChaceQuery.setState(StateStatus.STATED.getType());
            //设置指派人(当前登录用的的ID)
            //从cookie中获取当前登录用户的ID
            Integer userId= LoginUserUtil.releaseUserIdFromCookie(request);
            saleChaceQuery.setAssignMan(userId);
        }
        return saleChanceService.querySaleChaceByParams(saleChaceQuery);

    }

    /**
     * 添加视图
     * 营销机会管理界面
     * @return
     */
    @RequiredPermission(code = "1010")
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
    @RequiredPermission(code = "101001")
    @PostMapping("add")
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
    @RequiredPermission(code = "101002")
    @PutMapping("update")
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
    @RequiredPermission(code = "101004")
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

    /**
     * 逻辑删除营销机会数据
     * @param ids
     * @return
     */
    @RequiredPermission(code = "101003")
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids){
        // 删除营销机会的数据
        saleChanceService.deleteBatch(ids);
        return success("营销机会数据删除成功！");
    }

    /**
     * 更新营销机会的开发状态
     * @param id
     * @param devResult
     * @return
     */
    @RequestMapping("updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer id,Integer devResult){

        saleChanceService.updateSaleChanceDevResult(id,devResult);
            return  success("营销机会开发状态更新成功!");
    }

}
