package com.qtec.unicom.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qtec.unicom.mapper.OperateLogMapper;
import com.qtec.unicom.pojo.OperateLog;
import com.qtec.unicom.service.OperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by duhc on 2017/10/18.
 */
@Service
public class OperateLogServiceImpl implements OperateLogService {
    @Autowired
    OperateLogMapper operateLogMapper;
    @Override
    public int insertOperateLog(OperateLog log) {
        return operateLogMapper.insertOperateLog(log);
    }

    @Override
    public List<OperateLog> getOperateLogs(Map pamars) {
        if (pamars == null)
        {
            pamars = new HashMap();
        }
        List<OperateLog> list = operateLogMapper.getOperateLogs(pamars);
        return list;
    }

    @Override
    public int insertFailOperateLog(OperateLog log) {
        return operateLogMapper.insertOperateLog(log);
    }

//    @OperateLogAnno(operateDesc = "日志管理", operateModel = "分页查询日志")
	@Override
	public PageInfo<OperateLog> getOperateLogs1(Map pamars) {
		int offset = Integer.parseInt(pamars.get("offset")+"");
		int pageSize = Integer.parseInt(pamars.get("pageSize")+"");
		if (pamars == null){
            pamars = new HashMap();
        }
		/**
         * 分页
         */
        PageHelper.startPage(offset, pageSize);
        List<OperateLog> list = operateLogMapper.getOperateLogs(pamars);
        PageInfo<OperateLog> pageInfo1 = new PageInfo<>(list);
        
		return pageInfo1;
	}

    @Override
    public List<OperateLog> getOperateLogsForDeviceInfoShow() {
        return operateLogMapper.getOperateLogsForDeviceInfoShow();
    }
}
