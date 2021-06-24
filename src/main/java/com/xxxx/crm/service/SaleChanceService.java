package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.SaleChanceMapper;
import com.xxxx.crm.enums.DevResult;
import com.xxxx.crm.enums.StateStatus;
import com.xxxx.crm.query.SaleChaceQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {

        @Resource
        private SaleChanceMapper saleChanceMapper ;

    /**
     * 多条件分页查询营销机会 (返回的数据格式必须满足layUi中数据表格的数据要去格式)
     * @param saleChaceQuery
     * @return
     */
    public Map<String,Object> querySaleChaceByParams(SaleChaceQuery saleChaceQuery){
        Map<String,Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(saleChaceQuery.getPage(),saleChaceQuery.getLimit());
        //得到分页对象
        PageInfo<SaleChance> pageInfo =new PageInfo<>(saleChanceMapper.selectByParams(saleChaceQuery));
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        //设置分页好的列表
        map.put("data",pageInfo.getList());
        return map;
    }


    /**
     * 营销机会数据添加
     *   1.参数校验
     *      customerName:非空
     *      linkMan:非空
     *      linkPhone:非空 11位手机号
     *   2.设置相关参数默认值
     *      state:默认未分配  如果选择分配人  state 为已分配
     *      assignTime:如果  如果选择分配人   时间为当前系统时间
     *      devResult:默认未开发 如果选择分配人devResult为开发中 0-未开发 1-开发中 2-开发成功 3-开发失败
     *      isValid:默认有效数据(1-有效  0-无效)
     *      createDate updateDate:默认当前系统时间
     *   3.执行添加 判断结果
     *
     *    * @param saleChance
     *
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public  void  addSaleChance(SaleChance saleChance){
        //1、参数校验
        checkSaleChaceByParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());
        //2、设置相关属性的默认值
        //IsValid是否有效（0=无效，1=有效） 设置有效值 1=有效
        saleChance.setIsValid(1);
        //CreateDate创建时间 默认是系统当前时间
        saleChance.setCreateDate(new Date());
        //UpdateDate 默认是系统当前时间
        saleChance.setUpdateDate(new Date());
        //判断是否设置了指派人
        if (StringUtils.isBlank(saleChance.getAssignMan())){
            //如果为空则表示未设置指派人
            saleChance.setState(StateStatus.UNSTATE.getType());
            //AssignTime指派时间 设置为null
            saleChance.setAssignTime(null);
            //DevResult开发状态（0=未开发，1=开发中，2=开发成功，3=开发失败）0=默认未开发
            saleChance.setDevResult(DevResult.UNDEV.getStatus());

        }else {
            //IsValid是否有效（0=无效，1=有效） 设置有效值 1=有效
            saleChance.setIsValid(StateStatus.STATED.getType());
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }
        //执行添加，判断结果
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance) !=1,"营销机会数据添加失败");
    }

    /**
     *      参数校验
     *           customerName:非空
     *           linkMan:非空
     *           linkPhone:非空 11位手机号
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkSaleChaceByParams(String customerName, String linkMan, String linkPhone) {
        //customerName 客户名称 非空判断
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名称不能为空!");
        // linkMan:联系人非空
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空!");
        //linkPhone:非空 11位手机号
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"手机号不能为空!");
        //判断手机号码格式
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"联系号码格式不正确!");
    }


    /**
     * 营销机会数据更新
     *      1、参数校验
     *             营销机会对应ID 非空 ，数据库中的记录要存在
     *             customerName 客户名称:非空
     *             linkMan联系人:非空
     *             linkPhone手机号:非空 11位手机号
     *      2、设置相关参数的默认值
     *             updateDate更新时间  设置为当前系统时间
     *             assignMan指派人
     *                  1、原始的数据未设置
     *                      修改后未设置
     *                          不需要操作
     *                      修改后已设置
     *                          assigMan指派时间 设置为当前系统时间
     *                          分配状态 1 = 已分配
     *                          开发状态 1 = 开发中
     *                  2、原始的数据已设置
     *                     修改后未设置
     *                           assigMan指派时间 设置为null
     *                           分配状态 0 = 已分配
     *                           开发状态 0 = 开发中
     *                     修改后已设置
     *                            判断修改前后是否同一个人 如果是则不需要操作 如果是则更新指派时间
     *      3、执行更新操作，判断受影响的行数
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void  updateSaleChance(SaleChance saleChance){
        //1、参数校验
        //营销机会ID 非空 ，数据库中有相应记录
        AssertUtil.isTrue(null == saleChance.getId(),"待更新记录不存在");
        //通过主键查询对象
        SaleChance temp =saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        //判断数据库中对应的记录存在
        AssertUtil.isTrue(temp ==null,"待更新记录不存在");
        checkSaleChaceByParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());

        //2、设置相关参数的默认值
        //updateDate更新时间  设置为当前系统时间
        saleChance.setUpdateDate(new Date());
        //assignMan指派人
        //修改前的数据是否存在
        if (StringUtils.isBlank(temp.getAssignMan())){//不存在
            //判断修改后的值是否存在
            if (StringUtils.isBlank(saleChance.getAssignMan())){//修改前为空，修改后有值
                //assigMan指派时间 设置为当前系统时间
                saleChance.setAssignTime(new Date());
                // 分配状态 1 = 已分配
                saleChance.setState(StateStatus.STATED.getType());
               //  开发状态 1 = 开发中*/
                saleChance.setDevResult(DevResult.DEVING.getStatus());
            }
        }else {//存在
            //判断修改后的值是否存在
            if (StringUtils.isBlank(saleChance.getAssignMan())){//修改前有值,修改后无值
                //assigMan指派时间 设置为null
                saleChance.setAssignTime(null);
                //分配状态 0 = 已分配
                saleChance.setState(StateStatus.UNSTATE.getType());
                //开发状态 0 = 开发中
                saleChance.setDevResult(DevResult.UNDEV.getStatus());
            }else {//修改前有值，修改后有值
                    //判断修改前后用户是否相同
                if (saleChance.getAssignMan().equals(temp.getAssignMan())){
                    //更新时间
                    saleChance.setAssignTime(new Date());
                }else {//设置指派时间为起始时间
                    saleChance.setAssignTime(temp.getAssignTime());

                }

            }
        }
        //3、执行更新操作，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)!=1,"营销机会更新失败!");

    }

    /**
     * 逻辑删除（删除营销机会）
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChance(Integer[] ids){
        //判断id是否为空
        AssertUtil.isTrue(null == ids || ids.length<1,"待删除的记录不存在");
        //执行逻辑删除
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids) !=ids.length,"删除失败!");
    }
}
