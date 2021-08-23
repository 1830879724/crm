package com.xxxx.crm.aspect;

import com.xxxx.crm.annoation.RequiredPermission;
import com.xxxx.crm.exceptions.AuthException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Component
@Aspect
public class PermissionProxy {

    @Resource
    private HttpSession session;
    /**
     * 切面拦截指定包下的指定注解
     *  拦截com.xxxx.crm.annoation.RequiredPermission注解
     * @param pjp
     * @return
     */
    //环绕通知 强大的通知
    @Around(value = "@annotation(com.xxxx.crm.annoation.RequiredPermission)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object obj =null;
        //得到当前登录用户拥有的权限（session作用域中）
        List<String> lists= (List<String>) session.getAttribute("lists");
        //判断用户是否拥有权限
        if (null == lists || lists.size()<1){
            //抛出认证异常
            throw new AuthException();
        }
        //得到对应的目标方法
        MethodSignature methodSignature= (MethodSignature) pjp.getSignature();
        //得到方法上的注解
        RequiredPermission requiredPermission=methodSignature.getMethod().getDeclaredAnnotation(RequiredPermission.class);
        //判断注解上对应的状态码
        if (!(lists.contains(requiredPermission.code()))){
            //如果权限中不包含当前方法的权限码则抛出异常
            throw  new AuthException();
        }
        obj =pjp.proceed();
        return obj;

    }
    //切面定义切入点
}
