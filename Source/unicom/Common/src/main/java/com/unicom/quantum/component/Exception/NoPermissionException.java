/*
package com.unicom.quantum.component.Exception;

import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class NoPermissionException {

    @ResponseBody
    @ExceptionHandler(AuthorizationException.class)
    public Result auth(Exception e){
        return ResultHelper.genResult(403,"权限不足");
    }

}
*/
