package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.OperateLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by duhc on 2017/10/18.
 */
@Mapper
@Repository
public interface OperateLogMapper {

    int insertOperateLog(@Param("operateLog") OperateLog log);
    List<OperateLog> getOperateLogs(Map pamars);

    List<OperateLog>  getOperateLogsForDeviceInfoShow();
}
