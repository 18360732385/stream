/**
 * Copyright (C), 2019
 * FileName: LogFilter
 * Author:   zhangjian
 * Date:     2019/10/1 12:17
 * Description: 打印日志过滤器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.zj.stream.filter;

import com.zj.stream.constants.SecretConstant;
import com.zj.stream.util.RequestWrapper;
import com.zj.stream.util.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * 日志过滤器
 */
public class LogFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(LogFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("LogFilter 初始化");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("LogFilter 启动");

        long start = System.currentTimeMillis();

        RequestWrapper requestWrapper = (RequestWrapper) servletRequest;
        ResponseWrapper responseWrapper = (ResponseWrapper) servletResponse;
        
        //头部参数
        HashMap<String, Object> headArgs = new HashMap<>();
        Enumeration<String> headerNames = requestWrapper.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            String header = requestWrapper.getHeader(name);
            headArgs.put(name,header);
        }

        //取出decryptContent
        String decryptContent = requestWrapper.getParameter(SecretConstant.DECRYPTCONTENT);

        HashMap<String, Object> args = new HashMap<>();
        Enumeration<String> parameterNames = requestWrapper.getParameterNames();
        while(parameterNames.hasMoreElements()){
            String name = parameterNames.nextElement();
            String parameter = requestWrapper.getParameter(name);
            args.put(name,parameter);
        }
        System.out.println("agrs: "+args);

        try{
            filterChain.doFilter(requestWrapper,responseWrapper);
        }finally {
            byte[] content = responseWrapper.getResponseData();

            StringBuffer logBuffer = new StringBuffer();
            logBuffer.append("\n").append("===========globalId："+requestWrapper.getAttribute("globalId")+"===========")
                    .append("\n").append("URL : " + requestWrapper.getRequestURL().toString())
                    .append("\n").append("HTTP_METHOD : " + requestWrapper.getMethod())
                    .append("\n").append("IP : " + requestWrapper.getRemoteAddr())
                    .append("\n").append("HEADARGS : "+headArgs)
                    .append("\n").append("ARGS : "+decryptContent)
                    .append("\n").append("RESPONSE : "+new String(content,"UTF-8"))
                    .append("\n").append("TIME : "+(System.currentTimeMillis() - start)+"ms")
                    .append("\n");

            logger.error(logBuffer.toString());
        }

    }

    @Override
    public void destroy() {
        System.out.println("LogFilter 销毁");
    }
}
