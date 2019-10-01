/**
 * Copyright (C), 2019
 * FileName: CustomException
 * Author:   zhangjian
 * Date:     2019/9/30 16:37
 * Description: 自定义异常
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.zj.stream.exception;

import com.zj.stream.enums.ResultCode;

public class CustomException extends Exception {
    private static final long serialVersionUID = 5819174480253773214L;

    private ResultCode resultCode;
    private Exception e;

    public CustomException(ResultCode resultCode){
        this.resultCode = resultCode;
    }
    public CustomException(ResultCode resultCode,Exception e){
        this.resultCode = resultCode;
        this.e = e;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public Exception getException() {
        return e;
    }

    public void setException(Exception e) {
        this.e = e;
    }
}
