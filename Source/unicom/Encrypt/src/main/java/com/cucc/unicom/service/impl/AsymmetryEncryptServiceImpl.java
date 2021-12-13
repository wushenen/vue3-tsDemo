package com.cucc.unicom.service.impl;

import com.cucc.unicom.pojo.AsymmetricData;
import com.cucc.unicom.service.AsymmetryEncryptService;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.component.util.HexUtils;
import com.cucc.unicom.component.Exception.PwspException;
import com.cucc.unicom.component.annotation.OperateLogAnno;
import com.cucc.unicom.component.util.SwsdsTools;
import com.cucc.unicom.mapper.KeyVersionMapper;
import com.cucc.unicom.pojo.KeyVersion;
import com.cucc.unicom.pojo.PrimaryKey;
import com.cucc.unicom.service.PrimaryKeyService;
import com.sansec.device.bean.SM2refPublicKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.Base64;

@Service
public class AsymmetryEncryptServiceImpl implements AsymmetryEncryptService {

    private final String OPERATE_MODEL = "非对称加解密模块";

    @Autowired
    private PrimaryKeyService primaryKeyService;
    @Autowired
    private KeyVersionMapper keyVersionMapper;
    /**
     * 非对称密钥公钥加密
     * @param primaryKey
     * @param plaintext
     * @return
     * @throws Exception
     */
    @Override
    public byte[] primaryKeyEncryptAsymmetry(PrimaryKey primaryKey, byte[] plaintext) throws Exception {
        //公共方法：用非对称主密钥对象查找对应的keyVersion
        KeyVersion keyVersion = getKeyVersionAsymmetry(primaryKey);
        byte[] ciphertextByte = new byte[0];
        if("HSM".equalsIgnoreCase(primaryKey.getProtectionLevel())){
            Integer cardIndex = keyVersion.getCardIndex();
            ciphertextByte = SwsdsTools.sm2Encrypt(cardIndex,plaintext);
        }else{
            //SOFTWARE
            byte[] key = SwsdsTools.SDF_Decrypt1(keyVersion.getPubKeyData(),1);
            ciphertextByte = SwsdsTools.sm2Encrypt(plaintext,new String (key,"UTF-8"));
        }
        return ciphertextByte;
    }

    /**
     * 非对称密钥公钥解密
     * @param primaryKey
     * @param ciphertextBlobByte
     * @return
     * @throws Exception
     */
    @Override
    public byte[] primaryKeyDecryptAsymmetry(PrimaryKey primaryKey, byte[] ciphertextBlobByte) throws Exception {
        //公共方法：用非对称主密钥对象查找对应的keyVersion
        KeyVersion keyVersion = getKeyVersionAsymmetry(primaryKey);
        byte[] plaintextByte = new byte[0];
        if("HSM".equalsIgnoreCase(primaryKey.getProtectionLevel())){
            Integer cardIndex = keyVersion.getCardIndex();
            plaintextByte = SwsdsTools.sm2Decrypt(cardIndex,ciphertextBlobByte);
        }else{
            //SOFTWARE
            byte[] key = SwsdsTools.SDF_Decrypt1(keyVersion.getPriKeyData(),1);
            plaintextByte = SwsdsTools.sm2Decrypt(ciphertextBlobByte,new String (key,"UTF-8"));
        }
        return plaintextByte;
    }
    @OperateLogAnno(operateDesc = "非对称密钥签名", operateModel = OPERATE_MODEL)
    @Override
    public AsymmetricData asymmetricSign(AsymmetricData asymmetricData) throws Exception {
        //获取密钥版本，并判断非对称密钥是否是SIGN/VERIFY
        KeyVersion keyVersion = getKeyVersionIsSign(asymmetricData);
        byte[] signData;
        //主密钥保护等级是HSM
        if(keyVersion.getCardIndex() != null){
            Integer cardIndex = keyVersion.getCardIndex();
            signData = SwsdsTools.sm2Sign(cardIndex, Base64.getDecoder().decode(asymmetricData.getDigest()));
        }else{
            //SOFTWARE
            byte[] priKey = SwsdsTools.SDF_Decrypt1(keyVersion.getPriKeyData(),1);
            byte[] pubKey = SwsdsTools.SDF_Decrypt1(keyVersion.getPubKeyData(),1);
            signData = SwsdsTools.sm2Sign(new String (priKey,"UTF-8"),
                    new String (pubKey,"UTF-8"),
                    Base64.getDecoder().decode(asymmetricData.getDigest()));
        }
        asymmetricData.setValue(Base64.getEncoder().encodeToString(signData));
        return asymmetricData;
    }
    @OperateLogAnno(operateDesc = "非对称密钥验签", operateModel = OPERATE_MODEL)
    @Override
    public AsymmetricData asymmetricVerify(AsymmetricData asymmetricData) throws Exception {
        //获取密钥版本，并判断非对称密钥是否是SIGN/VERIFY
        KeyVersion keyVersion = getKeyVersionIsSign(asymmetricData);
        boolean verifyFlag;
        //主密钥保护等级是HSM
        if(keyVersion.getCardIndex() != null){
            Integer cardIndex = keyVersion.getCardIndex();
            verifyFlag = SwsdsTools.sm2VerifySign(cardIndex,
                    Base64.getDecoder().decode(asymmetricData.getDigest()),Base64.getDecoder().decode(asymmetricData.getValue()));
        }else{
            //SOFTWARE
            byte[] pubKey = SwsdsTools.SDF_Decrypt1(keyVersion.getPubKeyData(),1);
            verifyFlag = SwsdsTools.sm2VerifySign(new String (pubKey,"UTF-8"),
                    Base64.getDecoder().decode(asymmetricData.getDigest()),Base64.getDecoder().decode(asymmetricData.getValue()));
        }
        asymmetricData.setValue(verifyFlag+"");
        return asymmetricData;
    }

