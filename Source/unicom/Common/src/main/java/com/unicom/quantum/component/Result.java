package com.unicom.quantum.component;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * 公共返回结果
 * @param <T>
 */
@ApiModel("通用返回结果")
public class Result<T> {
    @ApiModelProperty("请求编号")
    private String requestId;
    @ApiModelProperty(value = "结果代码",notes = "0-成功，1-失败")
    private int code;
    @ApiModelProperty("结果信息")
    private String msg;
    @ApiModelProperty("返回数据")
    private T data;
    public static Builder builder(){
        return new Builder();
    }
    public static class Builder<T>{
        private String requestId;
        private int code;
        private String msg;
        private T data;
         public Builder(){

         }

        public Builder requestId() {
            this.requestId = UUID.randomUUID().toString();
            return this;
        }

        public  Builder code(int code){
             this.code = code;
             return this;
        }
        public Builder msg(String msg){
             this.msg = msg;
             return  this;
        }
        public Builder data(T data){
            this.data = data;
            return this;
        }
        public Result build(){
            return new Result(this);
        }
    }

    public Result() {
    }

    private Result(Builder<T> builder){
        this.requestId = builder.requestId;
        this.code = builder.code;
        this.msg = builder.msg;
        this.data = builder.data;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

}
