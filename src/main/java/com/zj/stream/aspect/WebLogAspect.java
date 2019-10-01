///**
// * Copyright (C), 2019
// * FileName: WebLogAspect
// * Author:   zhangjian
// * Date:     2019/8/31 22:31
// * Description: web请求切面
// * History:
// * <author>          <time>          <version>          <desc>
// * 作者姓名           修改时间           版本号              描述
// */
//package com.zj.stream.aspect;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import javax.servlet.http.HttpServletRequest;
//import java.util.Enumeration;
//import java.util.HashMap;
//import java.util.UUID;
//
//
///**
// * 打印日志切片（弃用，使用LogFilter！！！）
// */
//@Aspect
//@Component
//class WebLogAspect {
//
//    private Logger logger = LoggerFactory.getLogger(WebLogAspect.class);
//
//    private String requestId;//请求流水号
//    private long startTime;
//
//    @Pointcut("execution(public * com.zj.stream.controller..*.*(..))")
//    public void webLog() {
//    }
//
//    @Before("webLog()")
//    public void doBefore(JoinPoint joinPoint) throws Throwable {
//        requestId = UUID.randomUUID().toString();
//        startTime = System.currentTimeMillis();
//
//        // 接收到请求，记录请求内容
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//
//        //请求的控制器
//        //MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
//
//        HashMap<String, Object> args = new HashMap<>();
//        Enumeration<String> parameterNames = request.getParameterNames();
//        while(parameterNames.hasMoreElements()){
//            String name = parameterNames.nextElement();
//            String parameter = request.getParameter(name);
//            args.put(name,parameter);
//        }
//
//        //头参数(实际开发中应该取token)
//        HashMap<String, Object> headArgs = new HashMap<>();
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while(headerNames.hasMoreElements()){
//            String name = headerNames.nextElement();
//            String header = request.getHeader(name);
//            headArgs.put(name,header);
//        }
//
//        //打印日志
//        StringBuffer requestBuffer = new StringBuffer();
//        requestBuffer.append("\n").append("===========请求流水号："+requestId+"===========").append("\n")
//                    .append("URL : " + request.getRequestURL().toString()).append("\n")
//                    .append("HTTP_METHOD : " + request.getMethod()).append("\n")
//                    .append("IP : " + request.getRemoteAddr()).append("\n")
//                    //.append("CLASS_METHOD : " + methodSignature.getDeclaringTypeName() + "." + methodSignature.getName()).append("\n")
//                    .append("HEADARGS : "+headArgs).append("\n")
//                    .append("ARGS : "+args).append("\n");
//
//        logger.error(requestBuffer.toString());
//    }
//
//    @AfterReturning(returning = "ret", pointcut = "webLog()")
//    public void doAfterReturning(Object ret) {
//        //打印返回报文
//        long time = System.currentTimeMillis() - startTime;
//        StringBuffer responseBuffer = new StringBuffer();
//        responseBuffer.append("\n").append("RESPONSE : " + ret).append("\n")
//                      .append("Time : "+time+"ms").append("\n");
//
//        logger.error(responseBuffer.toString());
//    }
//
//
//}
//
