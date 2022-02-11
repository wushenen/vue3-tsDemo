package com.unicom.quantum.component;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * code 为0表示成功，-1
 */
public class ResultHelper {

    private static final Logger logger = LoggerFactory.getLogger(ResultHelper.class);

    private final static int SUCCESS =0; //成功


    public static Result genResult(int code, String msg, Object data){
        if (data==null){
            data = new JSONObject();
        }
        Result result = Result.builder().requestId().code(code)
                .msg(msg).data(data).build();
        logger.info("requestId:{}",result.getRequestId());
        logger.info("code:{}",result.getCode());
        logger.info("data:{}",result.getData());
        logger.info("message:{}",result.getMsg());
        return result;
    }
    public static Result genResult(int code, String msg){
        Result result = Result.builder().requestId().code(code).data(new JSONObject())
                .msg(msg).build();
        logger.info("requestId:{}",result.getRequestId());
        logger.info("message:{}",result.getMsg());
        return result;
    }
    public static Result genResultWithSuccess(){
        Result result = Result.builder().requestId().data(new JSONObject()).code(SUCCESS).build();
        logger.info("requestId:{}",result.getRequestId());
        return result;
    }
    public static Result genResultWithSuccess(Object data){
        if (data==null){
            data= new JSONObject();
        }
        Result result =  Result.builder().requestId().code(SUCCESS).data(data).build();
        logger.info("requestId:{}",result.getRequestId());
        return result;
    }

    public static Result genResult(int code){
        Result result = Result.builder().requestId().code(code).data(new JSONObject())
                .build();
        logger.info("requestId:{}",result.getRequestId());
        logger.info("code:{}",result.getCode());
        return result;
    }

}
