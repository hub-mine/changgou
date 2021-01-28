package com.changgou.exception;

import com.changgou.util.Result;
import com.changgou.util.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice //获取异常
public class BaseExceptionHandler {
    /**
     * 异常处理
     */
    @ExceptionHandler(value = Exception.class)  //设置捕获什么类型的异常
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR,e.getMessage());
    }

}
