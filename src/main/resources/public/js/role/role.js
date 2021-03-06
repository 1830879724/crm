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


       /**
        * 监听头部工具栏
        */
       table.on('toolbar(roles)',function (data){
              if (data.event == "add"){//添加操作
                     //打开添加或修改用户的对话框
                     openAddOrUpdateRoleDialog();
              }else if (data.event == "grant"){//授权操作
                     //获取被选中的数据信息
                     var checkStatus =table.checkStatus(data.config.id);
                     //打开授权窗口
                     openAddGrantDialog(checkStatus.data);
              }
       })

       /**
        * 打开授权界面
        */
       function openAddGrantDialog(data){
              //判断是否选择角色记录
              if (data.length == 0){
                     layer.msg("请选择要授权的用户",{icon:5});
                     return;
              }
              //只支持单个用户授权
              if (data.length > 1){
                     layer.msg("暂不支持多个角色授权",{icon:5});
                     return;
              }
              var url = ctx + "/module/toAddGrantPage?roleId="+data[0].id;
              var title= "<h3>角色管理 - 角色授权</h3>";
              layui.layer.open({
                     title:title,
                     type:2,
                     content:url,
                     area:["650px","450px"],
                     //设置可大可小化
                     maxmin:true
              });
       }

       /**
        * 打开添加或修改用户的对话框
        */
       function openAddOrUpdateRoleDialog(roleId){
              var title = "<h3>用户管理 -添加用户</h3>";
              var url =ctx + "/role/toAddOrUpdateRolePage";

              //判断id是否为空如果为空则为添加操作反之更新操作
              if (roleId != null && roleId != ''){
                     title = "<h3>用户管理 -角色更新</h3>";
                     url += "?roleId="+roleId;//传递主键查询数据
              }
              layui.layer.open({
                     title:title,
                     type:2,
                     content:url,
                     area:["500px","400px"],
                     //设置可大可小化
                     maxmin:true
              });
       }

       /**
        * 监听行工具栏
        */
       table.on('tool(roles)',function (data){
              if (data.event == "edit"){//更新操作
                     //打开添加或修改用户的对话框
                     openAddOrUpdateRoleDialog(data.data.id);
              } else if (data.event=='del'){//删除记录
                     //删除单条记录
                     deleteRole(data.data.id);

              }
       })

       /**
        * 删除单条用户记录
        * @param id
        */
       function deleteRole(roleId){
              //提示用户是否删除 ,弹出层
              layer.confirm('确定删除该记录嘛！',{icon:3,title:"角色管理"},function (index){
                     //关闭确认框
                     layer.close(index);
                     //发送对应的ajax请求删除记录
                     $.ajax({
                            type:"post",
                            url: ctx + "/role/delete",
                            data:{
                                   roleId:roleId
                            },
                            success:function (result) {
                                   //判断删除结果
                                   if (result.code == 200){
                                          layer.msg("删除成功",{icon:6});
                                          //刷新表格
                                          tableIns.reload();
                                   }else {
                                          //提示失败
                                          layer.msg(result.msg,{icon: 5})
                                   }
                            }
                     });
              });
       }

});
