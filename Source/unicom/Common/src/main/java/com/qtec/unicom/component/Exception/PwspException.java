package com.qtec.unicom.component.Exception;

import com.alibaba.fastjson.JSONObject;
import com.qtec.unicom.component.Result;

public class PwspException extends Exception {
    private JSONObject result;

    private Result errorResult;

    //默认构造器
    public PwspException() {
    }
    //带有详细信息的构造器，信息存储在message中
    public PwspException(String message) {
        super(message);
    }
    //带有详细信息的构造器，信息存储在message中
    public PwspException(JSONObject result) {
        this.result = result;
    }

    public PwspException(Result errorResult) {
        this.errorResult = errorResult;
    }

    public Result getErrorResult() {
        return errorResult;
    }

    public void setErrorResult(Result errorResult) {
        this.errorResult = errorResult;
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }
}
