layui.use(['table','layer'],function(){
       var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;


       /**
        * 加载数据表格
        */
       var tableIns = table.render({//渲染表格
              id:"roleTable",
              //容器元素的ID属性值
              elem:'#roleList',
              //容器的高度 full-差值
              height:"full-120",
              //单元格最小的宽度
              cellMinWidth:95,
              //访问数据接口，对应后台的数据接口
              url : ctx + '/role/list',
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
                     {field:'roleName',title:'用户名称',align:'center'},
                     {field:'roleRemark',title:'角色备注',align:'center'},
                     {field:'createDate',title:'创建时间',align:'center'},
                     {field:'updateDate',title:'修改时间',align:'center'},
                     {title:'操作',templet:'#roleListBar',fixed:"right",align:"center", minWidth:150},
              ]]

       });

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
                            roleName:$("[name='roleName']").val(),//用户名称
                     },
                     page:{
                            curr:1//默认重第 1 页开始
                     }
              })
       })
});
