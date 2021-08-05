layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 加载数据表格
     */
    var tableIns = table.render({//渲染表格
        id:"userTable",
        //容器元素的ID属性值
        elem:'#userList',
        //容器的高度 full-差值
        height:"full-120",
        //单元格最小的宽度
        cellMinWidth:95,
        //访问数据接口，对应后台的数据接口
        url : ctx + '/user/list',
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
            {field:'userName',title:'用户姓名',align:'center'},
            {field:'trueName',title:'真实姓名',align:'center'},
            {field:'email',title:'用户邮箱',align:'center'},
            {field:'phone',title:'用户号码',align:'center'},
            {field:'createDate',title:'创建时间',align:'center'},
            {field:'updateDate',title:'修改时间',align:'center'},
            {title:'操作',templet:'#userListBar',fixed:"right",align:"center", minWidth:150},
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
                userName:$("[name='userName']").val(),//用户名称

                email:$("[name='email']").val(),//邮箱

                phone:$("[name='phone']").val(),//用户号码
            },
            page:{
                curr:1//默认重第 1 页开始
            }
        })
    })

    /**
     * 监听头部工具栏
     */
    table.on('toolbar(users)',function (data){
        if (data.event == "add"){//添加操作
            //打开添加或修改用户的对话框
            openAddOrUpdateUserDialog();
        }else if (data.event == "del"){//删除操作
            //获取被选中的数据信息
            var checkStatus =table.checkStatus(data.config.id);
            console.log(checkStatus);
            //删除多个用户记录
            deleteUsers(checkStatus.data);
        }
    })

    /**
     * 删除多条用户记录
     * @param userData
     */
    function deleteUsers(userData){
        //判断用户是否选择要删除的记录
        if (userData.length ==0){
            layer.msg("请选择删除记录",{icon:5});
            return;
        }
        //询问用户是否确认删除
        layer.confirm('确定删除选中的记录？',{icon:3,title:"用户管理"},function (index) {
            //关闭确认框
            layer.close(index);
            //传递的参数是数组
            var ids = "ids=";
            //循环选中的行数据记录
            for (var i =0;i<userData.length;i++){
                if (i<userData.length-1){
                    ids =ids +userData[i].id + "&ids=";
                }else {
                    ids =ids +userData[i].id
                }
            }
            //发送ajax请求,执行删除用户
            $.ajax({
                type:"post",
                url: ctx +"/user/delete",
                data:ids,//传递的参数是数据
                success:function(result){
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
            })
        });
    }

    /**
     * 监听行工具栏
     */
    table.on('tool(users)',function (data){
        if (data.event == "edit"){//更新操作
            //打开添加或修改用户的对话框
            openAddOrUpdateUserDialog(data.data.id);
        } else if (data.event=='del'){//删除记录
            //删除单条记录
            deleteUser(data.data.id);

        }
    })

    /**
     * 删除单条用户记录
     * @param id
     */
    function deleteUser(id){
//提示用户是否删除 ,弹出层
        layer.confirm('确定删除该记录嘛！',{icon:3,title:"用户管理"},function (index){
            //关闭确认框
            layer.close(index);
            //发送对应的ajax请求删除记录
            $.ajax({
                type:"post",
                url: ctx + "/user/delete",
                data:{
                    ids:id
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

    /**
     * 打开添加或修改用户的对话框
     */
    function openAddOrUpdateUserDialog(id){
        var title = "<h3>用户管理 -添加用户</h3>";
        var url =ctx + "/user/toAddOrUpdateUserPage";

        //判断id是否为空如果为空则为添加操作反之更新操作
        if (id != null && id != ''){
            title = "<h3>用户管理 -更新用户</h3>";
            url += "?id="+id;//传递主键查询数据
        }
        layui.layer.open({
            title:title,
            type:2,
            content:url,
            area:["650px","400px"],
            //设置可大可小化
            maxmin:true
        });
    }



});