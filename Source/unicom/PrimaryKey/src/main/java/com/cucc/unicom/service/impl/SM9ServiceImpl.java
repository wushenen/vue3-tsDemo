package com.cucc.unicom.service.impl;

import com.cucc.src.*;
import com.cucc.src.field.curve.CurveElement;
import com.cucc.src.util.HexUtils;
import com.cucc.unicom.service.SM9Service;
import com.qtec.src.*;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class SM9ServiceImpl implements SM9Service {

    private static byte[] hid = {0x03};
    private static byte[] signHid = {0x01};
    private static int k1 = 128, k2 = 256;     // K1_len：0x80  K2_len：0x0100
    private static int type = 1;   // type为0，表示使用基于密钥派生函数的序列密码算法；为1，表示使用结合密钥派生函数的分组密码算法
    @Override
    public SM9KeyGenerationCenter getKGC() {
        SM9KeyGenerationCenter sm9KeyGenerationCenter = new SM9KeyGenerationCenter();
        return sm9KeyGenerationCenter;
    }

    @Override
    public byte[] generateEncryptPrivateKey(String priEncHex,String IDB) throws Exception {
        SM9KeyGenerationCenter kgc = new SM9KeyGenerationCenter(priEncHex,1);
        SM9EncryptPrivateKey priEnc = kgc.generateEncrypyPrivateKey(IDB,hid);
        return priEnc.getDe().toBytes();
    }

    @Override
    public byte[] generateSignPrivateKey(String priSignHex,String IDB) throws Exception {
        SM9KeyGenerationCenter kgc = new SM9KeyGenerationCenter(priSignHex,2);
        SM9SignPrivateKey priSign = kgc.generateSignPrivatekey(IDB,signHid);
        return priSign.getDs().toBytes();
    }

    @Override
    public byte[] getEncPublickey(String priEncHex) throws Exception {
        SM9KeyGenerationCenter kgc = new SM9KeyGenerationCenter(priEncHex,1);
        byte[] pubEnc = kgc.getPpube().toBytes();
        return pubEnc;
    }

    @Override
    public byte[] getSignPublickey(String priSignHex) throws Exception {
        SM9KeyGenerationCenter kgc = new SM9KeyGenerationCenter(priSignHex,2);
        byte[] pubSign = kgc.getPpubs().toBytes();
        return pubSign;
    }

    @Override
    public byte[] sm9Encypt(String IDB, byte[] source, String pubKey) throws Exception {
        SM9KeyGenerationCenter kgc = new SM9KeyGenerationCenter(pubKey,null,0);
        SM9Engine sm9Engine = new SM9Engine(kgc, hid);
        // 加密过程，消息M 的长度mlen = 0xA0为转为二进制时的长度
        sm9Engine.initEncrypt(IDB, k1, k2, source.length, type);
        byte[] ciphertext = sm9Engine.processEncrypt(source);
        return ciphertext;
    }

    @Override
    public byte[] sm9Decrypt(String IDB, byte[] mi, String priKey) throws Exception {
        SM9KeyGenerationCenter kgc = new SM9KeyGenerationCenter(1);
        SM9Engine sm9Engine = new SM9Engine(kgc, hid);
        CurveElement de = Sm9Utils.convertToPriEnc(priKey);
        sm9Engine.initDecrypt(IDB, new SM9EncryptPrivateKey(de), k1, k2, type);
//        sm9Engine.initDecrypt(IDB, kgc.generateEncrypyPrivateKey(IDB,hid), k1, k2, type);

        byte[] mw = sm9Engine.processDecrypt(mi);
        return mw;
    }

    @Override
    public String sm9Sign(String IDB, byte[] source, String pubKey,String priKey) throws Exception {
        SM9KeyGenerationCenter kgc = new SM9KeyGenerationCenter(null,pubKey,1);
        CurveElement de = Sm9Utils.convertToPriSign(priKey);
        SM9SignPrivateKey privateKey = new SM9SignPrivateKey(de);
        SM9Signer signer = new SM9Signer(kgc, signHid);
        // 签名
        signer.initSign(privateKey);
        SM9Signer.Signature signature = signer.generateSignature(source);
        String sign = signature.getH().toString(16)+ HexUtils.bytesToHexString(signature.getS().toBytes());
        return sign;
    }

    @Override
    public boolean sm9VeritySign(String IDB, byte[] source, String sign, String pubKey) throws Exception {
        String H = sign.substring(0,64);
        String S = sign.substring(64);
        SM9KeyGenerationCenter kgc = new SM9KeyGenerationCenter(null,pubKey,1);
        SM9Signer signer = new SM9Signer(kgc, signHid);
        CurveElement s = signer.getSByByte(HexUtils.hexStringToBytes(S));
        signer.initVerify(IDB);
        SM9Signer.Signature signature = new SM9Signer.Signature(new BigInteger(H,16),s);
        boolean ff = signer.verifySignature(source, signature);
        return ff;
    }
}




