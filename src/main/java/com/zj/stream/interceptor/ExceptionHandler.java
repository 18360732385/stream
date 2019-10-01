/**
 * Copyright (C), 2019
 * FileName: ExceptionHandler
 * Author:   zhangjian
 * Date:     2019/9/30 16:22
 * Description: 全局异常拦截器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.zj.stream.interceptor;

import com.zj.stream.enums.ResultCode;
import com.zj.stream.exception.CustomException;
import com.zj.stream.pojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常拦截器
 */
@RestControllerAdvice
public class ExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(Exception e) {
        ResultCode resultCode = ResultCode.FAILURE;

        //自定义异常返回对应编码
        if(e instanceof CustomException) {
            CustomException ex = (CustomException)e;
            resultCode = ex.getResultCode();
            logger.error("错误码:{}，错误信息:{}，异常:{}",resultCode.code(),resultCode.msg(),ex);
        }else {
            logger.error("错误码:{}，错误信息:{}，异常:{}",resultCode.code(),resultCode.msg(),e);
        }

        return Result.failure(resultCode);
    }
}
