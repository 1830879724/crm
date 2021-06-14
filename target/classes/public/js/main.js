layui.use(['element', 'layer', 'layuimini','jquery','jquery_cookie'], function () {
    var $ = layui.jquery,
        layer = layui.layer,
        $ = layui.jquery_cookie($);

    // 菜单初始化
    $('#layuiminiHomeTabIframe').html('<iframe width="100%" height="100%" frameborder="0"  src="welcome"></iframe>')
    layuimini.initTab();


    /**
     * 用户退出
     *
     */
    $(".login-out").click(function (){
        //删除cookie
        //  $.removeCookie('userIdstr',domain:"")
        //彈出询问框提示是否退出
        layer.confirm('确定退出嘛!',{icon:3,title:'提示'},function (index){
            //关闭询问问题
            layer.close(index);
            //清空cookie
            $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"});
            $.removeCookie("userName",{domain:"localhost",path:"/crm"});
            $.removeCookie("trueNamw",{domain:"localhost",path:"/crm"});

            //跳转到登录界面（父接口跳转）
            window.parent.location.href=ctx + "/index";
        });


    });

});