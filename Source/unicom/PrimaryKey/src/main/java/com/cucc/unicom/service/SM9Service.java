package com.cucc.unicom.service;


import com.cucc.src.SM9KeyGenerationCenter;

public interface SM9Service {
    //获取SM9 kgc对象（随机主私钥和主公钥）
    SM9KeyGenerationCenter getKGC();
    //获取SM9用户加密私钥 参数是用户标识
    byte[] generateEncryptPrivateKey(String priEncHex, String IDB) throws Exception;
    //获取SM9用户签名私钥 参数是用户标识
    byte[] generateSignPrivateKey(String priSignHex, String IDB) throws Exception;
    //获取SM9加密主公钥
    byte[] getEncPublickey(String priEncHex) throws Exception;
    //获取SM9签名主公钥
    byte[] getSignPublickey(String priSignHex) throws Exception;
    //SM9加密
    byte[] sm9Encypt(String IDB, byte[] source, String pubKey)throws Exception;
    //SM9解密
    byte[] sm9Decrypt(String IDB, byte[] source, String priKey)throws Exception;
    //SM9签名
    String sm9Sign(String IDB, byte[] source, String pubKey, String priKey)throws Exception;
    //SM9验签
    boolean sm9VeritySign(String IDB, byte[] source, String sign, String pubKey)throws Exception;
}
