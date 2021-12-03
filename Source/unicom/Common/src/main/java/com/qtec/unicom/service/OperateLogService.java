package com.qtec.unicom.service;

import com.github.pagehelper.PageInfo;
import com.qtec.unicom.pojo.OperateLog;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by duhc on 2017/10/18.
 */
@Service
public interface OperateLogService {
    int insertOperateLog(OperateLog log);
    int insertFailOperateLog(OperateLog log);
    List<OperateLog> getOperateLogs(Map pamars);
    PageInfo<OperateLog> getOperateLogs1(Map pamars);

    List<OperateLog>  getOperateLogsForDeviceInfoShow();

}
