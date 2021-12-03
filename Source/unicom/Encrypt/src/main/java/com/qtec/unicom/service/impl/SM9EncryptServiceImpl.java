package com.qtec.unicom.service.impl;

import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.component.util.HexUtils;
import com.qtec.unicom.component.Exception.PwspException;
import com.qtec.unicom.component.annotation.OperateLogAnno;
import com.qtec.unicom.component.util.SwsdsTools;
import com.qtec.unicom.pojo.KeyVersion;
import com.qtec.unicom.pojo.PrimaryKey;
import com.qtec.unicom.pojo.SM9Encrypt;
import com.qtec.unicom.pojo.dto.SM9ParamsDTO;
import com.qtec.unicom.service.AsymmetryEncryptService;
import com.qtec.unicom.service.PrimaryKeyService;
import com.qtec.unicom.service.SM9EncryptService;
import com.qtec.unicom.service.SM9Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qtec.src.util.io.Base64;

@Service
public class SM9EncryptServiceImpl implements SM9EncryptService {

    private final String OPERATE_MODEL = "非对称加解密模块";

    @Autowired
    private PrimaryKeyService primaryKeyService;
    @Autowired
    private AsymmetryEncryptService asymmetryEncryptService;
    @Autowired
    private SM9Service sm9Service;

    @OperateLogAnno(operateDesc = "获取SM9公开参数及用户私钥", operateModel = OPERATE_MODEL)
    @Override
    public SM9ParamsDTO sm9params(String IDB, SM9Encrypt sm9Encrypt) throws Exception {
        PrimaryKey key = primaryKeyService.getPrimaryKeybyKeyIdORalias(sm9Encrypt.getKeyId());
        KeyVersion keyVersion = asymmetryEncryptService.getKeyVersionAsymmetry(key);
        String sm9PubKey = new String(SwsdsTools.SDF_Decrypt1(keyVersion.getPubKeyData(),1));
        String sm9PriKey = new String(SwsdsTools.SDF_Decrypt1(keyVersion.getPriKeyData(),1));
        byte[] userPri;
        SM9ParamsDTO dto = new SM9ParamsDTO();
        if ("ENCRYPT/DECRYPT".equalsIgnoreCase(key.getKeyUsage())) {
            userPri = sm9Service.generateEncryptPrivateKey(sm9PriKey,IDB);
            dto.setPubEnc(sm9PubKey);
            dto.setPriEnc(HexUtils.bytesToHexString(userPri));
        }else if ("SIGN/VERIFY".equalsIgnoreCase(key.getKeyUsage())) {//SIGN/VERIFY
            userPri = sm9Service.generateSignPrivateKey(sm9PriKey,IDB);
            dto.setPubSign(sm9PubKey);
            dto.setPriSign(HexUtils.bytesToHexString(userPri));
        }else{
            throw new PwspException(ResultHelper.genResult(400,"<密钥用途>无效"));
        }
        return dto;
    }

    @Override
    public SM9Encrypt sm9Encrypt(String idb, SM9Encrypt sm9Encrypt) throws Exception {
        byte[] plaintext = Base64.decode(sm9Encrypt.getPlaintext());
        PrimaryKey key = primaryKeyService.getPrimaryKeybyKeyIdORalias(sm9Encrypt.getKeyId());
        if (! "ENCRYPT/DECRYPT".equalsIgnoreCase(key.getKeyUsage())) {
            throw new PwspException(ResultHelper.genResult(409, "该密钥不支持加密解密操作"));
        }
        KeyVersion keyVersion = asymmetryEncryptService.getKeyVersionAsymmetry(key);
        String sm9PubKey = new String(SwsdsTools.SDF_Decrypt1(keyVersion.getPubKeyData(),1));
        byte[] ciphertext = sm9Service.sm9Encypt(idb,plaintext,sm9PubKey);
        String ciphertextBlob = Base64.encodeBytes(ciphertext);
        sm9Encrypt.setCiphertextBlob(ciphertextBlob);
        return sm9Encrypt;
    }

    @Override
    public SM9Encrypt sm9Decrypt(String idb, SM9Encrypt sm9Encrypt) throws Exception {
        byte[] ciphertext = Base64.decode(sm9Encrypt.getCiphertextBlob());
        PrimaryKey key = primaryKeyService.getPrimaryKeybyKeyIdORalias(sm9Encrypt.getKeyId());
        if (! "ENCRYPT/DECRYPT".equalsIgnoreCase(key.getKeyUsage())) {
            throw new PwspException(ResultHelper.genResult(409,"该密钥不支持加密解密操作"));
        }
        KeyVersion keyVersion = asymmetryEncryptService.getKeyVersionAsymmetry(key);
        String sm9PriKey = new String(SwsdsTools.SDF_Decrypt1(keyVersion.getPriKeyData(),1));
        System.out.println("sm9PriKey:"+sm9PriKey);
        String userPri = HexUtils.bytesToHexString(sm9Service.generateEncryptPrivateKey(sm9PriKey,idb));
        System.out.println("userPri:"+userPri);
        System.out.println("idb:"+idb);
        byte[] plaintextByte =sm9Service.sm9Decrypt(idb,ciphertext,userPri);
        sm9Encrypt.setPlaintext(Base64.encodeBytes(plaintextByte));
        return sm9Encrypt;
    }

    @Override
    public SM9Encrypt sm9Sign(String idb, SM9Encrypt sm9Encrypt) throws Exception {
        byte[] digest = Base64.decode(sm9Encrypt.getDigest());
        PrimaryKey key = primaryKeyService.getPrimaryKeybyKeyIdORalias(sm9Encrypt.getKeyId());
        if (! "SIGN/VERIFY".equalsIgnoreCase(key.getKeyUsage())) {
            throw new PwspException(ResultHelper.genResult(409,"该密钥不支持签名验签操作"));
        }
        KeyVersion keyVersion = asymmetryEncryptService.getKeyVersionAsymmetry(key);
        String sm9PubKey = new String(SwsdsTools.SDF_Decrypt1(keyVersion.getPubKeyData(),1));
        String sm9PriKey = new String(SwsdsTools.SDF_Decrypt1(keyVersion.getPriKeyData(),1));
        String userPri = HexUtils.bytesToHexString(sm9Service.generateSignPrivateKey(sm9PriKey,idb));
        String sign = sm9Service.sm9Sign(idb,digest,sm9PubKey,userPri);
        String signBase64 = Base64.encodeBytes(HexUtils.hexStringToBytes(sign));
        sm9Encrypt.setSign(signBase64);
        return sm9Encrypt;
    }

    @Override
    public SM9Encrypt sm9VeritySign(String idb, SM9Encrypt sm9Encrypt) throws Exception {
        byte[] digest = Base64.decode(sm9Encrypt.getDigest());
        byte[] signByte = Base64.decode(sm9Encrypt.getSign());

        String sign = HexUtils.bytesToHexString(signByte);
        PrimaryKey key = primaryKeyService.getPrimaryKeybyKeyIdORalias(sm9Encrypt.getKeyId());
        if (! "SIGN/VERIFY".equalsIgnoreCase(key.getKeyUsage())) {
            throw new PwspException(ResultHelper.genResult(409,"该密钥不支持签名验签操作"));
        }
        KeyVersion keyVersion = asymmetryEncryptService.getKeyVersionAsymmetry(key);
        String sm9PubKey = new String(SwsdsTools.SDF_Decrypt1(keyVersion.getPubKeyData(),1));
        boolean value = sm9Service.sm9VeritySign(idb,digest,sign,sm9PubKey);
        sm9Encrypt.setValue(value+"");
        return  sm9Encrypt;
    }
}





