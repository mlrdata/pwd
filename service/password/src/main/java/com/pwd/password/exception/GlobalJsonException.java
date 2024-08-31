package com.pwd.password.exception;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalJsonException  {

    @ExceptionHandler(Exception.class)
    public Map handlerException(Exception e){
        Map map=new HashMap();
        map.put("code","500");
        map.put("msg",e.getMessage());
        return map;
    }


}
