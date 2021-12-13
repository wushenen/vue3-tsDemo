package com.qtec.unicom.service.impl;

import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.component.util.HexUtils;
import com.qtec.unicom.component.Exception.PwspException;
import com.qtec.unicom.component.annotation.OperateLogAnno;
import com.qtec.unicom.component.init.SelfExaminationParam;
import com.qtec.unicom.component.util.*;
import com.qtec.unicom.mapper.KeyVersionMapper;
import com.qtec.unicom.mapper.MaterialMapper;
import com.qtec.unicom.mapper.PrimaryKeyMapper;
import com.qtec.unicom.pojo.KeyVersion;
import com.qtec.unicom.pojo.Material;
import com.qtec.unicom.pojo.PrimaryKey;
import com.qtec.unicom.pojo.dto.PrimaryKeyDTO;
import com.qtec.unicom.service.PrimaryKeyService;
import com.sansec.device.crypto.CryptoException;
import com.sansec.device.crypto.MgrException;
import com.sansec.jce.provider.JCESM2PrivateKey;
import com.sansec.jce.provider.JCESM2PublicKey;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qtec.src.SM9KeyGenerationCenter;

import java.lang.reflect.InvocationTargetException;
import java.security.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class PrimaryKeyServiceImpl implements PrimaryKeyService {

    private final String OPERATE_MODEL = "密钥管理模块";

    private static final Logger logger = LoggerFactory.getLogger(PrimaryKeyServiceImpl.class);

    @Autowired
    private PrimaryKeyMapper primaryKeyMapper;
    @Autowired
    private KeyVersionMapper keyVersionMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private UtilService utilService;
/*    @Autowired
    private AliasMapper aliasMapper;*/
    @Autowired
    private com.qtec.unicom.service.SM9Service SM9Service;

    @Value("${cmdPath}")
    private String cmdPath;

    /**
     * 密码卡生成随机数密钥，导入密码卡，并新增主密钥表和密钥版本表数据
     * 1.只有对称密钥存在 外部导入密钥材料、轮转的功能
     * 2.外部导入时没有轮转功能
     * @param primaryKey
     * @return
     * @throws Exception
     */
    @OperateLogAnno(operateDesc = "新增主密钥", operateModel = OPERATE_MODEL)
    @Transactional
    @Override
    public PrimaryKey addKey(PrimaryKey primaryKey) throws Exception {
        String keyId = UUID.randomUUID().toString();
        String keyVersionId = UUID.randomUUID().toString();

        primaryKey.setKeyState("Enabled");
        primaryKey.setKeyId(keyId);
        primaryKey.setPrimaryKeyVersion(keyVersionId);

        KeyVersion keyVersion = new KeyVersion();
        keyVersion.setKeyId(primaryKey.getKeyId());
        keyVersion.setKeyVersionId(primaryKey.getPrimaryKeyVersion());
        primaryKey.setLastRotationDate(new Date());
        keyVersion.setCreator(primaryKey.getCreator());
        if (primaryKey.getProtectionLevel() == null) {
            primaryKey.setProtectionLevel("SOFTWARE");
        }
        switch (primaryKey.getProtectionLevel()) {
            case "HSM":
                createHSMKey(primaryKey,keyVersion);
                break;
            case "SOFTWARE":
                createSoftWareKey(primaryKey,keyVersion);
                break;
            default://默认SOFTWARE
                throw new PwspException(ResultHelper.genResult(400,"<保护级别>无效"));
        }
        int num = primaryKeyMapper.addKey(primaryKey);
        //所有秘钥都要插入密钥版本表，为了方便后续查找密钥版本
        keyVersionMapper.addKeyVersion(keyVersion);
        return primaryKey;
    }

    /**
     * 保护级别SOFTWARE，Origin只能为QTEC_KMS，不用导入密码卡（没有cardindex）
     * @param primaryKey
     * @return
     */
    public void createSoftWareKey(PrimaryKey primaryKey,KeyVersion keyVersion) throws Exception {
        if(!"QTEC_KMS".equalsIgnoreCase(primaryKey.getOrigin())){
            throw new PwspException(ResultHelper.genResult(400,"<材料来源>无效"));
        }
        switch (primaryKey.getKeySpec()==null?"":primaryKey.getKeySpec()){
            case "EC_SM2"://密码卡  kpType 1-加密；2-签名
                enableAutomaticRotation(primaryKey);
                primaryKey.setAutomaticRotation("Disabled");
                primaryKey.setRotationInterval(null);
                KeyPair kp = genKeyPair();
                if(!"ENCRYPT/DECRYPT".equalsIgnoreCase(primaryKey.getKeyUsage()) &&
                    !"SIGN/VERIFY".equalsIgnoreCase(primaryKey.getKeyUsage())){
                    throw new PwspException(ResultHelper.genResult(400,"<密钥用途>无效"));
                }
                JCESM2PrivateKey pri = (JCESM2PrivateKey)kp.getPrivate();
                JCESM2PublicKey pub = (JCESM2PublicKey)kp.getPublic();
                String priHex = pri.getS().toString(16);
                String pubHex = pub.getW().getAffineX().toString(16)+pub.getW().getAffineY().toString(16);
                //index为1的主密钥加密
                byte[] priKeyCipher = SwsdsTools.SDF_Encrypt1(priHex.getBytes("UTF-8"),1);
                keyVersion.setPriKeyData(priKeyCipher);
                keyVersion.setPubKeyData(SwsdsTools.SDF_Encrypt1(pubHex.getBytes("UTF-8"),1));
                break;
            case "EC_SM9"://密码卡  kpType 1-加密；2-签名
                enableAutomaticRotation(primaryKey);
                primaryKey.setAutomaticRotation("Disabled");
                primaryKey.setRotationInterval(null);
                SM9KeyGenerationCenter kgc = SM9Service.getKGC();
                String sm9PriKey;
                String sm9PubKey;
                if("ENCRYPT/DECRYPT".equalsIgnoreCase(primaryKey.getKeyUsage())){
                    sm9PriKey = kgc.getKe().toString(16);
                    sm9PubKey = HexUtils.bytesToHexString(kgc.getPpube().toBytes());
                }else if("SIGN/VERIFY".equalsIgnoreCase(primaryKey.getKeyUsage())){
                    sm9PriKey = kgc.getKs().toString(16);
                    sm9PubKey = HexUtils.bytesToHexString(kgc.getPpubs().toBytes());
                }else{
                    throw new PwspException(ResultHelper.genResult(400,"<密钥用途>无效"));
                }
                //index为1的主密钥加密
                priKeyCipher = SwsdsTools.SDF_Encrypt1(sm9PriKey.getBytes("UTF-8"),1);
                keyVersion.setPriKeyData(priKeyCipher);
                keyVersion.setPubKeyData(SwsdsTools.SDF_Encrypt1(sm9PubKey.getBytes("UTF-8"),1));
                break;
            case "QTEC_SM4":
                if("".equalsIgnoreCase(primaryKey.getKeyUsage())||primaryKey.getKeyUsage() ==null){
                    primaryKey.setKeyUsage("ENCRYPT/DECRYPT");
                }
                if(!"ENCRYPT/DECRYPT".equalsIgnoreCase(primaryKey.getKeyUsage())){
                    throw new PwspException(ResultHelper.genResult(400,"“QTEC_SM4”类型仅支持“加密/解密”"));
                }
                //新增密钥版本表数据
                byte[] random = utilService.generateQuantumRandom(16);
                //index为1的主密钥加密
                byte[] randomM = SwsdsTools.SDF_Encrypt1(HexUtils.bytesToHexString(random).getBytes("UTF-8"),1);
                keyVersion.setKeyData(randomM);
                //自动轮转判断
                enableAutomaticRotation(primaryKey);
                break;
            default:
                throw new PwspException(ResultHelper.genResult(400,"<密钥类型>无效"));
        }
    }

    /**
     * 最多循环5次，重新生成密钥对
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws InvalidKeyException
     * @throws PwspException
     */
    public KeyPair genKeyPair() throws Exception {
        for (int i=0;i<5;i++){
            KeyPair kp = SwsdsTools.generateKeyPair();
            try{
                if(verityKeyPair(kp)){
                    logger.info("生成密钥对次数{}",(i+1));
                    return kp;
                }
            }catch (Exception e){
                logger.error(e.getMessage(),"|生成密钥对失败！");
            }
        }
        throw new PwspException(ResultHelper.genResult(500,"密码卡出错，请重新生成"));
    }
    public boolean verityKeyPair(KeyPair kp) throws Exception {
        JCESM2PublicKey pub = (JCESM2PublicKey)kp.getPublic();
        JCESM2PrivateKey pri = (JCESM2PrivateKey)kp.getPrivate();
        String pubHex = pub.getW().getAffineX().toString(16)+pub.getW().getAffineY().toString(16);
        String priHex = pri.getS().toString(16);
        logger.info("..pub..:"+pub.getW().getAffineX().toString(16)+pub.getW().getAffineY().toString(16));
        logger.info("..pri..:"+pri.getS().toString(16));
        byte[] source = HexUtils.hexStringToBytes(SelfExaminationParam.SM2_SIGN_SOURCE);
        byte[] signData = SwsdsTools.sm2Sign(priHex,pubHex,source);
        boolean flag = SwsdsTools.sm2VerifySign(pubHex,source,signData);
        if(!flag){ return false;}
        //对预置明文加密
        byte[] cipher = SwsdsTools.sm2Encrypt(HexUtils.hexStringToBytes(SelfExaminationParam.SM2_SOURCE),pubHex);
        //SM2解密
        byte[] encSource = SwsdsTools.sm2Decrypt(cipher,priHex);
        flag = SelfExaminationParam.SM2_SOURCE.equalsIgnoreCase(HexUtils.bytesToHexString(encSource));
        return flag;
}
    /**
     * 保护级别 HSM
     * 密码卡生成密钥
     * @param primaryKey
     * @param keyVersion
     * @throws Exception
     */
    public void createHSMKey(PrimaryKey primaryKey,KeyVersion keyVersion) throws Exception {
        switch (primaryKey.getKeySpec()){
            case "EC_SM2"://密码卡  kpType 1-加密；2-签名
                enableAutomaticRotation(primaryKey);
                primaryKey.setAutomaticRotation("Disabled");
                primaryKey.setRotationInterval(null);
                //生成SM2密钥对，在密码卡中
                generateSM2toCard(primaryKey,keyVersion);
                break;
            case "QTEC_SM4":
                //自动轮转判断
                enableAutomaticRotation(primaryKey);
                if("".equalsIgnoreCase(primaryKey.getKeyUsage())||primaryKey.getKeyUsage() ==null){
                    primaryKey.setKeyUsage("ENCRYPT/DECRYPT");
                }
                if(!"ENCRYPT/DECRYPT".equalsIgnoreCase(primaryKey.getKeyUsage())){
                    throw new PwspException(ResultHelper.genResult(400,"“<密钥用途>”无效"));
                }
                if("EXTERNAL".equalsIgnoreCase(primaryKey.getOrigin())){
                    primaryKey.setRotationInterval(null);
                    primaryKey.setAutomaticRotation("Disabled");
                    primaryKey.setKeyState("PendingImport");
                }else if("QTEC_KMS".equalsIgnoreCase(primaryKey.getOrigin())){
                    int index = SwsdsTools.getKeyIndex();
                    String ml = "sh "+ cmdPath +"addSymmetric.sh "+" "+index+" 128 "+cmdPath;
                    CmdUtil.executeLinuxCmd(ml);
                    keyVersion.setCardIndex(index);
                }else{
                    throw new PwspException(ResultHelper.genResult(400,"<材料来源>无效"));
                }
                break;
            default:
                throw new PwspException(ResultHelper.genResult(400,"<密钥类型>无效"));
        }
    }

    /**
     * 自动轮转判断
     * @param primaryKey
     */
    public void enableAutomaticRotation(PrimaryKey primaryKey) throws Exception {
        //自动轮转判断
        if(primaryKey.isEnableAutomaticRotation()){
            if(primaryKey.getKeySpec().startsWith("EC")||"EXTERNAL".equalsIgnoreCase(primaryKey.getOrigin())){
                throw new PwspException(ResultHelper.genResult(400,"此密钥不支持<开启密钥轮转>"));
            }
            primaryKey.setAutomaticRotation("Enabled");
            String rotationInterval = primaryKey.getRotationInterval();
            if(rotationInterval == null){
                throw new PwspException(ResultHelper.genResult(400,"<轮转周期>不能为空"));
            }
            if(0l==DateUtils.integerUnit(primaryKey.getRotationInterval())){
                throw new PwspException(ResultHelper.genResult(400,"<轮转周期>无效"));
            }
        }else{
            //没有自动轮转，忽视时间
            primaryKey.setRotationInterval(null);
        }
    }

    @OperateLogAnno(operateDesc = "开启主密钥", operateModel = OPERATE_MODEL)
    @Override
    public Object enableKey(PrimaryKey primaryKey) throws Exception {
        PrimaryKey rePrimaryKey = getPrimaryKeybyKeyIdORalias(primaryKey.getKeyId());
        keyStatsEnable(rePrimaryKey,2);
        primaryKey.setKeyState("Enabled");
        return primaryKeyMapper.updatePrimaryKeyState(primaryKey);
    }

    @OperateLogAnno(operateDesc = "禁用主密钥", operateModel = OPERATE_MODEL)
    @Override
    public Object disableKey(PrimaryKey primaryKey) throws Exception {
        PrimaryKey rePrimaryKey = getPrimaryKeybyKeyIdORalias(primaryKey.getKeyId());
        keyStatsEnable(rePrimaryKey,2);
        primaryKey.setKeyState("Disabled");
        return primaryKeyMapper.updatePrimaryKeyState(primaryKey);
    }

    @OperateLogAnno(operateDesc = "申请删除主密钥", operateModel = OPERATE_MODEL)
    @Override
    public Object scheduleKeyDeletion(PrimaryKey primaryKey) throws PwspException {
        PrimaryKey rePrimaryKey = getPrimaryKeybyKeyIdORalias(primaryKey.getKeyId());
        keyStatsEnable(rePrimaryKey,3);

        int pendingWindowInDays = primaryKey.getPendingWindowInDays();
        long time = pendingWindowInDays *24*60*60*1000L;
        Date exp = new Date(System.currentTimeMillis() + time);
        primaryKey.setDeleteDate(exp);
        primaryKey.setKeyState("PendingDeletion");
        return primaryKeyMapper.updatePrimaryKeyState(primaryKey);
    }

    @OperateLogAnno(operateDesc = "取消删除主密钥", operateModel = OPERATE_MODEL)
    @Override
    public Object cancelKeyDeletion(PrimaryKey primaryKey) throws Exception {
        PrimaryKey rePrimaryKey = getPrimaryKeybyKeyIdORalias(primaryKey.getKeyId());
        keyStatsEnable(rePrimaryKey,4);
        primaryKey.setKeyState("Enabled");
        if("EXTERNAL".equalsIgnoreCase(rePrimaryKey.getOrigin())){
            Material mat = materialMapper.selectMaterial(rePrimaryKey.getKeyId());
            if(mat == null){
                primaryKey.setKeyState("PendingImport");
            }
        }
        primaryKey.setDeleteDate(null);
        return primaryKeyMapper.updatePrimaryKeyState(primaryKey);
    }

    /**
     * origin为EXTERNAL 时，查询材料表
     * @param primaryKey
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @OperateLogAnno(operateDesc = "主密钥详情", operateModel = OPERATE_MODEL)
    @Override
    public PrimaryKeyDTO describeKey(PrimaryKey primaryKey) throws Exception {
        PrimaryKey result = getPrimaryKeybyKeyIdORalias(primaryKey.getKeyId());
//        keyStatsEnable(result,2);

        PrimaryKeyDTO dto = new PrimaryKeyDTO();
        BeanUtils.copyProperties(dto, result);
        if("EXTERNAL".equalsIgnoreCase(result.getOrigin()) && !"PendingImport".equalsIgnoreCase(result.getKeyState()) && "QTEC_SM4".equalsIgnoreCase(result.getKeySpec())){
            Material material = materialMapper.selectMaterial(primaryKey.getKeyId());
            if(material == null){
                Long expireUnix = material==null?0:material.getKeyMaterialExpireUnix();
                dto.setMaterialExpireTime(expireUnix);            }
        }
        return dto;
    }

    @OperateLogAnno(operateDesc = "列出所有主密钥", operateModel = OPERATE_MODEL)
    @Override
    public List<Map> listKeys(String userName) throws Exception {
        return primaryKeyMapper.listPrimaryKey(userName);
    }

    @Override
    public List<Map> listKeys1(String keyId) throws Exception {
        return primaryKeyMapper.listKeys1(keyId);

    }

    @OperateLogAnno(operateDesc = "修改主密钥描述", operateModel = OPERATE_MODEL)
    @Override
    public void updateKeyDescription(PrimaryKey primaryKey) throws Exception {
        PrimaryKey rePrimaryKey = getPrimaryKeybyKeyIdORalias(primaryKey.getKeyId());
        keyStatsEnable(rePrimaryKey,3);
        primaryKeyMapper.updatePrimaryKeyDesc(primaryKey);
    }

    @OperateLogAnno(operateDesc = "主密钥版本详情", operateModel = OPERATE_MODEL)
    @Override
    public KeyVersion describeKeyVersion(KeyVersion kv) throws Exception {
        KeyVersion keyVersion = keyVersionMapper.getKeyVersion(kv);
        if(keyVersion == null){
            throw new PwspException(ResultHelper.genResult(404,"找不到指定的密钥版本"));
        }
        return keyVersion;
    }

    @OperateLogAnno(operateDesc = "列出主密钥", operateModel = OPERATE_MODEL)
    @Override
    public List<Map> listKeyVersions(KeyVersion kv) throws Exception {
        return keyVersionMapper.listKeyVersion(kv);
    }

    /**
     * 下列情况不允许配置自动轮转策略：
     * 指定的主密钥为非对称密钥。
     * 指定的主密钥为云产品托管的默认密钥。
     * 指定的主密钥为用户自带密钥（外部导入到KMS的密钥）。
     * 指定的主密钥处于Enabled之外的状态
     * 1.创建时未开启：（LastRotationDate = null）
     *      1.1.配置关闭：只改automaticRotation为Disabled
     *      1.1.配置开启：automaticRotation为Enabled，LastRotationDate为new Date() + 新的RotationInterval
     * 2.创建时开启：（LastRotationDate 有值new Date()）
     *      2.1.配置关闭：只改automaticRotation为Disabled
     *      2.1.配置开启：automaticRotation为Enabled，LastRotationDate为LastRotationDate + 新的RotationInterval
     * @param primaryKey
     * @return
     * @throws Exception
     */
    @OperateLogAnno(operateDesc = "配置自动轮转策略", operateModel = OPERATE_MODEL)
    @Override
    public void updateRotationPolicy(PrimaryKey primaryKey) throws Exception {
        PrimaryKey rePrimaryKey = getPrimaryKeybyKeyIdORalias(primaryKey.getKeyId());
        keyStatsEnable(rePrimaryKey,1);

        if(rePrimaryKey.getKeySpec().startsWith("QTEC_")
                && !"EXTERNAL".equalsIgnoreCase(rePrimaryKey.getOrigin())){
            if(primaryKey.isEnableAutomaticRotation()){
                primaryKey.setAutomaticRotation("Enabled");
                //判断之前是否开启过轮转
                Date time = rePrimaryKey.getLastRotationDate()==null?new Date():rePrimaryKey.getLastRotationDate();
                primaryKey.setLastRotationDate(time);
                if(0l==DateUtils.integerUnit(primaryKey.getRotationInterval())){
                    throw new PwspException(ResultHelper.genResult(400,"<轮转周期>无效"));
                }
            }else{
                primaryKey.setAutomaticRotation("Disabled");
                primaryKey.setRotationInterval(null);
            }
            primaryKeyMapper.updateRotationPolicy(primaryKey);
        }else{
            throw new PwspException(ResultHelper.genResult(409,"该密钥无法配置自动轮转策略"));
        }
    }

    /**
     * 仅适用于非对称类型主密钥
     * 且主密钥必须处于开启（Enabled）状态
     * 创建密钥版本的最小间隔为7天
     * 通过LastRotationDate属性查看的7天之内调用此接口将返回错误码Rejected.UnsupportedOperation
     * @param primaryKey
     * @return
     * @throws Exception
     */
    @OperateLogAnno(operateDesc = "创建非对称主密钥版本", operateModel = OPERATE_MODEL)
    @Override
    public KeyVersion createKeyVersion(PrimaryKey primaryKey) throws Exception {
        PrimaryKey rePrimaryKey = getPrimaryKeybyKeyIdORalias(primaryKey.getKeyId());
        keyStatsEnable(rePrimaryKey,1);
        //判断是否7天
        Date lastRotationDate= rePrimaryKey.getLastRotationDate();
        if(rePrimaryKey.getKeySpec().startsWith("QTEC_")
                || lastRotationDate.getTime()+(7*24*60*60*1000) > System.currentTimeMillis()){
            throw new PwspException(ResultHelper.genResult(409,"不支持此操作"));
        }
        String keyVersionId = UUID.randomUUID().toString();
        KeyVersion keyVersion = new KeyVersion();
        keyVersion.setKeyId(primaryKey.getKeyId());
        keyVersion.setKeyVersionId(keyVersionId);
        keyVersion.setCreator(primaryKey.getCreator());
        switch (rePrimaryKey.getProtectionLevel()) {
            case "HSM":
                createHSMKey(rePrimaryKey,keyVersion);
                break;
            default://默认SOFTWARE
                createSoftWareKey(rePrimaryKey,keyVersion);
                break;
        }
        //新增密钥版本
        keyVersionMapper.addKeyVersion(keyVersion);
        //更新主密钥表 的 主密钥版本
        rePrimaryKey.setLastRotationDate(new Date());
        rePrimaryKey.setPrimaryKeyVersion(keyVersionId);
        primaryKeyMapper.updateRotationPolicy(rePrimaryKey);
        keyVersion.setCreationDate(new Date());
        return keyVersion;
    }

    /**
     * 用别名或keyId查询主密钥对象
     * @param keyIdORalias
     * @return
     * @throws PwspException
     */
    @Override
    public PrimaryKey getPrimaryKeybyKeyIdORalias(String keyIdORalias) throws PwspException {
        /*if(keyIdORalias.startsWith("alias/")){
            Alias alias = aliasMapper.getAlias(keyIdORalias);
            if(alias == null){
                throw new PwspException(ResultHelper.genResult(404,"未找到指定密钥"));
            }
            keyIdORalias = alias.getKeyId();
        }*/
        PrimaryKey primaryKey = primaryKeyMapper.getPrimaryKey(keyIdORalias);
        if(primaryKey == null){
            throw new PwspException(ResultHelper.genResult(404,"未找到指定密钥"));
        }
        return primaryKey;
    }

    /**
     * 4种级别
     * 1级最高，只有Enabled不报错
     * 2级：Enabled和Disabled不报错
     * 3级：Enabled和Disabled和PendingImport不报错
     * 4级：PendingDeletion不报错
     * @param primaryKey
     * @return
     * @throws PwspException
     */
    @Override
    public boolean keyStatsEnable(PrimaryKey primaryKey,int level) throws PwspException {
        boolean flag = false;
        switch (primaryKey.getKeyState()){
            case "Enabled":
                if(level != 4){//1,2,3
                    flag = true;
                    break;
                }
                throw new PwspException(ResultHelper.genResult(409,"请求被拒绝，密钥状态为启用"));
            case "Disabled":
                if(level == 2 || level ==3){
                    flag = true;
                    break;
                }
                throw new PwspException(ResultHelper.genResult(409,"请求被拒绝，密钥状态为禁用"));
            case "PendingDeletion":
                if(level == 4){
                    flag = true;
                    break;
                }
                throw new PwspException(ResultHelper.genResult(409,"请求被拒绝，密钥状态为待删除"));
            case "PendingImport":
                if(level == 3){
                    flag = true;
                    break;
                }
                throw new PwspException(ResultHelper.genResult(409,"请求被拒绝，密钥状态为待导入"));
            default:
                    flag = false;
                    break;
        }
        return flag;
    }

    /**
     * 定时任务 删除到期时间的主密钥
     * 如果是HSM 要同时删除密码卡里数据
     * @throws PwspException
     */
    @OperateLogAnno(operateDesc = "定时任务：删除到期主密钥", operateModel = OPERATE_MODEL)
    @Override
    public void autoDeleteKey() throws Exception {
        List<PrimaryKey> list = primaryKeyMapper.listPrimaryKeyOutTime(new Date());
        for (PrimaryKey key:list
             ) {
            if("HSM".equalsIgnoreCase(key.getProtectionLevel()) && !"EXTERNAL".equalsIgnoreCase(key.getOrigin())){
                KeyVersion keyVersion = new KeyVersion();
                keyVersion.setKeyId(key.getKeyId());
                keyVersion.setKeyVersionId(key.getPrimaryKeyVersion());
                keyVersion = keyVersionMapper.getKeyVersion(keyVersion);
                if(keyVersion !=null){
                    int cardIndex = keyVersion.getCardIndex();
                    int type = 3;
                    if(!key.getKeySpec().startsWith("QTEC_")){
                        type = 2;
                        cardIndex = cardIndex%2==0?cardIndex/2:(cardIndex+1)/2;
                    }
                    //调用删除脚本
                    String ml = "sh "+ cmdPath +"deleteKey.sh "+ type +" "+cardIndex +" "+cmdPath;
                    CmdUtil.executeLinuxCmd(ml);
                }
            }
            if("EXTERNAL".equalsIgnoreCase(key.getOrigin())){
                Material material =new Material();
                material.setKeyId(key.getKeyId());
                materialMapper.deleteMaterial(material);
            }
            keyVersionMapper.deleteKeyVersion(key.getKeyId());
        }
        int deleteNum = primaryKeyMapper.deletePrimaryKey(new Date());
        logger.info("需要删除的主密钥个数："+list.size()+" | 自动删除主密钥个数："+deleteNum);
    }

    /**
     * 自动轮转 定时任务
     * 1.查询出所有开启轮转的主密钥
     * 2.new Date(result.getLastRotationDate().getTime()+ DateUtils.integerUnit(result.getRotationInterval())));
     * 上次轮转时间 + 轮转间隔 与 当前时间比较
     * 3.符合轮转条件，创建新版本keyVersion
     * 4.判断是否HSM，HSM需要调用脚本更新密钥
     * @throws Exception
     */
    @OperateLogAnno(operateDesc = "定时任务：轮转主密钥", operateModel = OPERATE_MODEL)
    @Override
    public void automatic() throws Exception {
        List<PrimaryKey> list = primaryKeyMapper.listAutomaticKey();
        for (PrimaryKey key:list
             ) {
            Date lastRotationDate = key.getLastRotationDate();
            String rotationInterval = key.getRotationInterval();
            if(lastRotationDate.getTime()+DateUtils.integerUnit(rotationInterval) <= System.currentTimeMillis()){
                //符合轮转条件
                KeyVersion keyVersion = new KeyVersion();
                keyVersion.setKeyId(key.getKeyId());
                keyVersion.setKeyVersionId(key.getPrimaryKeyVersion());
                keyVersion = keyVersionMapper.getKeyVersion(keyVersion);
                if ("HSM".equalsIgnoreCase(key.getProtectionLevel())){
                    //调用脚本
                    int index = keyVersion.getCardIndex();
                    String ml = "sh "+ cmdPath +"updateSymmetric.sh "+" "+index+" 128 "+cmdPath;
                    CmdUtil.executeLinuxCmd(ml);
                }else{
                    byte[] random = utilService.generateQuantumRandom(16);
                    byte[] randomM = SwsdsTools.SDF_Encrypt1(HexUtils.bytesToHexString(random).getBytes("UTF-8"),1);
                    keyVersion.setKeyData(randomM);
                }
                String keyVersionId = UUID.randomUUID().toString();
                keyVersion.setKeyVersionId(keyVersionId);
                //新增密钥版本
                keyVersionMapper.addKeyVersion(keyVersion);
                //更新主密钥表 的 主密钥版本
                key.setLastRotationDate(new Date());
                key.setPrimaryKeyVersion(keyVersionId);
                primaryKeyMapper.updateRotationPolicy(key);
            }
        }
    }

    /**
     * 生成SM2密钥对，在密码卡中
     * @param primaryKey
     * @param keyVersion
     * @throws CryptoException
     * @throws PwspException
     * @throws MgrException
     */
    public void generateSM2toCard(PrimaryKey primaryKey,KeyVersion keyVersion) throws CryptoException, PwspException, MgrException {
        if("ENCRYPT/DECRYPT".equalsIgnoreCase(primaryKey.getKeyUsage())){
            int index = SwsdsTools.getKeyPairIndex(1);
            keyVersion.setCardIndex(SwsdsTools.changeIndex(index,1));
            SwsdsTools.generateKeyPair(index,1);
        }else  if("SIGN/VERIFY".equalsIgnoreCase(primaryKey.getKeyUsage())){
            int index = SwsdsTools.getKeyPairIndex(2);
            keyVersion.setCardIndex(SwsdsTools.changeIndex(index,2));
            SwsdsTools.generateKeyPair(index,2);
        }
    }
}





