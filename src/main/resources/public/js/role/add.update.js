layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    /**
     * 关闭弹出层
     */
    $('#closeBtn').click(function () {
        var index = parent.layer.getFrameIndex(window.name);//先得到当前iframe层的索引
        parent.layer.close(index);//在执行关闭
    });

    /**
     * 表单Submit监听
     */
    form.on('submit(addOrUpdateRole)',function (data){
        //提交数据时的加载层
        var  index =top.layer.msg("数据提交中，请稍后...",{
            icon:16,//图标
            time:false,//不关闭
            shade:0.8//设置遮罩的透明度
        });
        //得到所有的表单元素的值
        var formData =data.field;
        console.log(formData);
        //请求地址
        var url = ctx + "/role/add";//默认添加
        //判断用户id是否为空不为空则更新操作
        // if ($("[name='id']").val()){
        //     var url = ctx + "/user/update";
        // }

        //判断计划项ID是否为空（不为空则表示更新）
        $.post(url,formData,function (result) {
            //判断
            if (result.code == 200){
                //提示成功
                top.layer.msg("操作成功",{icon:6});
                //关闭加载层
                top.layer.close(index);
                //关闭弹出层
                layer.closeAll("iframe");
                // 刷新父页面，重新渲染表格数据
                parent.location.reload();
            }
            else {
                layer.msg(result.msg,{icon:5})
            }
        });
        // 阻止表单提交
        return false;
    });

});