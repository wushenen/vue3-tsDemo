package com.cucc.unicom.service.impl;

import com.cucc.unicom.mapper.EncryptionContextMapper;
import com.cucc.unicom.pojo.DataKey;
import com.cucc.unicom.pojo.EncryptionContext;
import com.cucc.unicom.service.AsymmetryEncryptService;
import com.cucc.unicom.service.EncryptService;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.component.util.HexUtils;
import com.cucc.unicom.component.Exception.PwspException;
import com.cucc.unicom.component.annotation.OperateLogAnno;
import com.cucc.unicom.component.util.SM3Util;
import com.cucc.unicom.component.util.SwsdsTools;
import com.cucc.unicom.component.util.UtilService;
import com.cucc.unicom.mapper.KeyVersionMapper;
import com.cucc.unicom.pojo.KeyVersion;
import com.cucc.unicom.pojo.PrimaryKey;
import com.cucc.unicom.service.PrimaryKeyService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class EncryptServiceImpl implements EncryptService {

    private final String OPERATE_MODEL = "对称加解密模块";

    @Autowired
    private PrimaryKeyService primaryKeyService;
    @Autowired
    private KeyVersionMapper keyVersionMapper;
    @Autowired
    private EncryptionContextMapper encryptionContextMapper;
    @Autowired
    private UtilService utilService;
    @Autowired
    private AsymmetryEncryptService asymmetryEncryptService;
    /**
     * 对称密钥 主密钥加密
     * 1.判断密钥类型 QTEC_SM4
     * 2.判断密钥级别 HSM or SOFTWARE
     * 3.判断密钥状态 Enabled
     * 密文步骤：
     * 1.密码卡加密得到byte[] 转16进制ciphertextHex
     * 2.ciphertextHex + EncryptionContext 做sm3 得到 contextHash
     * 3.keyId + keyVersionId  做sm3 得到 keyHash
     * 4.数据库存keyId，keyVersionId，keyHash
     * 5.返回的密文ciphertextBlob是 ciphertext + contextHash + keyHash
     * @param encrypt
     * @return
     * @throws Exception
     */
    @OperateLogAnno(operateDesc = "主密钥加密", operateModel = OPERATE_MODEL)
    @Override
    public EncryptionContext encrypt(EncryptionContext encrypt) throws Exception {
        if(encrypt.getEncryptionContext() == null){
            encrypt.setEncryptionContext("");
        }
        PrimaryKey primaryKey = primaryKeyService.getPrimaryKeybyKeyIdORalias(encrypt.getKeyId());
        //第一步：主密钥加密
        byte[] ciphertextByte = primaryKeyEncrypt(primaryKey,Base64.getDecoder().decode(encrypt.getPlaintext()));
        String ciphertextHex = HexUtils.bytesToHexString(ciphertextByte);
        //第二步：ciphertextHex + EncryptionContext 做sm3 得到contextHash
        String contextHash = HexUtils.bytesToHexString(
                                        SM3Util.hash(
                                                (ciphertextHex + encrypt.getEncryptionContext()).getBytes()));
        //第三步：keyId + keyVersionId  做sm3 得到 keyHash
        String keyHash = HexUtils.bytesToHexString(
                SM3Util.hash(
                        (primaryKey.getKeyId() + primaryKey.getPrimaryKeyVersion()).getBytes()));
        String ciphertextBlob = Base64.getEncoder().encodeToString(
                                                HexUtils.hexStringToBytes(ciphertextHex + contextHash + keyHash));
        //数据库里是keyId + keyVersionId + encryptionContext
        encrypt.setKeyHash(keyHash);
        encrypt.setCiphertextBlob(ciphertextBlob);
        encrypt.setKeyVersionId(primaryKey.getPrimaryKeyVersion());
        EncryptionContext reEncryptionContext = encryptionContextMapper.getEncryptionContext(keyHash);
        if(reEncryptionContext == null){
            encryptionContextMapper.addEncryptionContext(encrypt);
        }
        return encrypt;
    }
    /**
     * 1.密文base64解码，转16进制
     * 2.截取后64位是keyHash,后128-64,是contextHash，0-后128是ciphertextHex
     * 3.ciphertextHex + EncryptionContext 做sm3 结果与 contextHash对比，不一致返回错误
     * 4.用keyHash查询 keyId和keyVersionId 解密
     * 5.返回解密结果base64
     * @param encrypt
     * @return
     * @throws Exception
     */
    @OperateLogAnno(operateDesc = "主密钥解密", operateModel = OPERATE_MODEL)
    @Override
    public EncryptionContext decrypt(EncryptionContext encrypt) throws Exception {
        if(encrypt.getEncryptionContext() == null){
            encrypt.setEncryptionContext("");
        }
        //第一步
        String ciphertextBlob = encrypt.getCiphertextBlob();
        String ciphertextBlobHex = HexUtils.bytesToHexString(Base64.getDecoder().decode(ciphertextBlob));
        //第二步：截取后64位是keyHash,后128-64,是contextHash，0-后128是ciphertextHex
        String keyHash = ciphertextBlobHex.substring(ciphertextBlobHex.length()-64);
        String contextHash = ciphertextBlobHex.substring(ciphertextBlobHex.length()-128,ciphertextBlobHex.length()-64);
        String ciphertextHex = ciphertextBlobHex.substring(0,ciphertextBlobHex.length()-128);
        //第三步ciphertextHex + EncryptionContext 做sm3 结果与 contextHash对比，不一致返回错误
        String contextHash2 = HexUtils.bytesToHexString(
                SM3Util.hash(
                        (ciphertextHex + encrypt.getEncryptionContext()).getBytes()));
        if(!contextHash.equalsIgnoreCase(contextHash2)){
            throw new PwspException(ResultHelper.genResult(400,"InvalidParameter","指定的参数“<encryptionContext>”无效"));
        }
        EncryptionContext reEncryptionContext = encryptionContextMapper.getEncryptionContext(keyHash);
        if(reEncryptionContext == null){
            throw new PwspException(ResultHelper.genResult(404,"Forbidden.KeyNotFound","找不到密文对应的密钥"));
        }
        PrimaryKey primaryKey = primaryKeyService.getPrimaryKeybyKeyIdORalias(reEncryptionContext.getKeyId());
        primaryKey.setPrimaryKeyVersion(reEncryptionContext.getKeyVersionId());
        //对称密钥解密接口
        byte[] plaintextByte = primaryKeyDecrypt(primaryKey,HexUtils.hexStringToBytes(ciphertextHex));
        encrypt.setKeyId(primaryKey.getKeyId());
        encrypt.setKeyVersionId(reEncryptionContext.getKeyVersionId());
        encrypt.setPlaintext(Base64.getEncoder().encodeToString(plaintextByte));
        return encrypt;
    }

    /**
     * 此API随机生成一个数据密钥，并通过您指定的主密钥（CMK）加密后，返回数据密钥的密文和明文
     * 使用KeySpec或者NumberOfBytes来指定数据密钥长度。如果两者都不指定，KMS生成256比特的数据密钥；如果两者都被指定，KMS会忽略KeySpec参数
     * @param dataKey
     * @return
     * @throws Exception
     */
    @OperateLogAnno(operateDesc = "主密钥加密随机数据密钥", operateModel = OPERATE_MODEL)
    @Override
    public DataKey generateDataKey(DataKey dataKey) throws Exception {
        Integer numberOfBytes = dataKey.getNumberOfBytes();
        if(numberOfBytes<1||numberOfBytes>1024||numberOfBytes == null){
            throw new PwspException(ResultHelper.genResult(400,"参数“<numberOfBytes>”范围1-1024"));
        }
        Integer len;
        switch (dataKey.getKeySpec()==null?"":dataKey.getKeySpec()){
            case "AES_128":
                len = 128/8;
                break;
            default:
                len = 256/8;
        }
        len = numberOfBytes==null?len:numberOfBytes;
        byte[] random = utilService.generateQuantumRandom(len);
        byte[] randomM = HexUtils.bytesToHexString(random).getBytes("UTF-8");
        //主密钥加密
        EncryptionContext encrypt = new EncryptionContext();
        encrypt.setKeyId(dataKey.getKeyId());
        encrypt.setEncryptionContext(dataKey.getEncryptionContext());
        encrypt.setPlaintext(Base64.getEncoder().encodeToString(randomM));
        //调用加密接口
        encrypt(encrypt);
        BeanUtils.copyProperties(dataKey, encrypt);
        return dataKey;
    }

    /**
     * 1.用解密接口 解密CiphertextBlob得到
     * 2.用publicKeyBlob加密
     * @param dataKey
     * @return
     * @throws Exception
     */
    @OperateLogAnno(operateDesc = "导出数据密钥", operateModel = OPERATE_MODEL)
    @Override
    public DataKey exportDataKey(DataKey dataKey) throws Exception {
        EncryptionContext encrypt = new EncryptionContext();
        encrypt.setEncryptionContext(dataKey.getEncryptionContext());
        encrypt.setCiphertextBlob(dataKey.getCiphertextBlob());
        //调用解密接口
        //对称密钥解密接口
        decrypt(encrypt);
        String plaintext = encrypt.getPlaintext();
        byte[] plaintextByte = Base64.getDecoder().decode(plaintext);
        //用自带公钥加密明文
        byte[] ciphertextByte = publicKeyBlobEncrypt(dataKey,plaintextByte);
        dataKey.setExportedDataKey(Base64.getEncoder().encodeToString(ciphertextByte));
        return dataKey;
    }

    /**
     * 用自带公钥加密明文
     * @param dataKey
     * @param plaintextByte
     * @return
     * @throws Exception
     */
    public byte[] publicKeyBlobEncrypt(DataKey dataKey,byte[] plaintextByte) throws Exception {
        String publicKeyBlob = dataKey.getPublicKeyBlob();
        String publicKeyHex = new String(Base64.getDecoder().decode(publicKeyBlob),"UTF-8");
        byte[] ciphertextByte;
        switch (dataKey.getWrappingKeySpec()){
            case "EC_SM2":
                //默认第一组加密密钥对
                ciphertextByte = SwsdsTools.sm2Encrypt1(plaintextByte,SwsdsTools.parseSM2PublicKey(publicKeyHex));
                if(!"SM2PKE".equalsIgnoreCase(dataKey.getWrappingAlgorithm())){
                    throw new PwspException(ResultHelper.genResult(400,"InvalidParameter","指定的参数“<wrappingAlgorithm>”无效"));
                }
                break;
            default:
                throw new PwspException(ResultHelper.genResult(400,"MissingParameter","参数“<wrappingKeySpec>”是必需的，但未提供"));
        }
        return ciphertextByte;
    }
    @OperateLogAnno(operateDesc = "导出随机数据密钥", operateModel = OPERATE_MODEL)
    @Override
    public DataKey generateAndExportDataKey(DataKey dataKey) throws Exception {
        //调用此API随机生成一个数据密钥接口
        generateDataKey(dataKey);
        //用自带公钥加密明文
        String plaintext = dataKey.getPlaintext();
        byte[] plaintextByte = Base64.getDecoder().decode(plaintext);
        byte[] ciphertextByte = publicKeyBlobEncrypt(dataKey,plaintextByte);
        dataKey.setExportedDataKey(Base64.getEncoder().encodeToString(ciphertextByte));
        return dataKey;
    }

    /**
     * 1.用SourceEncryptionAlgorithm判断是否公钥加密
     * 2.公钥密文，SourceKeyId 和 SourceKeyVersionId 必传
     * 3.对称密文，用密文内容里的keyHash
     * 4.再加密需要判断主密钥是对称或非对称
     * @param dataKey
     * @return
     * @throws Exception
     */
    @OperateLogAnno(operateDesc = "重加密", operateModel = OPERATE_MODEL)
    @Override
    public DataKey reEncrypt(DataKey dataKey) throws Exception {
        String ciphertextBlob = dataKey.getCiphertextBlob();
        byte[] ciphertextBlobByte =  Base64.getDecoder().decode(ciphertextBlob);
        byte[] plaintextByte = new byte[0];
        switch (dataKey.getSourceEncryptionAlgorithm()==null?"":dataKey.getSourceEncryptionAlgorithm()){
            case "SM2PKE":
                //非对称解密
                PrimaryKey primaryKey = primaryKeyService.getPrimaryKeybyKeyIdORalias(dataKey.getSourceKeyId());
                plaintextByte = asymmetryEncryptService.primaryKeyDecryptAsymmetry(primaryKey,ciphertextBlobByte);
                break;
            case "":
                //调用对称解密
                EncryptionContext encrypt = new EncryptionContext();
                encrypt.setEncryptionContext(dataKey.getSourceEncryptionContext());
                encrypt.setCiphertextBlob(dataKey.getCiphertextBlob());
                //调用解密接口
                decrypt(encrypt);
                String plaintext = encrypt.getPlaintext();
                plaintextByte = Base64.getDecoder().decode(plaintext);
                break;
                default:
                    throw new PwspException(ResultHelper.genResult(400,"InvalidParameter","指定的参数“<sourceEncryptionAlgorithm>”无效"));
        }
        PrimaryKey primaryKey = primaryKeyService.getPrimaryKeybyKeyIdORalias(dataKey.getDestinationKeyId());
        primaryKeyService.keyStatsEnable(primaryKey,1);
        String ciphertextBlob2;
        if(!primaryKey.getKeySpec().startsWith("QTEC_")){
            //非对称
            byte[]ciphertextByte = asymmetryEncryptService.primaryKeyEncryptAsymmetry(primaryKey,plaintextByte);
            ciphertextBlob2 = Base64.getEncoder().encodeToString(ciphertextByte);
        }else{
            //对称
            //主密钥加密
            EncryptionContext encrypt = new EncryptionContext();
            encrypt.setKeyId(dataKey.getDestinationKeyId());
            encrypt.setEncryptionContext(dataKey.getDestinationEncryptionContext());
            encrypt.setPlaintext(Base64.getEncoder().encodeToString(plaintextByte));
            //调用加密接口
            encrypt(encrypt);
            ciphertextBlob2 = encrypt.getCiphertextBlob();
        }
        dataKey.setKeyId(dataKey.getDestinationKeyId());
        dataKey.setKeyVersionId(primaryKey.getPrimaryKeyVersion());
        dataKey.setCiphertextBlob(ciphertextBlob2);
        return dataKey;
    }

    /**
     * 用对称主密钥加密
     * @param primaryKey
     * @param plaintext
     * @return
     * @throws Exception
     */
    public byte[] primaryKeyEncrypt(PrimaryKey primaryKey,byte[] plaintext) throws Exception {
        primaryKeyService.keyStatsEnable(primaryKey,1);
        if(!primaryKey.getKeySpec().startsWith("QTEC_")){
            throw new PwspException(ResultHelper.genResult(409,"不支持此操作"));
        }
        KeyVersion keyVersion = new KeyVersion();
        keyVersion.setKeyId(primaryKey.getKeyId());
        keyVersion.setKeyVersionId(primaryKey.getPrimaryKeyVersion());
        keyVersion = keyVersionMapper.getKeyVersion(keyVersion);
        byte[] ciphertextByte = new byte[0];
        if("HSM".equalsIgnoreCase(primaryKey.getProtectionLevel())){
            Integer cardIndex = keyVersion.getCardIndex();
            ciphertextByte = SwsdsTools.SDF_Encrypt1(plaintext,cardIndex);
        }else{
            //SOFTWARE
            byte[] key = SwsdsTools.SDF_Decrypt1(keyVersion.getKeyData(),1);
            ciphertextByte = HexUtils.hexStringToBytes(SwsdsTools.sm4Encrypt(new String(plaintext,"UTF-8"),
                    new String(key,"UTF-8"),null,"SM4","SM4/ECB/PKCS5PADDING"));
        }
        System.out.println("加密生成："+HexUtils.bytesToHexString(ciphertextByte));
        return ciphertextByte;
    }
    public byte[] primaryKeyDecrypt(PrimaryKey primaryKey,byte[] ciphertextByte) throws Exception {
        System.out.println("解密生成："+HexUtils.bytesToHexString(ciphertextByte));

        primaryKeyService.keyStatsEnable(primaryKey,1);
        KeyVersion keyVersion = new KeyVersion();
        keyVersion.setKeyId(primaryKey.getKeyId());
        keyVersion.setKeyVersionId(primaryKey.getPrimaryKeyVersion());
        keyVersion = keyVersionMapper.getKeyVersion(keyVersion);
        byte[] plaintextByte;
        if("HSM".equalsIgnoreCase(primaryKey.getProtectionLevel())){
            Integer cardIndex = keyVersion.getCardIndex();
            plaintextByte = SwsdsTools.SDF_Decrypt1(ciphertextByte,cardIndex);
        }else{
            //SOFTWARE
            byte[] key = SwsdsTools.SDF_Decrypt1(keyVersion.getKeyData(),1);
            plaintextByte = SwsdsTools.sm4Decrypt(HexUtils.bytesToHexString(ciphertextByte),
                    new String(key,"UTF-8"),null,"SM4","SM4/ECB/PKCS5PADDING").getBytes("UTF-8");
        }
        return plaintextByte;
    }
}





