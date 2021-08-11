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