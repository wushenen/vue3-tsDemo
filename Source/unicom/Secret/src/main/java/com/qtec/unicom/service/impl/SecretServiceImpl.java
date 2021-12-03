package com.qtec.unicom.service.impl;

import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.component.util.HexUtils;
import com.qtec.unicom.component.Exception.PwspException;
import com.qtec.unicom.component.util.SwsdsTools;
import com.qtec.unicom.mapper.KeyMapper;
import com.qtec.unicom.mapper.SecretMapper;
import com.qtec.unicom.pojo.Key;
import com.qtec.unicom.pojo.PrimaryKey;
import com.qtec.unicom.pojo.Secret;
import com.qtec.unicom.service.PrimaryKeyService;
import com.qtec.unicom.service.SecretService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SecretServiceImpl implements SecretService {

    @Autowired
    private KeyMapper keyMapper;

    @Autowired
    private SecretMapper secretMapper;

    @Autowired
    private PrimaryKeyService primaryKeyService;

    private static final Logger logger = LoggerFactory.getLogger(SecretServiceImpl.class);

    @Override
    @Transactional
    public Secret createSecret(Secret secret) throws Exception {
        String keyId = secret.getEncryptionKeyId();

        //查询数据库中是否有该条数据
        //通过用户输入的secretName和versionID来获取数据库中的数据
        Secret getSecret =  secretMapper.getSecretByVersionId(secret);
        if(getSecret==null){
            //获取凭据值的明文
            String trueSecretData = secret.getSecretData();
            byte[] secretDataArr = trueSecretData.getBytes();


            if(keyId == null || keyId == "") {
                secret.setEncryptionKeyId(null);
                byte[] secretArr1 = SwsdsTools.SDF_Encrypt1(secretDataArr,1);
                byte[] secretArr2 = SwsdsTools.SM3(secretArr1);
                String secretData1 = HexUtils.bytesToHexString(secretArr1);
                String secretData2 = HexUtils.bytesToHexString(secretArr2);
                String secretData = secretData1+secretData2;
                secret.setSecretData(secretData);
                secretMapper.createSecret(secret);
                return secret;

            }else{
                //通过keyId或者别名查询出主密钥相关信息
                PrimaryKey primaryKey = primaryKeyService.getPrimaryKeybyKeyIdORalias(secret.getEncryptionKeyId());
                //根据查询出来的主密钥的相关信息获得keyId 并进一步获得cardIndex
                Key key = keyMapper.getKey(primaryKey.getKeyId());
                String keySpec = primaryKey.getKeySpec();
                Integer cardIndex = key.getCardIndex();
                if (cardIndex == null) {
                    cardIndex = 1;
                }
                String arn = primaryKey.getArn();
                secret.setArn(arn);
                //判断是否为SM4算法 如果是则直接调用加密算法  如果不是 直接抛出异常
                if (keySpec.equals("QTEC_SM4")) {
                    byte[] secretArr1 = SwsdsTools.SDF_Encrypt1(secretDataArr,cardIndex);
                    byte[] secretArr2 = SwsdsTools.SM3(secretArr1);
                    String secretData1 = HexUtils.bytesToHexString(secretArr1);
                    String secretData2 = HexUtils.bytesToHexString(secretArr2);
                    String secretData = secretData1+secretData2;
                    secret.setSecretData(secretData);
                    secretMapper.createSecret(secret);
                    return secret;
                } else {
                    throw new PwspException(ResultHelper.genResult(400, "用户指定的主密钥为非对称密钥，无法对凭据值进行加密"));
                }
            }
        }else{
//            //若数据库中存在该数据 则对输入的数据与数据库中的数据进行对比
//            //将数据库中的数据解密
//            Secret getSecretData = getSecretValue(getSecret);
//            if(getSecretData.getVersionId().equals(secret.getVersionId()) && getSecretData.getSecretData().equals(secret.getSecretData())){
//                return secret;
//            }else{
//                throw new PwspException(JSONResult.genFailResult(400,"InvalidParameter","指定版本号凭据值已经存在"));
//            }
            throw new PwspException(ResultHelper.genResult(400,"指定凭据名已经存在"));
        }
    }

    @Override
    @Transactional
    public Secret deleteSecret(Secret secret) throws Exception {
        boolean forceDeleteWithoutRecovery = secret.getForceDeleteWithoutRecovery();
        Integer recoveryWindowInDays = secret.getRecoveryWindowInDays();
        Date date = new Date();
        secret.setUpdateTime(date);
        //当用户设置为不可恢复的删除时直接删除凭据对象
        if(forceDeleteWithoutRecovery){
            secret.setPlannedDeleteTime(null);
            if(secretMapper.deleteSecretsTrue(secret)>0){
                return secret;
            }else{
                throw new PwspException(ResultHelper.genResult(400,"InvalidParameter","指定的参数<凭据名>无效"));
            }
        }else{
            long time = recoveryWindowInDays *24*60*60*1000L;
            Date plannedDeletedTime = new Date(System.currentTimeMillis() + time);
            secret.setPlannedDeleteTime(plannedDeletedTime);
            if(secretMapper.deleteSecrets(secret)>0){
                return secret;
            }else{
                throw new PwspException(ResultHelper.genResult(400,"InvalidParameter","指定的参数<凭据名>无效"));
            }
        }
    }

    @Override
    public Secret describeSecret(Secret secret) throws Exception {
        secret = secretMapper.describeSecret(secret);
        if(secret==null){
            throw new PwspException(ResultHelper.genResult(400,"InvalidParameter","指定的参数<凭据名>无效"));
        }else{
            String keyId = secret.getEncryptionKeyId();
            if(keyId==null){
                logger.info("createTime:"+secret.getCreateTime());
                return secret;
            }else {
                //通过keyId或者别名查询出主密钥相关信息
                PrimaryKey primaryKey = primaryKeyService.getPrimaryKeybyKeyIdORalias(secret.getEncryptionKeyId());
                //根据查询出来的主密钥的相关信息获得keyId
                Key key = keyMapper.getKey(primaryKey.getKeyId());
                String arn = key.getArn();
                secret.setArn(arn);
                return secret;
            }
        }
    }

    @Override
    public Secret getSecretValue(Secret secret) throws Exception {
        secret = secretMapper.getSecretValue(secret);
        if(secret==null){
            throw new PwspException(ResultHelper.genResult(400,"指定的参数<凭据名>无效"));
        }else{
            //获取凭据值的密文
            String trueSecretData = secret.getSecretData();
            //截取数组中SM4加密的部分
            String secretDataSM4 = trueSecretData.substring(0, trueSecretData.length() - 64);
            byte[] secretData4 = HexUtils.hexStringToBytes(secretDataSM4);
            //对SM4加密的部分再使用SM3加密 确保凭据值没有被纂改
            String secretDataSM3Again = HexUtils.bytesToHexString(SwsdsTools.SM3(secretData4));
            logger.info("secretDataSM3Again:"+secretDataSM3Again);

            //截取数组中SM3加密的部分
            String secretDataSM3 = trueSecretData.substring(trueSecretData.length() - 64, trueSecretData.length());
            logger.info("secretDataSM3:"+secretDataSM3);

            if (secretDataSM3Again.equals(secretDataSM3)) {
                String keyId = secret.getEncryptionKeyId();
                if (keyId == null || keyId == "") {
                    //使用SM4对前半部分解密
                    byte[] secretDataArrSM4 = SwsdsTools.SDF_Decrypt1(secretData4, 1);
                    secret.setSecretData(new String(secretDataArrSM4));
                    return secret;
                } else {
                    //通过keyId或者别名查询出主密钥相关信息
                    PrimaryKey primaryKey = primaryKeyService.getPrimaryKeybyKeyIdORalias(secret.getEncryptionKeyId());
                    //根据查询出来的主密钥的相关信息获得keyId 并进一步获得cardIndex
                    Key key = keyMapper.getKey(primaryKey.getKeyId());
                    Integer cardIndex = key.getCardIndex();
                    if (cardIndex == null) {
                        cardIndex = 1;
                    }
                    //使用SM4对前半部分解密
                    byte[] secretDataArrSM4 = SwsdsTools.SDF_Decrypt1(secretData4, cardIndex);
                    secret.setSecretData(new String(secretDataArrSM4));
                    return secret;
                }
            } else {
                throw new PwspException(ResultHelper.genResult(400, "InvalidParameter", "凭据值完整性被破坏"));
            }
        }
    }

    @Override
    @Transactional
    public Secret updateSecret(Secret secret) throws Exception {
        secret.setUpdateTime(new Date());
        if(secretMapper.updateSecret(secret)>0){
            return secret;
        }else{
            throw new PwspException(ResultHelper.genResult(400,"InvalidParameter","指定的参数<凭据名>无效"));
        }
    }

    @Override
    @Transactional
    public Secret restoreSecret(Secret secret) throws Exception {
        boolean forceDeleteWithoutRecovery = secret.getForceDeleteWithoutRecovery();
        if(forceDeleteWithoutRecovery){
            throw new PwspException(ResultHelper.genResult(404,"InvalidParameter","指定的对象无法恢复删除"));
        }else{
            Date date = new Date();
            secret.setUpdateTime(date);
            if(secretMapper.restoreSecret(secret)>0){
                return secret;
            }else{
                throw new PwspException(ResultHelper.genResult(400,"InvalidParameter","指定的参数<凭据名>无效"));
            }
        }
    }

    @Override
    public List<Secret> listSecretVersionIds(Secret secret) throws Exception {
        List<Secret> versionIds = secretMapper.listSecretVersionIds(secret);
        return versionIds;
    }

    @Override
    public List<Secret> listSecrets(String creator) throws Exception{
        List<Secret> secrets = secretMapper.listSecrets(creator);
        return secrets;
    }

    @Override
    @Transactional
    public Secret putSecretValue(Secret secret) throws Exception {
        //通过用户输入的secretName和versionID来获取数据库中的数据
        Secret getSecret =  secretMapper.getSecretByVersionId(secret);
        if(getSecret == null){
            //将数据使用默认的主密钥进行加密并存储
            //获取传入的secretData
            String trueSecretData = secret.getSecretData();
            byte[] secretDataArr = trueSecretData.getBytes();
            byte[] secretArr1 = SwsdsTools.SDF_Encrypt1(secretDataArr,1);
            byte[] secretArr2 = SwsdsTools.SM3(secretArr1);
            String secretData1 = HexUtils.bytesToHexString(secretArr1);
            String secretData2 = HexUtils.bytesToHexString(secretArr2);
            String secretData = secretData1+secretData2;
            secret.setSecretData(secretData);
            secretMapper.putSecretValue(secret);
            return secret;
        }else{
            //将数据库中的数据解密
            Secret getSecretData = getSecretValue(getSecret);
            logger.info("secret_data:"+getSecretData.getSecretData());
            logger.info("data_secret:"+secret.getSecretData());
            if(getSecretData.getVersionId().equals(secret.getVersionId()) && getSecretData.getSecretData().equals(secret.getSecretData())){
                return secret;
            }else{
                throw new PwspException(ResultHelper.genResult(400,"InvalidParameter","指定版本号凭据值已经存在"));
            }
        }
    }

    //自动删除过期的凭据对象
    @Override
    public void autoDeleteSecret() throws Exception{
        secretMapper.autoDeleteSecret(new Date());
    }

}
