/**
 * Copyright (C), 2019
 * FileName: MyInterceptor
 * Author:   zhangjian
 * Date:     2019/9/2 18:29
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.zj.stream.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class MyInterceptor implements HandlerInterceptor {
    private Long startTime;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle启动");

        startTime = new Date().getTime();
        System.out.println("controller层类名："+((HandlerMethod) handler).getBean().getClass().getName().toString());  // 获取处理请求的类名称
        System.out.println("controller层方法名："+((HandlerMethod) handler).getMethod().getName());  // 获取处理请求的方法名

        System.out.println("preHandle结束");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle启动");

        //long startTime = (long) request.getAttribute("startTime");
        System.out.println("my interceptor 耗时：" + (new Date().getTime() - startTime));

        System.out.println("postHandle结束");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
        System.out.println("afterCompletion启动");

        //long startTime = (long) request.getAttribute("startTime");
        System.out.println("my interceptor 耗时：" + (new Date().getTime() - startTime));

        System.out.println("afterCompletion启动" );
    }

}