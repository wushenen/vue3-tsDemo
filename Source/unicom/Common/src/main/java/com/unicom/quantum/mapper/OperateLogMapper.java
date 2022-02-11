package com.unicom.quantum.mapper;

import com.unicom.quantum.controller.vo.OperateLogRequest;
import com.unicom.quantum.pojo.OperateLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OperateLogMapper {
    void insertOperateLog(OperateLog log);
    List<OperateLog> getOperateLogs(OperateLogRequest operateLogRequest);
    List<String> getOperator();
    List<String> getOperateModel(String operator);
    List<String> getOperateDetail(String operator, String operateModel);
}
