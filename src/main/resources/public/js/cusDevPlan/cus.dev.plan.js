layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;


    /**
     * 加载数据表格
     */
    var tableIns = table.render({//渲染表格
        id:"saleChanceTable",
        //容器元素的ID属性值
        elem:'#saleChanceList',
        //容器的高度 full-差值
        height:"full-120",
        //单元格最小的宽度
        cellMinWidth:95,
        //访问数据接口，对应后台的数据接口 设置flag参数表示查询的是客户开发计划数据
        url : ctx + '/sale_chance/list?flag=1',
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
            {field:'chanceSource',title:'机会来源',align:'center'},
            {field:'customerName',title:'客户名称',align:'center'},
            {field:'cgjl',title:'成功几率',align:'center'},
            {field:'overview',title:'概要',align:'center'},
            {field:'linkMan',title:'联系人',align:'center'},
            {field:'linkPhone',title:'联系号码',align:'center'},
            {field:'description',title:'描述',align:'center'},
            {field:'createMan',title:'创建人',align:'center'},
            {field:'createDate',title:'创建时间',align:'center'},
            {field:'updateDate',title:'修改时间',align:'center'},
            {field:'devResult',title:'开发状态',align:'center',templet: function (d){
                    //调用函数，返回格式化的结果
                    return formatterDevResult(d.devResult);
                }},
            {title:'操作',templet:'#op',fixed:"right",align:"center", minWidth:150},
        ]]

    });


    /**
     * 格式化开发状态
     *  0 - 未开发
     *  1 - 开发中
     *  2 - 开发成功
     *  3 - 开发失败
     * @param value
     * @returns {string}
     */
    function formatterDevResult(devResult){
        if(devResult == 0) {
            return "<div style='color:seagreen'>未开发</div>";
        } else if(devResult==1) {
            return "<div style='color: #00FF00;'>开发中</div>";
        } else if(devResult==2) {
            return "<div style='color: #00B83F'>开发成功</div>";
        } else if(devResult==3) {
            return "<div style='color: red'>开发失败</div>";
        } else {
            return "<div style='color: #af0000'>未知</div>"
        }
    }


    /**
     * 搜索按钮的点击事件
     */
    $(".search_btn").click(function () {
        /**
         * 表格重载
         * 多条件查询
         */
        tableIns.reload({
            //设置需要传递给后端的参数
            where:{//设置异步数据的接口参数
                //通过文本框的值/下拉框的值
                customerName:$("[name='customerName']").val(),//客户名称

                createMan:$("[name='createMan']").val(),//创建人

                devResult:$("#devResult").val(),//开发状态
            },
            page:{
                curr:1//默认重第 1 页开始
            }
        })
    })


    /**
     *行工具栏监听
     */
    table.on('tool(saleChances)',function (data){
        //判断类型
        if (data.event =="dev"){//开发
            //打开计划项与开发项
            openCusDevPlan('计划项数据开发',data.data.id)
        }else if (data.event == "info"){//详情
            //打开计划项与详情页面
            openCusDevPlan('计划项数据维护',data.data.id)
        }
    });

    /**
     * 打开计划项开发或详情页面
     * @param titile
     * @param id
     */
    function openCusDevPlan(title,id){
        layui.layer.open({
            title:title,
            type:2,
            content: ctx + "/cus_dev_plan/toCusDevPlanPage?id="+id,
            area:["750px","550px"],
            //设置可大可小化
            maxmin:true
        });

    }

});
