layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 加载数据表格
     */
    var tableIns = table.render({//渲染表格
        //容器元素的ID属性值
        elem:'#saleChanceList',
        //容器的高度 full-差值
        height:"full-120",
        //单元格最小的宽度
        cellMinWidth:95,
        //访问数据接口，对应后台的数据接口
        url : ctx + '/sale_chance/list',
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
            {field:'userName',title:'分配人',align:'center'},
            {field:'assignTime',title:'分配时间',align:'center'},
            {field:'createDate',title:'创建时间',align:'center'},
            {field:'state',title:'分配状态',align:'center',templet: function (d) {
               //调用函数，返回格式化的结果
                    return formatterState(d.state);
                }},
            {field:'devResult',title:'开发状态',align:'center',templet: function (d){
                    //调用函数，返回格式化的结果
                    return formatterDevResult(d.devResult);
                }},

            {title:'操作',templet:'#saleChanceListBar',fixed:"right",align:"center", minWidth:150},
        ]]

    });

    /**
     * 格式化分配状态
     *  0 - 未分配
     *  1 - 已分配
     *  其他 - 未知
     * @param state
     * @returns {string}
     */
    function formatterState(state){
        if(state==0) {
            return "<div style='color: cyan'>未分配</div>";
        } else if(state==1) {
            return "<div style='color: green'>已分配</div>";
        } else {
            return "<div style='color: red'>未知</div>";
        }
    }

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

                state:$("#state").val(),//分配状态
            },
            page:{
                curr:1//默认重第 1 页开始
            }
        })
    })


    /**
     * 头部工具栏 监听事件  toolbar
     */
    table.on('toolbar(saleChances)',function (data) {
        console.log(data);
        //判断对应的事件类型
        //data.event ：对应的元素上设置的lay-event的属性值
        if (data.event == "add"){
            //添加操作
            openSaleChanceDialog();

        }else if (data.event == "del"){
        //删除操作
        }

    })

    /**
     * 打开添加/修改营销机会数据窗口
     *
     * 如果营销机会ID 为空 则为添加操作 反之为修改操作
     */
    function openSaleChanceDialog(saleChanceId) {
        //弹出层的标题
        var title = "<h2>营销机会管理 - 添加营销机会</h2>";
        var url = ctx + "/sale_chance/toSaleChancePage";
        //判断营销机会ID是否为空
        if (saleChanceId !=null && saleChanceId !==''){
            //更新标题
            title = "<h2>营销机会管理 - 更新营销机会</h2>";
            //请求地址传递营销机会的ID
            url +='?saleChanceId=' + saleChanceId;
        }
        layui.layer.open({
            title:title,
            type:2,
            content: url,
            area:["500px","620px"],
            //设置可大可小化
            maxmin:true
        });
    }

    /**
     * 行工具栏监听事件  tool
     */
    table.on('tool(saleChances)',function (data) {
      //判断类型
        if (data.event == "edit"){//编辑操作
            //得到对应的ID
            var saleChanceId = data.data.id;
            //打开修改窗口
            openSaleChanceDialog(saleChanceId);
        }else if (data.event == "del"){//删除操作

        }
    })
});
