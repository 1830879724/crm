package com.xxxx.crm;

import com.alibaba.fastjson.JSON;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.exceptions.AuthException;
import com.xxxx.crm.exceptions.NoLoginException;
import com.xxxx.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.security.auth.login.LoginException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 全局异常统一处理
 */
@Component//异常生效注解
public class GlobalExceptionResolver  implements HandlerExceptionResolver {

    /**
     * 异常处理方法
     *方法的返回值：
     *          1、返回视图
     *          2、返回(JSON数据)
     * 怎么判断方法的返回值：
     *         方法上是否声明   @ResponseBody 注解
     *            如果未声明，则表示返回视图
     *            如果声明了，则表示返回数据
     *
     * @param request 请求对象
     * @param response 响应对象
     * @param handler 方法对象
     * @param e 异常对象
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {

        /**
         *  非法请求拦截
         * 判断是否抛出未登录异常:
         *               如果抛出该异常则跳转到登录界面  重定向到登录界面
         */
        if (e instanceof NoLoginException){
            //重定向到登录界面
            ModelAndView mv=new ModelAndView("redirect:/index");
            return  mv;
        }

        /**
         *  默认的异常情况 返回视图
         */
        ModelAndView modelAndView =new ModelAndView("error");
        //设置异常信息
        modelAndView.addObject("code",500);
        modelAndView.addObject("msg","异常异常,请重试..");
        //判断handler是否是方法对象
        if (handler instanceof HandlerMethod){
            //类型转换
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //通过反射 获取方法上的声明的  @ResponseBody 注解  如果声明了就有值 未声明返回的就是null
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);

            //判断 responseBody 对象是否为空（如果对象为空则表示返回的是视图，如果不为空则返回的是数据）
            if (responseBody == null){
                /**
                 * 方法返回视图
                 */
                //判断异常类型
                if (e instanceof ParamsException){
                    ParamsException p =(ParamsException) e;
                    //设置异常信息
                    modelAndView.addObject("code",p.getCode());
                    modelAndView.addObject("msg",p.getMsg());

                }
                if (e instanceof AuthException) { // 认证异常
                    AuthException a  = (AuthException) e;
                    // 设置异常信息
                    modelAndView.addObject("code",a.getCode());
                    modelAndView.addObject("msg",a.getMsg());
                }
                return  modelAndView;
            } else {
                /**
                 * 方法返回数据
                 */
                //设置默认的异常处理
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(500);
                resultInfo.setMsg("系统异常请重试!");
                //判断异常类型是否是自定义异常
                if (e instanceof ParamsException){
                    ParamsException p =(ParamsException) e;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());

                }else if (e instanceof AuthException) { // 认证异常
                    AuthException a = (AuthException) e;
                    resultInfo.setCode(a.getCode());
                    resultInfo.setMsg(a.getMsg());
                }

                //设置响应类型和编码格式 （响应JSON格式的数据） 用流输出可能会中文乱码 所以先设置编码格式
                response.setContentType("application/json;charset=utf-8");
                // 得到字符输出流
                PrintWriter out =null;
                try {
                    //得到输入流
                    out = response.getWriter();
                    //将需要返回的对象转换成JSON格式的字符串
                    String json = JSON.toJSONString(resultInfo);
                    //输出数据
                    out.write(json);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                finally {
                    //如果对象不为空则关闭
                    if ( out !=null){
                        out.close();
                    }
                }
                return  null;

            }
        }

        return modelAndView;
    }
}