    @OperateLogAnno(operateDesc = "非对称密钥公钥加密", operateModel = OPERATE_MODEL)
    @Override
    public AsymmetricData asymmetricEncrypt(AsymmetricData asymmetricData) throws Exception {
        //获取密钥版本，并判断非对称密钥是否是ENCRYPT/DECRYPT
        KeyVersion keyVersion = getKeyVersionIsEnc(asymmetricData);
        byte[] tResult;
        if(keyVersion.getCardIndex() != null){
            Integer cardIndex = keyVersion.getCardIndex();
            tResult = SwsdsTools.sm2Encrypt1(cardIndex,Base64.getDecoder().decode(asymmetricData.getPlaintext()));
        }else{
            //SOFTWARE
            byte[] pubKey = SwsdsTools.SDF_Decrypt1(keyVersion.getPubKeyData(),1);
            tResult = SwsdsTools.sm2Encrypt1( Base64.getDecoder().decode(asymmetricData.getPlaintext()),
                    SwsdsTools.parseSM2PublicKey(new String (pubKey,"UTF-8")));
        }
        asymmetricData.setCiphertextBlob(Base64.getEncoder().encodeToString(tResult));
        return asymmetricData;
    }

    @OperateLogAnno(operateDesc = "非对称密钥公钥解密", operateModel = OPERATE_MODEL)
    @Override
    public AsymmetricData asymmetricDecrypt(AsymmetricData asymmetricData) throws Exception {
        //获取密钥版本，并判断非对称密钥是否是ENCRYPT/DECRYPT
        KeyVersion keyVersion = getKeyVersionIsEnc(asymmetricData);
        byte[] tResult;
        if(keyVersion.getCardIndex() != null){
            Integer cardIndex = keyVersion.getCardIndex();
            tResult = SwsdsTools.sm2Decrypt1(cardIndex,Base64.getDecoder().decode(asymmetricData.getCiphertextBlob()));
        }else{
            //SOFTWARE
            byte[] priKey = SwsdsTools.SDF_Decrypt1(keyVersion.getPriKeyData(),1);
            byte[] pubKey = SwsdsTools.SDF_Decrypt1(keyVersion.getPubKeyData(),1);
            PrivateKey priKey1 = SwsdsTools.parseSM2PrivateKey(new String(priKey,"UTF-8"),new String(pubKey,"UTF-8"));
            System.out.println("priKey"+new String (priKey,"UTF-8"));
            System.out.println("pubKey"+new String (pubKey,"UTF-8"));
//            tResult = SwsdsTools.sm2Decrypt1( Base64.getDecoder().decode(asymmetricData.getCiphertextBlob()),
//                    priKey1);
            tResult = SwsdsTools.sm2Decrypt1( Base64.getDecoder().decode(asymmetricData.getCiphertextBlob()),
                    priKey1);
        }
        asymmetricData.setPlaintext(Base64.getEncoder().encodeToString(tResult));
        return asymmetricData;
    }

