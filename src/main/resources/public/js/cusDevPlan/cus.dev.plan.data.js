layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;




    /**
     * 加载计划项数据表格
     */
    var tableIns = table.render({//渲染表格
        id:"CusDevPlanTable",
        //容器元素的ID属性值
        elem:'#cusDevPlanList',
        //容器的高度 full-差值
        height:"full-120",
        //单元格最小的宽度
        cellMinWidth:95,
        //访问数据接口，对应后台的数据接口
        url : ctx + '/cus_dev_plan/list?saleChanceId=' + $("[name='id']").val(),
        //开启分页
        page:true,
        //默认每页显示的数量
        limit:10,
        //每页页数的可选项
        limits: [10,15,20,25,30],
        //开启头部工具栏
        toolbar:'#toolbarDemo',
        //表头
        cols:[[

            //field 要求field属性值与返回的数据中对应的数据字段名一致
            //title 设置列的标题
            //sort 是否允许排序（默认是false）
            //fixed 固定列

            //复选框
            {type:'checkbox',fixed: 'center'},
            {field:'id',title:'编号',sort:true,fixed:'left'},
            {field:'planItem',title:'计划项',align:'center'},
            {field:'planDate',title:'计划时间',align:'center'},
            {field:'exeAffect',title:'计划效果',align:'center'},
            {field:'createDate',title:'创建时间',align:'center'},
            {field:'updateDate',title:'修改时间',align:'center'},
            {title:'操作',templet:'#cusDevPlanListBar',fixed:"right",align:"center", minWidth:150},
        ]]

    });
    /**
     * 监听头部工具栏
     */
    table.on('toolbar(cusDevPlans)',function (data){
        if (data.event == "add"){//添加
            //打开添加或修改的窗口
            openAddOrUpdateCusDevPlanDialog();
        }else if (data.event == "success"){//成功

        }else if (data.event == "failed"){//失败

        }
    });

    /**
     * 监听行工具栏
     */

    table.on('tool(cusDevPlans)',function (data){
        if (data.event == "edit"){//更新操作
            //打开添加或修改的窗口
            openAddOrUpdateCusDevPlanDialog(data.data.id);
        }else if (data.event =="del"){//删除操作

        }
    })

    /**
     * 打开添加或修改的窗口
     */
    function openAddOrUpdateCusDevPlanDialog(id){
        var title = "计划项管理  - 添加计划项";
        var  url = ctx +"/cus_dev_plan/toAddOrUpdateCusDevPlanPage?sId=" +$("[name='id']").val();
        //判断计划项的ID是否为空，如果为空表示添加 否则为更新
        if (id!=null && id !=''){
            //更新计划项
            title = "计划项管理  - 更新计划项";
            url+= "&id=" +id;
        }
        layui.layer.open({
            title:title,
            type:2,
            content: url,
            area:["500px","300px"],
            //设置可大可小化
            maxmin:true
        });
    }

});
