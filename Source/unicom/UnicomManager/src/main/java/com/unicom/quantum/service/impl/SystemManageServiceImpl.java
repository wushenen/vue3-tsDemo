package com.unicom.quantum.service.impl;


import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.component.annotation.OperateLogAnno;
import com.unicom.quantum.component.util.CmdUtil;
import com.unicom.quantum.component.util.NetworkUtil;
import com.unicom.quantum.component.util.SwsdsTools;
import com.unicom.quantum.controller.vo.LinuxServerRequest;
import com.unicom.quantum.mapper.QkmVersionMapper;
import com.unicom.quantum.pojo.QkmVersion;
import com.unicom.quantum.service.SystemManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Service
public class SystemManageServiceImpl implements SystemManageService {
    private static final Logger logger = LoggerFactory.getLogger(SystemManageServiceImpl.class);

    @Value("${cmdPath}")
    private String cmdPath;
    @Autowired
    private QkmVersionMapper qkmVersionMapper;

    @OperateLogAnno(operateDesc = "修改ip,网关,子网掩码", operateModel = "系统管理模块")
    @Override
    public String updateIpNetmaskAndGateway(LinuxServerRequest linuxServerRequest) throws Exception {
        ArrayList<String> commands = new ArrayList<>(4);
        commands.add("/bin/bash");
        commands.add("-c");
        StringBuilder sb = new StringBuilder("/bin/sh "+cmdPath+"changeip.sh \"" + linuxServerRequest.getIp() + "\" ");
        String netMask = linuxServerRequest.getNetMask();
        String gateWay = linuxServerRequest.getGateWay();
        String nicName = linuxServerRequest.getNicName();
        netMask = netMask == null ? "" : netMask;
        gateWay = gateWay == null ? "" : gateWay;
        nicName = nicName == null ? "" : nicName;
        sb.append("\"" + netMask + "\" ");
        sb.append("\"" + gateWay + "\" ");
        sb.append("\"" + nicName + "\"");
        sb.append(" ; echo $?");
        commands.add(sb.toString());
        Future<String> res = CmdUtil.CMD_THREAD_POOL.submit(new Callable<String>() {
            public String call() throws Exception {
                String fu = CmdUtil.execCommand(commands);
                return fu;
            }
        });
        logger.info("修改IP结果:"+res.get());
        if(!"1".equals(res.get())){
            CmdUtil.CMD_THREAD_POOL.submit(new Runnable(){
                @Override
                public void run() {
                    try {
                        CmdUtil.executeLinuxCmd("service network restart");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return "0";
        }
        return res.get();
    }
    @OperateLogAnno(operateDesc = "获取系统版本信息", operateModel = "系统管理模块")
    @Override
    public Map<String,String> getQkmVersion(){
        HashMap<String, String> map = new HashMap<>();
        String macAddr = NetworkUtil.getLinuxMACAddress();
        QkmVersion qkmVersion = qkmVersionMapper.getQkmVersion(macAddr);
        String mysqlVersion = qkmVersionMapper.getMysqlVersion();
        if(qkmVersion == null){
            qkmVersion = new QkmVersion();
            qkmVersion.setMacAddr(macAddr);
            qkmVersion.setVersion(mysqlVersion);
            qkmVersionMapper.addQkmVersion(qkmVersion);
            qkmVersion = qkmVersionMapper.getQkmVersion(macAddr);
        }
        map.put("macAddr",qkmVersion.getMacAddr());
        map.put("mysqlVersion","mysql-"+qkmVersion.getVersion());
        if (qkmVersion.getState() == 0) {
            map.put("systemStatus","初始");
        }else if (qkmVersion.getState() == 1){
            map.put("systemStatus","就绪");
        }else
            map.put("systemStatus","异常");
        return map;
    }

    /**
     * 1.密码卡 对称index为1，非对称第一组初始化
     * 2.生成当前MAC地址与 静态校验参数在版本表
     *
     * @return
     * @throws Exception
     */
    @OperateLogAnno(operateDesc = "系统初始化", operateModel = "系统管理模块")
    @Override
    public String init() throws Exception {
        Map<String, String> qkmVersion = getQkmVersion();
        if(1 == SwsdsTools.getKeyIndex()){
            String ml = "sh "+ cmdPath +"addSymmetric.sh "+" 1"+" 128 "+cmdPath;
            CmdUtil.executeLinuxCmd(ml);
        }
        if(1 == SwsdsTools.getKeyPairIndex(1)){
            SwsdsTools.generateKeyPair(1,1);
            SwsdsTools.generateKeyPair(1,2);
        }
        if ("初始".equals(qkmVersion.get("systemStatus"))) {
            qkmVersionMapper.updateStateOk(qkmVersion.get("macAddr"));
            return "0";
        }else {
            throw new QuantumException(ResultHelper.genResult(1, "系统已经初始化"));
        }
    }

}




