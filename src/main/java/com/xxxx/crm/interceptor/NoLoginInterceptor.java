package com.xxxx.crm.interceptor;

import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.exceptions.NoLoginException;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 非法访问拦截
 *  继承HandlerInterceptorAdapter 适配器
 */

public class NoLoginInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private UserMapper userMapper;
    private UserService userService;

    /**
     * 重写方法快捷键  Ctrl+O
     *
     *   拦截用户是否登录状态
     *   在目标资源执行前 (执行的方法)
     *   返回布尔类型：
     *          如果返回true，表示目标方法可以被执行，
     *          如果返回false ，表示阻止目标方法
     *
     *   如何判断用户是否是登录状态：
     *                 1、 判断cookie中是否存在用户信息（获取用户ID）
     *                 2、 数据库中是否存在指定用户ID
     *
     *     如果用户是登录状态，则允许目标方法执行，如果用户是未登录状态，则抛出异常(在全局异常中判断，如果是未登录异常，则跳转到登录界面)
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取cookie中的用户ID
        Integer userId= LoginUserUtil.releaseUserIdFromCookie(request);
        //判断用户ID是否为空且数据库中是否有用户信息
        if (null==userId || null==userMapper.selectByPrimaryKey(userId)){
                //抛出未登录异常
            throw  new NoLoginException();
        }
        return true;
    }
}
