package com.qtec.unicom.service.impl;


import com.qtec.unicom.component.annotation.OperateLogAnno;
import com.qtec.unicom.component.util.*;
import com.qtec.unicom.mapper.CardDataMapper;
import com.qtec.unicom.mapper.QkmVersionMapper;
import com.qtec.unicom.pojo.CardData;
import com.qtec.unicom.pojo.LinuxServer;
import com.qtec.unicom.pojo.QkmVersion;
import com.qtec.unicom.service.SystemMangeService;
import com.sansec.device.crypto.MgrException;
import com.sansec.jce.provider.JCESM2PublicKey;
import com.sansec.jce.provider.asymmetric.ec.KeyPairGenerator;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Service
public class SystemMangeServiceImpl implements SystemMangeService {
    private static final Logger logger = LoggerFactory.getLogger(SystemMangeServiceImpl.class);

    @Value("${cmdPath}")
    private String cmdPath;
    @Value("${managerPath}")
    private String managerPath;
    @Value("${serverPath}")
    private String serverPath;
    @Value("${spring.datasource.druid.password}")
    private String mysqlPassword;
    @Value("${spring.datasource.host}")
    private String mysqlHost;
    @Autowired
    private QkmVersionMapper qkmVersionMapper;
    @Autowired
    private CardDataMapper cardDataMapper;

