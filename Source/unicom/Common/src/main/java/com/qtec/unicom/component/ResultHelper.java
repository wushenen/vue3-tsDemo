package com.qtec.unicom.component;

import com.alibaba.fastjson.JSONObject;

/**
 * code 为0表示成功，-1
 */
public class ResultHelper {
    private final static int SUCCESS =0; //成功


    public static Result genResult(int code, String msg, Object data){
        if (data==null){
            data = new JSONObject();
        }
        Result result = Result.builder().code(code)
                .msg(msg).data(data).build();
        return result;
    }
    public static Result genResult(int code, String msg){
        Result result = Result.builder().code(code).data(new JSONObject())
                .msg(msg).build();
        return result;
    }
    public static Result genResultWithSuccess(){
        Result result = Result.builder().data(new JSONObject()).code(SUCCESS).build();
        return result;
    }
    public static Result genResultWithSuccess(Object data){
        if (data==null){
            data= new JSONObject();
        }

        Result result =  Result.builder().code(SUCCESS).data(data).build();
        return result;
    }

    public static Result genResult(int code){
        Result result = Result.builder().code(code).data(new JSONObject())
                .build();
        return result;
    }

}
