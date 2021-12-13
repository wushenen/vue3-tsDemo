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

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exceptionHandler(Exception e, HttpServletResponse response) {
        if(e instanceof PwspException){
            PwspException pwspE = (PwspException)e;
            logger.error(pwspE.getErrorResult().toString());
            Result result = pwspE.getErrorResult();
            return result;
        }
        logger.error(e.getMessage(),e);
        return ResultHelper.genResult(500,"InternalFailure",e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public Result UnauthorizedException(Exception e, HttpServletResponse response) {
        logger.error(e.getMessage(),e);
        return ResultHelper.genResult(403,"该用户没权限");
    }
    @ExceptionHandler(UnauthenticatedException.class)//
    @ResponseBody
    public Result UnauthenticatedException(Exception e, HttpServletResponse response) {
        logger.error(e.getMessage(),e);
        return ResultHelper.genResult(401,"请重新登陆");
    }

}
