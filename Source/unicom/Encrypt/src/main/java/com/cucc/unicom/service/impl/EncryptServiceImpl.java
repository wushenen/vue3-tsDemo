package com.cucc.unicom.service.impl;

import com.cucc.unicom.component.Exception.PwspException;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.component.annotation.OperateLogAnno;
import com.cucc.unicom.component.util.HexUtils;
import com.cucc.unicom.component.util.SM3Util;
import com.cucc.unicom.component.util.SwsdsTools;
import com.cucc.unicom.mapper.EncryptionContextMapper;
import com.cucc.unicom.mapper.KeyVersionMapper;
import com.cucc.unicom.pojo.EncryptionContext;
import com.cucc.unicom.pojo.KeyVersion;
import com.cucc.unicom.pojo.PrimaryKey;
import com.cucc.unicom.service.EncryptService;
import com.cucc.unicom.service.PrimaryKeyService;
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


    @OperateLogAnno(operateDesc = "主密钥加密", operateModel = OPERATE_MODEL)
    @Override
    public EncryptionContext encrypt(EncryptionContext encrypt) throws Exception {
        if(encrypt.getEncryptionContext() == null){
            encrypt.setEncryptionContext("");
        }
        PrimaryKey primaryKey = primaryKeyService.getPrimaryKeybyKeyIdORalias(encrypt.getKeyId());
        byte[] ciphertextByte = primaryKeyEncrypt(primaryKey,Base64.getDecoder().decode(encrypt.getPlaintext()));
        String ciphertextHex = HexUtils.bytesToHexString(ciphertextByte);
        String contextHash = HexUtils.bytesToHexString(
                                        SM3Util.hash(
                                                (ciphertextHex + encrypt.getEncryptionContext()).getBytes()));
        String keyHash = HexUtils.bytesToHexString(
                SM3Util.hash(
                        (primaryKey.getKeyId() + primaryKey.getPrimaryKeyVersion()).getBytes()));
        String ciphertextBlob = Base64.getEncoder().encodeToString(
                                                HexUtils.hexStringToBytes(ciphertextHex + contextHash + keyHash));
        encrypt.setKeyHash(keyHash);
        encrypt.setCiphertextBlob(ciphertextBlob);
        encrypt.setKeyVersionId(primaryKey.getPrimaryKeyVersion());
        EncryptionContext reEncryptionContext = encryptionContextMapper.getEncryptionContext(keyHash);
        if(reEncryptionContext == null){
            encryptionContextMapper.addEncryptionContext(encrypt);
        }
        return encrypt;
    }

    @OperateLogAnno(operateDesc = "主密钥解密", operateModel = OPERATE_MODEL)
    @Override
    public EncryptionContext decrypt(EncryptionContext encrypt) throws Exception {
        if(encrypt.getEncryptionContext() == null){
            encrypt.setEncryptionContext("");
        }
        String ciphertextBlob = encrypt.getCiphertextBlob();
        String ciphertextBlobHex = HexUtils.bytesToHexString(Base64.getDecoder().decode(ciphertextBlob));
        String keyHash = ciphertextBlobHex.substring(ciphertextBlobHex.length()-64);
        String contextHash = ciphertextBlobHex.substring(ciphertextBlobHex.length()-128,ciphertextBlobHex.length()-64);
        String ciphertextHex = ciphertextBlobHex.substring(0,ciphertextBlobHex.length()-128);
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
        byte[] plaintextByte = primaryKeyDecrypt(primaryKey,HexUtils.hexStringToBytes(ciphertextHex));
        encrypt.setKeyId(primaryKey.getKeyId());
        encrypt.setKeyVersionId(reEncryptionContext.getKeyVersionId());
        encrypt.setPlaintext(Base64.getEncoder().encodeToString(plaintextByte));
        return encrypt;
    }

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
            byte[] key = SwsdsTools.SDF_Decrypt1(keyVersion.getKeyData(),1);
            ciphertextByte = HexUtils.hexStringToBytes(SwsdsTools.sm4Encrypt(new String(plaintext,"UTF-8"),
                    new String(key,"UTF-8"),null,"SM4","SM4/ECB/PKCS5PADDING"));
        }
        System.out.println("加密生成："+HexUtils.bytesToHexString(ciphertextByte));
        return ciphertextByte;
    }
    public byte[] primaryKeyDecrypt(PrimaryKey primaryKey,byte[] ciphertextByte) throws Exception {
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
            byte[] key = SwsdsTools.SDF_Decrypt1(keyVersion.getKeyData(),1);
            plaintextByte = SwsdsTools.sm4Decrypt(HexUtils.bytesToHexString(ciphertextByte),
                    new String(key,"UTF-8"),null,"SM4","SM4/ECB/PKCS5PADDING").getBytes("UTF-8");
        }
        return plaintextByte;
    }
}





