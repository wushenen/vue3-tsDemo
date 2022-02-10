package com.cucc.unicom.mapper;

import com.cucc.unicom.controller.vo.OperateLogRequest;
import com.cucc.unicom.pojo.OperateLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OperateLogMapper {
    void insertOperateLog(OperateLog log);
    List<OperateLog> getOperateLogs(OperateLogRequest operateLogRequest);
}
