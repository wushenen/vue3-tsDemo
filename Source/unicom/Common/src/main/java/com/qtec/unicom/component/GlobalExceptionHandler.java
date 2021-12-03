package com.qtec.unicom.component;

import com.qtec.unicom.component.Exception.PwspException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /*@ExceptionHandler(Exception.class)
    @ResponseBody //在返回自定义相应类的情况下必须有，这是@ControllerAdvice注解的规定
    public JSONObject exceptionHandler(Exception e, HttpServletResponse response) {

        if(e instanceof PwspException){
            PwspException pwspE = (PwspException)e;
            logger.error(pwspE.getResult().toString());
            JSONObject result = pwspE.getResult();
            return result;
        }
//        e.printStackTrace();
        logger.error(e.getMessage(),e);
        return JSONResult.genFailResult(500,"InternalFailure",e.getMessage());
    }*/

    @ExceptionHandler(Exception.class)
    @ResponseBody //在返回自定义相应类的情况下必须有，这是@ControllerAdvice注解的规定
    public Result exceptionHandler(Exception e, HttpServletResponse response) {
        if(e instanceof PwspException){
            PwspException pwspE = (PwspException)e;
            logger.error(pwspE.getErrorResult().toString());
            Result result = pwspE.getErrorResult();
            return result;
        }
//        e.printStackTrace();
        System.out.println("-----error-----------------");
        logger.error(e.getMessage(),e);
        return ResultHelper.genResult(500,"InternalFailure",e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)//UnauthenticatedException
    @ResponseBody //在返回自定义相应类的情况下必须有，这是@ControllerAdvice注解的规定
    public Result UnauthorizedException(Exception e, HttpServletResponse response) {
        // 记录日志
//        e.printStackTrace();
        logger.error(e.getMessage(),e);
        return ResultHelper.genResult(403,"该用户没权限");
    }
    @ExceptionHandler(UnauthenticatedException.class)//
    @ResponseBody //在返回自定义相应类的情况下必须有，这是@ControllerAdvice注解的规定
    public Result UnauthenticatedException(Exception e, HttpServletResponse response) {
        // 记录日志
//        e.printStackTrace();
        logger.error(e.getMessage(),e);
        return ResultHelper.genResult(401,"请重新登陆");
    }

}
