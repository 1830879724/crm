layui.use(['table', 'treetable'], function () {
    var $ = layui.jquery;
    var table = layui.table;
    var treeTable = layui.treetable;

    // 渲染表格
    treeTable.render({
        treeColIndex: 1,
        treeSpid: -1,
        treeIdName: 'id',
        treePidName: 'parentId',
        elem: '#munu-table',
        url: ctx+'/module/list',
        toolbar: "#toolbarDemo",
        treeDefaultClose:true,
        page: true,
        cols: [[
            {type: 'numbers'},
            {field: 'moduleName', minWidth: 100, title: '菜单名称'},
            {field: 'optValue', title: '权限码'},
            {field: 'url', title: '菜单url'},
            {field: 'createDate', title: '创建时间'},
            {field: 'updateDate', title: '更新时间'},
            {
                field: 'grade', width: 80, align: 'center', templet: function (d) {
                    if (d.grade == 0) {
                        return '<span class="layui-badge layui-bg-blue">目录</span>';
                    }
                    if(d.grade==1){
                        return '<span class="layui-badge-rim">菜单</span>';
                    }
                    if (d.grade == 2) {
                        return '<span class="layui-badge layui-bg-gray">按钮</span>';
                    }
                }, title: '类型'
            },
            {templet: '#auth-state', width: 180, align: 'center', title: '操作'}
        ]],
        done: function () {
            layer.closeAll('loading');
        }
    });

    /**
     * 头部工具栏 监听事件  toolbar
     */
    table.on('toolbar(munu-table)',function (data) {
        //判断对应的事件类型
        //data.event ：对应的元素上设置的munu-table的属性值 全部展开
        if (data.event == "expand"){
            //全部展开
            treeTable.expandAll("#munu-table");
        }else if (data.event == "fold"){
            //全部折叠
            treeTable.foldAll("#munu-table");
        }else if (data.event=="add"){
            //添加目录 层级0 父菜单-1
            openAddModuleDialog(0,-1)
        }
    });


    /**
     * 监听行工具栏
     */
   table.on('tool(munu-table)',function (data){
        if (data.event == "add"){
            //添加子项
            //判断当前层级（如果是三级菜单，不能添加）
            if (data.data.grade == 2){
                layer.msg("不支持四级菜单添加",{icon:5});
                return;
            }
            //一级菜单|二级菜单 grade =当前层级+1，parentId = 当前资源的ID
            openAddModuleDialog(data.data.grade+1,data.data.id);
        }
   });

    /**
     * 打开添加资源对话框
     * @param grade 层级
     * @param parentId 父菜单
     */
    function openAddModuleDialog(grade,parentId){
        var title="<h3>资源管理 - 资源添加</h3>";
        var url = ctx + "/module/toAddModulePage?grade=" + grade + "&parentId=" + parentId;
        //打开窗口
        layui.layer.open({
            type: 2,
            title:title,
            content:url,
            area:["700px","450px"],
            maxmin:true
        });
    }

    
});