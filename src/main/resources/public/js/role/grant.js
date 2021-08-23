$(function (){
    //加载树形结构
   loadModuleData();

});


//定义树形结构对象
var zTreeObj;
/**
 * 加载树形资源列表
 */
function loadModuleData(){
    //配置信息对象 zTree的参数配置
     var setting ={
        //使用复选框
         check:{
             enable:true,
         },
         //使用简单的json数据
         data:{
             simpleData:{
                 enable: true,
             }
         },
         //绑定函数
         callback:{
             // onCheck函数：当 checkbox/radio 被选中或取消选中时触发的函数
             onCheck:zTreeOnCheck
         }

    }
    //数据 通过Ajax查询资源列表
    $.ajax({
        type:"get",
        url:ctx + "/module/queryAllModules",
        dataType:"json",
        success:function (data){
            //data 查询到的资源列表
            //异步请求 加载zTree插件
            zTreeObj = $.fn.zTree.init($("#test1"), setting, data);
        }
    });
}

/**
 * onCheck函数：当 checkbox/radio 被选中或取消选中时触发的函数
 * @param event
 * @param treeId
 * @param treeNode
 */
function zTreeOnCheck(event, treeId, treeNode){
    //获取所有勾选的节点集合，如果checked=true表示获取勾选的节点  反之为表示为获取的节点
    var nodes =zTreeObj.getCheckedNodes(true);
    //获取所有的资源的id值
    //判断并遍历选中的节点集合
    if (nodes.length>0){
        //定义资源ID
        var mIds ="mIds=";
        //遍历节点集合，获取节点ID
        for (var i=0;i<nodes.length;i++){
            if (i<nodes.length-1){
                mIds += nodes[i].id +"&mIds=";
            }else {
                mIds += nodes[i].id ;
            }
        }
        console.log(mIds);
    }
    //通过属性选择器获取需要授权的角色id的值(隐藏域)
    var roleId = $("[name='roleId']").val();
    console.log("我的ID是："+roleId);
    //发送Ajax请求，执行角色的授权操作
    $.ajax({
        type:"post",
        url: ctx +"/role/addGrant",
        data: mIds+"&roleId="+roleId,
        dataType: "json",
        success:function (data){
            console.log(data)
    }
    })
 }