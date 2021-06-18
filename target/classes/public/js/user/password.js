layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);



    /**
     * 用户密码修改 表单提交
     */
    form.on("submit(saveBtn)", function(data){
        var fieldData = data.field;
        //所有表单元素的值
        console.log(data.field);
        //发送ajax请求
        $.ajax({
            type:"post",
            url:ctx + "/user/updatePwd",
            data:{
                oldPassWord:fieldData.old_password,
                newPassWord:fieldData.new_password,
                repeatPassWord:fieldData.again_password
            },
            dataType:"json",
            //回调结果 用result接收
            success:function (result) {
                console.log("我是"+result);
                console.log(result);
                //判断是否修改成功 如果code=200, 则表示成功否则失败
                if (result.code ==200){
                    //修改密码成功后清空cookie,跳转到登录界面
                    layer.msg("用户密码修改成功，系统将在3秒钟后退出!",function (){
                            //清空cookie
                            $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"});
                            $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                            $.removeCookie("trueNamw",{domain:"localhost",path:"/crm"});
                            //跳转到登录界面(父窗口跳转+parent，不然会直接在界面跳转一次)
                        window.parent.location.href=ctx + "/index";
                    });
                }else {
                    //登录失败
                    layer.msg(result.msg,{icon:5});
                }
            }
        })


    });

});