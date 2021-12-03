package com.qtec.unicom.service.impl;

import com.qtec.unicom.component.annotation.OperateLogAnno;
import com.qtec.unicom.component.init.Init;
import com.qtec.unicom.mapper.IpMapper;
import com.qtec.unicom.pojo.IpInfo;
import com.qtec.unicom.service.IpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IpServiceImpl implements IpService {

    private final String OPERATE_MODEL = "访问控制模块";

    @Autowired
    private IpMapper ipMapper;

    @OperateLogAnno(operateDesc = "添加IP权限", operateModel = OPERATE_MODEL)
    @Override
    public int addIp(String ipInfo) {
        if (Init.IP_WHITE_SET.contains(ipInfo)) {
            return 2;
        }
        Init.IP_WHITE_SET.add(ipInfo);
        return ipMapper.addIp(ipInfo);
    }

    @Override
    public List<IpInfo> getAllIps() {
        return ipMapper.getAllIps();
    }

    @OperateLogAnno(operateDesc = "删除IP权限", operateModel = OPERATE_MODEL)
    @Override
    public int deleteIpById(String ipInfo) {
        Init.IP_WHITE_SET.remove(ipInfo);
        return ipMapper.deleteIpById(ipInfo);
    }
}