    @OperateLogAnno(operateDesc = "获取公钥", operateModel = OPERATE_MODEL)
    @Override
    public AsymmetricData getPublicKey(AsymmetricData asymmetricData) throws Exception {
        PrimaryKey primaryKey = primaryKeyService.getPrimaryKeybyKeyIdORalias(asymmetricData.getKeyId());
        primaryKey.setPrimaryKeyVersion(asymmetricData.getKeyVersionId());
        KeyVersion keyVersion = getKeyVersionAsymmetry(primaryKey);
        byte[] pubKey;
        switch (primaryKey.getProtectionLevel()){
            case "HSM":
                int cardIndex = keyVersion.getCardIndex();
                SM2refPublicKey pubK = SwsdsTools.getSM2PublicKey(cardIndex);
                String pubHex = HexUtils.bytesToHexString(pubK.getX())+HexUtils.bytesToHexString(pubK.getY());
                pubKey = HexUtils.hexStringToBytes(pubHex);
                break;
             default://SOFTWARE
                 pubKey = SwsdsTools.SDF_Decrypt1(keyVersion.getPubKeyData(),1);
        }
        asymmetricData.setPublicKey(Base64.getEncoder().encodeToString(pubKey));
        return asymmetricData;
    }

    /**
     * 提取公共方法：获取密钥版本，并判断非对称密钥是否是SIGN/VERIFY
     * @param asymmetricData
     * @return
     * @throws Exception
     */
    public KeyVersion getKeyVersionIsSign(AsymmetricData asymmetricData) throws Exception {
        PrimaryKey primaryKey = primaryKeyService.getPrimaryKeybyKeyIdORalias(asymmetricData.getKeyId());
        if (! "SIGN/VERIFY".equalsIgnoreCase(primaryKey.getKeyUsage())) {
            throw new PwspException(ResultHelper.genResult(409, "Rejected.UnsupportedOperation", "该密钥不支持签名验签操作"));
        }
        if (! "SM2DSA".equalsIgnoreCase(asymmetricData.getAlgorithm())) {
            throw new PwspException(ResultHelper.genResult(400, "InvalidParameter", "指定的参数“<algorithm>”无效"));
        }
        primaryKey.setPrimaryKeyVersion(asymmetricData.getKeyVersionId());
        KeyVersion keyVersion = getKeyVersionAsymmetry(primaryKey);
        return keyVersion;
    }

    /**
     * 提取公共方法：获取密钥版本，并判断非对称密钥是否是ENCRYPT/DECRYPT
     * @param asymmetricData
     * @return
     * @throws Exception
     */
    public KeyVersion getKeyVersionIsEnc(AsymmetricData asymmetricData) throws Exception {
        PrimaryKey primaryKey = primaryKeyService.getPrimaryKeybyKeyIdORalias(asymmetricData.getKeyId());
        if (! "ENCRYPT/DECRYPT".equalsIgnoreCase(primaryKey.getKeyUsage())) {
            throw new PwspException(ResultHelper.genResult(409, "Rejected.UnsupportedOperation", "该密钥不支持加密解密操作"));
        }
        if (! "SM2PKE".equalsIgnoreCase(asymmetricData.getAlgorithm())) {
            throw new PwspException(ResultHelper.genResult(400, "InvalidParameter", "指定的参数“<algorithm>”无效"));
        }
        primaryKey.setPrimaryKeyVersion(asymmetricData.getKeyVersionId());
        KeyVersion keyVersion = getKeyVersionAsymmetry(primaryKey);
        return keyVersion;
    }
    /**
     * 提取公共方法：用非对称主密钥对象查找对应的keyVersion
     * @param primaryKey
     * @return
     * @throws Exception
     */
    public KeyVersion getKeyVersionAsymmetry(PrimaryKey primaryKey) throws Exception {
        primaryKeyService.keyStatsEnable(primaryKey, 1);
        if (primaryKey.getKeySpec().startsWith("QTEC_")) {
            throw new PwspException(ResultHelper.genResult(409, "Rejected.UnsupportedOperation", "对称密钥不支持此操作"));
        }
        KeyVersion keyVersion = new KeyVersion();
        keyVersion.setKeyId(primaryKey.getKeyId());
        keyVersion.setKeyVersionId(primaryKey.getPrimaryKeyVersion());
        keyVersion = keyVersionMapper.getKeyVersion(keyVersion);
        if(keyVersion == null){
            throw new PwspException(ResultHelper.genResult(404, "Forbidden.KeyVersionNotFound", "找不到指定的密钥版本"));
        }
        return keyVersion;
    }

}
