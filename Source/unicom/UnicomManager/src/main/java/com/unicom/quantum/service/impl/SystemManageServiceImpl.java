package com.unicom.quantum.service.impl;


import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.component.annotation.OperateLogAnno;
import com.unicom.quantum.component.util.CmdUtil;
import com.unicom.quantum.component.util.NetworkUtil;
import com.unicom.quantum.component.util.SwsdsTools;
import com.unicom.quantum.service.SystemManageService;
import com.unicom.quantum.mapper.CardDataMapper;
import com.unicom.quantum.mapper.QkmVersionMapper;
import com.unicom.quantum.pojo.CardData;
import com.unicom.quantum.controller.vo.LinuxServerRequest;
import com.unicom.quantum.pojo.QkmVersion;
import com.sansec.device.crypto.MgrException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Service
public class SystemManageServiceImpl implements SystemManageService {
    private static final Logger logger = LoggerFactory.getLogger(SystemManageServiceImpl.class);

    @Value("${cmdPath}")
    private String cmdPath;
    @Autowired
    private QkmVersionMapper qkmVersionMapper;
    @Autowired
    private CardDataMapper cardDataMapper;

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
        logger.info("执行修改ip的脚本命令--"+sb.toString()+"--");
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
    public QkmVersion getQkmVersion(){
        String macAddr = NetworkUtil.getLinuxMACAddress();
        QkmVersion kv = qkmVersionMapper.getQkmVersion(macAddr);
        String mysqlVersion = qkmVersionMapper.getMysqlVersion();
        if(kv == null){
            kv = new QkmVersion();
            kv.setMacAddr(macAddr);
            kv.setVersion(mysqlVersion);
            qkmVersionMapper.addQkmVersion(kv);
            kv = qkmVersionMapper.getQkmVersion(macAddr);
        }
        return kv;
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
        QkmVersion kv = getQkmVersion();
        if(1 == SwsdsTools.getKeyIndex()){
            String ml = "sh "+ cmdPath +"addSymmetric.sh "+" 1"+" 128 "+cmdPath;
            CmdUtil.executeLinuxCmd(ml);
        }
        if(1 == SwsdsTools.getKeyPairIndex(1)){
            SwsdsTools.generateKeyPair(1,1);
            SwsdsTools.generateKeyPair(1,2);
        }
        if (kv.getState() == 0) {
            qkmVersionMapper.updateStateOk(kv.getMacAddr());
            return "0";
        }else {
            throw new QuantumException(ResultHelper.genResult(1, "系统已经初始化"));
        }
    }


    @OperateLogAnno(operateDesc = "密码卡备份", operateModel = "系统管理模块")
    @Override
    public void backUp(String backPass) throws Exception {
        String macAddr = NetworkUtil.getLinuxMACAddress();
        byte[] bk = SwsdsTools.backup(backPass);
        CardData cardData = new CardData();
        cardData.setCardData(bk);
        cardData.setMacAddr(macAddr);
        List<CardData> cardDataList = cardDataMapper.listCardData(macAddr);
        int cardVersion =1;
        if(cardDataList.size() != 0){
            cardVersion = cardDataList.get(0).getCardVersion()+1;
        }
        cardData.setCardVersion(cardVersion);
        cardDataMapper.addCardData(cardData);
    }

    @OperateLogAnno(operateDesc = "密码卡还原", operateModel = "系统管理模块")
    @Override
    public String restore(CardData cardData) throws Exception {
        CardData cardData1 = cardDataMapper.getCardData(cardData);
        try{
            SwsdsTools.restore(cardData1.getCardData(),cardData.getBackPass());
        }catch (MgrException e){
            throw new QuantumException(ResultHelper.genResult(1,e.getMessage()));
        }
        return "0";
    }


    @Override
    public List<CardData> listCardData(CardData cardData) throws Exception {
        List<CardData> cardDataList = cardDataMapper.listCardData(cardData.getMacAddr());
        return cardDataList;
    }

}