    @OperateLogAnno(operateDesc = "修改ip,网关,子网掩码", operateModel = "系统管理模块")
    @Override
    public String updateIpNetmaskAndGateway(LinuxServer linuxServer) throws Exception {
        ArrayList<String> commands = new ArrayList<>(4);
        commands.add("/bin/bash");
        commands.add("-c");
        StringBuilder sb = new StringBuilder("/bin/sh "+cmdPath+"changeip.sh \"" + linuxServer.getIp() + "\" ");
        String netMask = linuxServer.getNetMask();
        String gateWay = linuxServer.getGateWay();
        String nicName = linuxServer.getNicName();
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
        mysqlHost = linuxServer.getIp();
        return res.get();
    }
    @OperateLogAnno(operateDesc = "获取系统版本信息", operateModel = "系统管理模块")
    @Override
    public QkmVersion getQkmVersion() throws Exception {
        String macAddr = NetworkUtil.getLinuxMACAddress();
        QkmVersion kv = qkmVersionMapper.getQkmVersion(macAddr);
        if(kv ==null){
            //新增版本表
            kv = new QkmVersion();
            kv.setMacAddr(macAddr);
            if(!initKeyPair(kv)){
                return null;
            }
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

        if(1 ==SwsdsTools.getKeyIndex()){
            String ml = "sh "+ cmdPath +"addSymmetric.sh "+" 1"+" 128 "+cmdPath;
            CmdUtil.executeLinuxCmd(ml);
        }
        if(1 == SwsdsTools.getKeyPairIndex(1)){
            SwsdsTools.generateKeyPair(1,1);
            SwsdsTools.generateKeyPair(1,2);
        }
        qkmVersionMapper.updateStateOk(kv.getMacAddr());
        cardDataMapper.initSql();
        return "0";
    }
    public boolean initKeyPair(QkmVersion kv) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException {
        byte[] b = new byte[1024 * 1024];
        String zo = "";
        //读取pwsp-0.0.1-SNAPSHOT.jar
        InputStream in = new BufferedInputStream(new FileInputStream(serverPath));

        while (in.read(b) != -1) {
            byte[] m = SM3Util.hash(b);
            zo += HexUtils.bytesToHexString(m);
        }
        in.close();
        //读取pwspManager-0.0.1-SNAPSHOT.war
        in = new BufferedInputStream(new FileInputStream(managerPath));
        while (in.read(b) != -1) {
            byte[] m = SM3Util.hash(b);
            zo += HexUtils.bytesToHexString(m);
        }
        in.close();
        //1、生成sm2密钥对
        KeyPair keyPair = KeyPairGenerator.getInstance("SM2", "SwxaJCE").generateKeyPair();
        //用sm2私钥对两个war包做签名(加密)，得到签名数据
        Signature signature = Signature.getInstance("SM3WithSM2", "SwxaJCE");
        signature.initSign(keyPair.getPrivate());
        signature.update(zo.getBytes());
        byte[] out = signature.sign();
        String sign = HexUtils.bytesToHexString(out);
        System.out.println("出厂签名：" + sign);
        //然后将签名数据+两个war包数据，做sm3哈希
        byte[] m = SM3Util.hash(HexUtils.hexStringToBytes(sign + zo));
        String sm3 = HexUtils.bytesToHexString(m);
        System.out.println("出厂sm3哈希：" + sm3);
        JCESM2PublicKey sm2Pub = (JCESM2PublicKey) keyPair.getPublic();
        String pubX = sm2Pub.getW().getAffineX().toString(16);
        String pubY = sm2Pub.getW().getAffineY().toString(16);
        String pubKeyString = pubX + pubY;
        System.out.println("出厂pubKeyString = " + pubKeyString);
        Signature signatureVerify1 = Signature.getInstance("SM3WithSM2", "SwxaJCE");
        signatureVerify1.initVerify(keyPair.getPublic());
        signatureVerify1.update(zo.getBytes());
        boolean flag1 = signatureVerify1.verify(out);
        System.out.println("出厂验签: " + flag1);
        kv.setSignWar(sign);
        kv.setVerifyWar(sm3);
        kv.setPublicKey(pubKeyString);
        return flag1;
    }
    @OperateLogAnno(operateDesc = "密码卡备份", operateModel = "系统管理模块")
    @Override
    public void backUp(String backPass) throws Exception {
        String macAddr = NetworkUtil.getLinuxMACAddress();
        byte[] bk = SwsdsTools.backup(backPass);

        String sqlData = backSql();

        CardData cardData = new CardData();
        cardData.setCardData(bk);
        cardData.setMacAddr(macAddr);
        List<CardData> cardDataList = cardDataMapper.listCardData(macAddr);
        int cardVersion =1;
        if(cardDataList.size() != 0){
            cardVersion = cardDataList.get(0).getCardVersion()+1;
        }
        cardData.setCardVersion(cardVersion);
        cardData.setSqlData(sqlData);
        cardDataMapper.addCardData(cardData);
    }
    private String backSql() throws Exception{
        StringBuilder sb = new StringBuilder("mysqldump -h"+mysqlHost+" -uroot -p"+ mysqlPassword +" mixedquantum t_primary_key t_key_version t_key_alias t_secret > /opt/backup.sql");
        String[] command = {"/bin/sh","-c",sb.toString()};
        Runtime.getRuntime().exec(command);
        Thread.sleep(5000);
        StringBuilder sqlData = new StringBuilder();
        BufferedReader reader = null;
        File file = new File("/opt/backup.sql");
        try{
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString=reader.readLine())!=null){
                sqlData.append(tempString);
                sqlData.append("\n");
            }
            reader.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (reader != null){
                try{
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return Base64.encodeBase64String(sqlData.toString().getBytes());
    }


    @OperateLogAnno(operateDesc = "密码卡还原", operateModel = "系统管理模块")
    @Override
    public String restore(CardData cardData) throws Exception {
        CardData cardData1 = cardDataMapper.getCardData(cardData);
        restoreSql(cardData1.getSqlData());
        try{
            SwsdsTools.restore(cardData1.getCardData(),cardData.getBackPass());
            StringBuilder sb = new StringBuilder("mysql -h"+mysqlHost+" -uroot -p"+ mysqlPassword +" mixedquantum < /opt/restore.sql");
            String[] command = {"/bin/sh","-c",sb.toString()};
            Runtime.getRuntime().exec(command);
        }catch (MgrException e){
            return e.getMessage();
        }
        return "0";
    }
    private void restoreSql(String sqlData) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("/opt/restore.sql");
            try {
                out.write(Base64.decodeBase64(sqlData),0,Base64.decodeBase64(sqlData).length);
                out.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<CardData> listCardData(CardData cardData) throws Exception {
        List<CardData> cardDataList = cardDataMapper.listCardData(cardData.getMacAddr());
        return cardDataList;
    }

}




