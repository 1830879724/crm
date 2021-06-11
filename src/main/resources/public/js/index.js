layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    /**
     * 用户登录 表单提交
     */
    form.on("submit(login)", function(data){
        console.log(data.field)//当前容器的全部表单字段，名值对形式：{name:value}
        /*表单元素的非空判断*/

        /*发送ajax请求，传递用户姓名与密码，执行用户登录操作*/
        $.ajax({
            type:"post",
            url:ctx + "/user/login",
            data:{
                userName:data.field.username,
                userPwd:data.field.password
            },
            //回调函数 返回结果
            success:function (result) { //result是回调函数，用来接受后端返回的数据
                console.log(result);
                //判断是否登录成功 如果code=200, 则表示成功否则失败
                if(result.code == 200){
                    //登录成功
                    //设置用户登录状态
                    /**
                     * 1、利用会话session会话
                     *         保存用户信息，如果会话存在则用户登录状态否则失败
                     *         缺点：服务器关闭，会话会失效
                     * 2、利用cookie
                     *          保存用户信息，cookie未失效，则用户是登录状态
                     */
                    layer.msg("登录成功！",function() {
                    //将用户信息设置到cookie中
                        //登录成功跳转到首页
                        $.cookie("userId",result.result.userId);
                        $.cookie("userName",result.result.userName);
                        $.cookie("trueName",result.result.trueName);
                        window.location.href = ctx + "/main";
                    })

                }else {
                    //登录失败
                    layer.msg(result.msg,{icon:5});
                }
            }
        })

        return false;//阻止表单提交，如果需要表单跳转去掉即可
    });

});