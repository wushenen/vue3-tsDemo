package com.qtec.unicom.component.init;


import com.qtec.unicom.component.util.HexUtils;
import com.qtec.unicom.component.util.SwsdsTools;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;

public class SelfExamination {

    public static boolean sm2NoPresetTest()throws Exception{
        //生成SM2密钥对
        KeyPair keyPair = KeyPairGenerator.getInstance("SM2", "SwxaJCE").generateKeyPair();
        //对预置明文签名
        Signature signature = Signature.getInstance("SM3WithSM2", "SwxaJCE");
        signature.initSign(keyPair.getPrivate());
        signature.update(HexUtils.hexStringToBytes(SelfExaminationParam.SM2_NOPRESET_SIGN_SOURCE));
        byte[] out = signature.sign();
        //SM2验签
        signature.initVerify(keyPair.getPublic());
        signature.update(HexUtils.hexStringToBytes(SelfExaminationParam.SM2_NOPRESET_SIGN_SOURCE));
        boolean flag = signature.verify(out);
        //对预置明文加密
        byte[] cipher = SwsdsTools.sm2Encrypt(HexUtils.hexStringToBytes(SelfExaminationParam.SM2_NOPRESET_ENC_SOURCE),keyPair.getPublic());
        //SM2解密
        byte[] encSource = SwsdsTools.sm2Decrypt(cipher,keyPair.getPrivate());
        flag = SelfExaminationParam.SM2_NOPRESET_ENC_SOURCE.equalsIgnoreCase(HexUtils.bytesToHexString(encSource));
        return flag;
    }
    public static boolean sm2Test()throws Exception{
        //先对预置签名验签
        byte[] source = SwsdsTools.sm3WithId(SelfExaminationParam.SM2_SIGN_PUB,HexUtils.hexStringToBytes(SelfExaminationParam.SM2_SIGN_ID),HexUtils.hexStringToBytes(SelfExaminationParam.SM2_SIGN_SOURCE));
        boolean flag = SwsdsTools.sm2_gm_verifySign(SelfExaminationParam.SM2_SIGN_PUB,
                source,HexUtils.hexStringToBytes(SelfExaminationParam.SM2_S));
        if(!flag){return flag;}
        //对预置签名明文进行签名再验签
        byte[] signData = SwsdsTools.sm2_gm_sign(SelfExaminationParam.SM2_SIGN_PRI,
                source);
        flag = SwsdsTools.sm2_gm_verifySign(SelfExaminationParam.SM2_SIGN_PUB,
                source,signData);
        if(!flag){return flag;}
        //预置私钥对预置密文解密，与预置明文比较
        byte[] ming = SwsdsTools.sm2_gm_decrypt(HexUtils.hexStringToBytes(SelfExaminationParam.SM2_M),SelfExaminationParam.SM2_ENC_PRI);
        flag = SelfExaminationParam.SM2_SOURCE.equalsIgnoreCase(HexUtils.bytesToHexString(ming));
        if(!flag){return flag;}
        //预置公钥对预置明文加密再解密，与预置明文比较
        byte[] mi = SwsdsTools.sm2_gm_encrypt(HexUtils.hexStringToBytes(SelfExaminationParam.SM2_SOURCE),SelfExaminationParam.SM2_ENC_PUB);
        ming = SwsdsTools.sm2_gm_decrypt(mi,SelfExaminationParam.SM2_ENC_PRI);
        flag = SelfExaminationParam.SM2_SOURCE.equalsIgnoreCase(HexUtils.bytesToHexString(ming));
        return flag;
    }
    public static boolean sm3Test()throws Exception{
        //载入明文进行sm3 与密文对比
        byte[] hashSM3 = SwsdsTools.SM3(HexUtils.hexStringToBytes(SelfExaminationParam.SM3_SOURCE));
        boolean flag = SelfExaminationParam.SM3_M.equalsIgnoreCase(HexUtils.bytesToHexString(hashSM3));
        return flag;
    }
    public static boolean sm4Test()throws Exception{
        //载入明文与密钥加密，与预置密文比较
        //----------------ECB-----------------
        byte[] cipherText = SwsdsTools.sm4Encrypt1(HexUtils.hexStringToBytes(SelfExaminationParam.SM4_ECB_SOURCE),SelfExaminationParam.SM4_ECB_KEY ,"","SM4","SM4/ECB/NOPADDING");
        System.out.println("ECB密文" + cipherText);
        boolean flag = SelfExaminationParam.SM4_ECB_M.equalsIgnoreCase(HexUtils.bytesToHexString(cipherText));
        if(!flag){return flag;}
        byte[] decryptedData = SwsdsTools.sm4Decrypt1(HexUtils.hexStringToBytes(SelfExaminationParam.SM4_ECB_M),SelfExaminationParam.SM4_ECB_KEY ,"","SM4","SM4/ECB/NOPADDING");
        flag = SelfExaminationParam.SM4_ECB_SOURCE.equalsIgnoreCase(HexUtils.bytesToHexString(decryptedData));
        if(!flag){return flag;}
        //---------------CBC-----------------
        byte[] cipherText2 = SwsdsTools.sm4Encrypt1(HexUtils.hexStringToBytes(SelfExaminationParam.SM4_CBC_SOURCE),SelfExaminationParam.SM4_CBC_KEY ,SelfExaminationParam.SM4_IV,"SM4","SM4/CBC/NOPADDING");
        flag = SelfExaminationParam.SM4_CBC_M.equalsIgnoreCase(HexUtils.bytesToHexString(cipherText2));
        System.out.println("CBC密文: " + cipherText2);
        if(!flag){return flag;}
        byte[] plainText3 = SwsdsTools.sm4Decrypt1(HexUtils.hexStringToBytes(SelfExaminationParam.SM4_CBC_M),SelfExaminationParam.SM4_CBC_KEY ,SelfExaminationParam.SM4_IV,"SM4","SM4/CBC/NOPADDING");
        flag = SelfExaminationParam.SM4_CBC_SOURCE.equalsIgnoreCase(HexUtils.bytesToHexString(plainText3));
        return flag;
    }
}
