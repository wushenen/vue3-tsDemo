package com.unicom.quantum.service.impl;

import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.component.ResultHelper;
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
    public int addIp(String ipInfo) throws QuantumException {
        if (Init.IP_WHITE_SET.contains(ipInfo))
            throw new QuantumException(ResultHelper.genResult(1,"该IP信息已添加，勿重复添加"));
        if (ipInfo.contains("/")) {
            Integer netmask = Integer.valueOf(ipInfo.substring(ipInfo.indexOf("/") + 1));
            if (netmask < 0 || netmask > 32)
                throw new QuantumException(ResultHelper.genResult(1,"请参照提示信息添加IP信息"));
        }else {
            if (!IpWhiteCheckUtil.validate(ipInfo))
                throw new QuantumException(ResultHelper.genResult(1,"请参照提示信息添加IP信息"));
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
        ipMapper.deleteIpById(ipInfo);
        return 0;
    }
}
