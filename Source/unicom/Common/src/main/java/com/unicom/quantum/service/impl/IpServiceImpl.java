package com.unicom.quantum.service.impl;

import com.unicom.quantum.component.annotation.OperateLogAnno;
import com.unicom.quantum.component.init.Init;
import com.unicom.quantum.component.util.IpWhiteCheckUtil;
import com.unicom.quantum.mapper.IpMapper;
import com.unicom.quantum.pojo.IpInfo;
import com.unicom.quantum.service.IpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IpServiceImpl implements IpService {

    private final String OPERATE_MODEL = "访问控制模块";

    @Autowired
    private IpMapper ipMapper;

    @OperateLogAnno(operateDesc = "添加IP信息", operateModel = OPERATE_MODEL)
    @Override
    public int addIp(String ipInfo) {
        if (Init.IP_WHITE_SET.contains(ipInfo)) {
            return 2;
        }
        //添加校验过程
        if (ipInfo.contains("/")) {
            Integer netmask = Integer.valueOf(ipInfo.substring(ipInfo.indexOf("/") + 1));
            if (netmask < 0 || netmask > 32)
                return 3;
        }else {
            if (!IpWhiteCheckUtil.validate(ipInfo)) return 3;
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
