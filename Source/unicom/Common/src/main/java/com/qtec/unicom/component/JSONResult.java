package com.qtec.unicom.component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.qtec.unicom.pojo.PageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class JSONResult {
    private static final Logger logger = LoggerFactory.getLogger(JSONResult.class);

    public static JSONObject genResult(int code, String msg, Object data){
        JSONObject object = new JSONObject();
        object.put("code",code);
        object.put("msg",msg);
        if(data instanceof  String){
            if(((String) data).startsWith("{")){//json字符串
                data = JSONObject.parse(data+"");
            }else{
                object.put("data",data) ;
                data = new JSONObject();
            }
        }else if(data instanceof List){
            object.put("data",data);
            data = new JSONObject();
        }
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(data);
        object.putAll(jsonObject);
        return object;
    }
    public static JSONObject genResult(int code, String msg){
        JSONObject object = new JSONObject();
        object.put("code",code);
        object.put("msg",msg);
        return object;
    }
    public static JSONObject genSuccessResult(){
        JSONObject object = new JSONObject();
        String requestId = UUID.randomUUID().toString();
        logger.debug("requestId:{}",requestId);
        object.put("requestId", requestId);
        object.put("httpStatus", 200);

        return object;
    }
    public static JSONObject genSuccessResult(Object data){
        JSONObject object = (JSONObject)JSONObject.toJSON(data);
        String requestId = UUID.randomUUID().toString();
        logger.debug("requestId:{}",requestId);
        logger.debug(requestId+"|返回数据:data:{}",data);

        object.put("requestId", requestId);
        object.put("httpStatus", 200);

//        String s = JSON.toJSONString(object);
//        object = JSON.parseObject(s);
        return object;
    }
    public static JSONObject genSuccessResultList(PageInfo<Object> pageInfo,String arrName){
        PageVo pageVo = new PageVo();
        pageVo.setPageNumber(pageInfo.getPageNum());
        pageVo.setPageSize(pageInfo.getPageSize());
        pageVo.setTotalCount(Integer.parseInt(pageInfo.getTotal()+""));

        JSONObject object = (JSONObject)JSONObject.toJSON(pageVo);
        object.put(arrName,pageInfo.getList());
        return JSONResult.genSuccessResult(object);
    }
    public static JSONObject genFailResult(int httpStatus,String code,String message){
        JSONObject object = new JSONObject();
        String requestId = UUID.randomUUID().toString();
        logger.debug("requestId:{}",requestId);
        object.put("requestId", requestId);
        object.put("httpStatus", httpStatus);
        object.put("msg", message);
        object.put("code", code);
        return object;
    }

}
