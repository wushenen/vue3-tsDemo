package com.unicom.quantum.component.util;

import com.alibaba.druid.util.StringUtils;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.component.Exception.QuantumException;
import com.sansec.asn1.ASN1InputStream;
import com.sansec.asn1.ASN1Sequence;
import com.sansec.asn1.DERBitString;
import com.sansec.asn1.DERObject;
import com.sansec.asn1.pkcs.*;
import com.sansec.asn1.x509.AlgorithmIdentifier;
import com.sansec.asn1.x509.SubjectPublicKeyInfo;
import com.sansec.device.MGRFactory;
import com.sansec.device.SDSFactory;
import com.sansec.device.bean.*;
import com.sansec.device.crypto.CryptoException;
import com.sansec.device.crypto.IMGRDevice;
import com.sansec.device.crypto.ISDSCrypto;
import com.sansec.device.crypto.MgrException;
import com.sansec.device.mgr.CardManager;
import com.sansec.jce.provider.JCESM2PrivateKey;
import com.sansec.jce.provider.SwxaProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECPoint;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;

/**
 * Created by duhc on 2018/6/13.
 */
public class SwsdsTools {
    private static final Logger logger = LoggerFactory.getLogger(SwsdsTools.class);
    private static final String ALGORITHM_SHA1PRNG = "SHA1PRNG";
    public static final String SWXA_JCE = "SwxaJCE";
    private static String transformation;
    private static Key keySM4 = null;
    private static byte[] IV = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static ISDSCrypto iSDSCrypto;
    private static CardManager mgr ;

    static {
        try {
            mgr = new CardManager();
            iSDSCrypto = SDSFactory.getInstance();
        } catch (CryptoException e) {
            e.printStackTrace();
        }catch (MgrException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成密码卡对象
     * SM4 ECB NOPADDING 密码卡内部密钥
     * @param keynum
     * @return
     */
	private static void sm4CBCBySwsds(int keynum) throws NoSuchProviderException, NoSuchAlgorithmException {

        String alg = "SM4";
        String mode = "ECB";
        String padding = "PKCS5PADDING";//NOPADDING
        transformation = String.valueOf(alg) + "/" + mode + "/" + padding;
        Key key = null;
            KeyGenerator kg = KeyGenerator.getInstance(alg, SWXA_JCE);
            kg.init(keynum << 16);
            key = kg.generateKey();
            if (key == null) {
                logger.error("fail");
            } else {
                logger.debug("ok");
            }
        keySM4 = key;
    }
    /**
     * 密码卡SM4加密CBC/PKCS5PADDING
     * @param keynum
     * @param plain
     */
    public static byte[] SDF_Encrypt1( byte[] plain,int keynum) throws NoSuchProviderException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        if(keySM4 == null)sm4CBCBySwsds(keynum) ;
        Cipher cipher = null;
        logger.debug("密码卡SM4加密");
            cipher = Cipher.getInstance(transformation, SWXA_JCE);
            cipher.init(1, keySM4);
            byte[] tTemp = cipher.doFinal(plain);
            if (tTemp == null) {
                logger.error(String.valueOf(transformation) + " 模式加密错误!");
            } else {
                return tTemp;
            }
        return null;
    }

    /**
     * 密码卡SM4解密CBC/PKCS5PADDING
     * @param tTemp
     * @param keynum
     * @param
     */
    public static byte[] SDF_Decrypt1(byte[] tTemp,int keynum) throws NoSuchProviderException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        if(keySM4 == null)sm4CBCBySwsds(keynum) ;
        Cipher cipher = null;
                cipher = Cipher.getInstance(transformation, SWXA_JCE);

                cipher.init(2, keySM4);

                byte[] tResult = cipher.doFinal(tTemp);
                if (tResult == null) {
                    System.err.println(String.valueOf(transformation) + " 模式解密错误!");
                }
        logger.debug("密码卡SM4解密");
                return tResult;
    }

    /**
     * 使用 密码卡 生成随机数
     *
     * @return 返回 32位 字节数组
     */
    public static byte[] generateRandom(int length) throws NoSuchProviderException, NoSuchAlgorithmException {
        SecureRandom secureRandom;
        byte[] random = new byte[length];
        try {
            secureRandom = SecureRandom.getInstance("RND", SWXA_JCE);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return random;
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            return random;
        }
        try {
            byte[] seed = secureRandom.generateSeed(length);
            secureRandom.setSeed(seed);
        } catch (Exception e) {
            e.printStackTrace();
            return random;
        }
        secureRandom.nextBytes(random);
        return random;
    }

    /**
     * SM2 公钥加密
     * @param plain
     * @param pubKeyStr
     * @return
     * @throws Exception
     */
    public static byte[] sm2Encrypt(byte[] plain,String pubKeyStr) throws Exception{
        PublicKey publicKey = parseSM2PublicKey(pubKeyStr);
        Cipher cipher = Cipher.getInstance("SM2", SWXA_JCE);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] tTemp = cipher.doFinal(plain);
        return tTemp;
    }
    public static byte[] sm2Encrypt(byte[] plain,PublicKey publicKey) throws Exception{
        Cipher cipher = Cipher.getInstance("SM2", SWXA_JCE);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] tTemp = cipher.doFinal(plain);
        return tTemp;
    }

    /**
     * 国密标准SM2加密
     * @param plain
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static byte[] sm2_gm_encrypt(byte[] plain,String publicKey) throws Exception{
        //对预置明文加密
        byte[] cipher = SwsdsTools.sm2Encrypt(plain,publicKey);
        System.out.println("cipher 是否裸数据:"+HexUtils.bytesToHexString(cipher));
        String xymc = SwsdsTools.makexymc(cipher);
        return HexUtils.hexStringToBytes(xymc);
    }
    /**
     * 国密标准SM2解密
     * @param tTemp
     * @param priKeyStr
     * @return
     * @throws Exception
     */
    public static byte[] sm2_gm_decrypt(byte[] tTemp,String priKeyStr) throws Exception{
        byte[] data = transSM2CipherGM2DER(tTemp);
        PrivateKey privateKey = parseSM2PrivateKey(priKeyStr);
        Cipher cipher = Cipher.getInstance("SM2", SWXA_JCE);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] tResult = cipher.doFinal(data);
        return tResult;
    }
    public static byte[] sm2_gm_sign(String priKeyHex,byte[] dataInput) throws Exception {
        SM2refPrivateKey priD = new SM2refPrivateKey(HexUtils.hexStringToBytes(priKeyHex));
        SM2refSignature signa = iSDSCrypto.sm2Sign(priD,dataInput);
        return signa.encode();
    }
    public static boolean sm2_gm_verifySign(String publicKey, byte[] dataInput,byte[] out) throws CryptoException {
        byte[]pkByte = HexUtils.hexStringToBytes(publicKey);
        byte[] r = new byte[32];
        byte[] s = new byte[32];
        byte[] x = new byte[32];
        byte[] y = new byte[32];
        System.arraycopy(out,0,r,0,32);
        System.arraycopy(out,32,s,0,32);
        System.arraycopy(pkByte,0,x,0,32);
        System.arraycopy(pkByte,32,y,0,32);

        SM2refSignature signature1 = new SM2refSignature(r, s);
        SM2refPublicKey pk = new SM2refPublicKey(x, y);
        boolean deviceF = false;
        try {
            deviceF = iSDSCrypto.sm2Verify(pk, dataInput, signature1);
        } catch (CryptoException e) {
            e.printStackTrace();
        }
        return deviceF;
    }
    public static byte[] sm2Encrypt1(byte[] plain,PublicKey publicKey) throws Exception{
        int len = plain.length;
        int yu = len%128;
        int duan = len/128;
        byte[] pTemp;
        String tTemp="";
        for (int i=0;i<duan;i++){
            pTemp = new byte[128];
            System.arraycopy(plain,i*128,pTemp,0,128);
            tTemp += HexUtils.bytesToHexString(sm2Encrypt(pTemp,publicKey)) + "&&";
        }
        if(yu == 0){
            tTemp = tTemp.substring(0,tTemp.lastIndexOf("&&"));
        }else{
            pTemp = new byte[yu];
            System.arraycopy(plain,duan*128,pTemp,0,yu);
            tTemp += HexUtils.bytesToHexString(sm2Encrypt(pTemp,publicKey));
        }
        return tTemp.getBytes();
    }
    /**
     * SM2 私钥解密
     * @param tTemp
     * @param priKeyStr
     * @return
     * @throws Exception
     */
    public static byte[] sm2Decrypt(byte[] tTemp,String priKeyStr) throws Exception{
        PrivateKey privateKey = parseSM2PrivateKey(priKeyStr);//parseSM2PrivateKey(priKeyStr);
        Cipher cipher = Cipher.getInstance("SM2", SWXA_JCE);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] tResult = cipher.doFinal(tTemp);
        return tResult;
    }
    public static byte[] sm2Decrypt(byte[] tTemp,PrivateKey privateKey) throws Exception{
        Cipher cipher = Cipher.getInstance("SM2", SWXA_JCE);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] tResult = cipher.doFinal(tTemp);
        return tResult;
    }
    public static byte[] sm2Decrypt1(byte[] tTemp,PrivateKey privateKey) throws Exception{
        String tTempS = new String(tTemp);
        String[] tTempArr = tTempS.split("&&");
        String tResultS="";
        for (String tTempA: tTempArr
             ) {
            byte[]tTempB = HexUtils.hexStringToBytes(tTempA);
            String ming = HexUtils.bytesToHexString(sm2Decrypt(tTempB,privateKey));
            tResultS += ming;
        }
        byte[]tResult = HexUtils.hexStringToBytes(tResultS);
        return tResult;
    }
    /**
     * sm2签名
     * @param priKey
     * @param dataInput
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static byte[] sign(PrivateKey priKey ,byte[] dataInput) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SM3WithSM2", SWXA_JCE);
        signature.initSign(priKey);
        signature.update(dataInput);
        byte[] out = signature.sign();
        return out;
    }
    public static byte[] sm2Sign(String priKeyHex ,String pubKeyHex,byte[] dataInput) throws Exception {
        PrivateKey priKey = parseSM2PrivateKey(priKeyHex,pubKeyHex);
        Signature signature = Signature.getInstance("SM3WithSM2", SWXA_JCE);
        signature.initSign(priKey);
        signature.update(dataInput);
        byte[] out = signature.sign();
        return out;
    }
    /**
     * SM2公钥验签
     * @param publicKey
     * @param dataInput 原数据
     * @param out 签名数据
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static boolean verifySign(PublicKey publicKey, byte[] dataInput,byte[] out) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
            Signature signature = Signature.getInstance("SM3WithSM2", SWXA_JCE);
            signature.initVerify(publicKey); // 公钥
            signature.update(dataInput); // clientProof' sm3
            boolean flag = signature.verify(out); // 结果
        logger.debug("验证结果: " + flag);
            return flag;
    }
    public static boolean verifySign(PublicKey publicKey, byte[] dataInput,byte[] out,String algo) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(algo, SWXA_JCE);
        signature.initVerify(publicKey); // 公钥
        signature.update(dataInput); // clientProof' sm3
        boolean flag = signature.verify(out); // 结果
        logger.debug("验证结果: " + flag);
        return flag;
    }
    public static boolean sm2VerifySign(String pubKeyHex, byte[] dataInput,byte[] out) throws Exception {
        PublicKey publicKey = parseSM2PublicKey(pubKeyHex);
        Signature signature = Signature.getInstance("SM3WithSM2", SWXA_JCE);
        signature.initVerify(publicKey); // 公钥
        signature.update(dataInput); // clientProof' sm3
        boolean flag = signature.verify(out); // 结果
        logger.debug("验证结果: " + flag);
        return flag;
    }
    /**
     * 密码卡内部sm2签名
     * @param keyNum
     * @param dataInput
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static byte[] sm2Sign(int keyNum ,byte[] dataInput) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("SM2", SWXA_JCE);
        kpg.initialize(keyNum << 16);
        KeyPair kp = kpg.genKeyPair();
        Signature signature = Signature.getInstance("SM3WithSM2", SWXA_JCE);
        signature.initSign(kp.getPrivate());
        signature.update(dataInput);
        byte[] out = signature.sign();
        return out;
    }

    /**
     * 密码卡内部sm2验签
     * @param keyNum
     * @param dataInput
     * @param signData
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static boolean sm2VerifySign(int keyNum ,byte[] dataInput,byte[] signData) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("SM2", SWXA_JCE);
        kpg.initialize(keyNum << 16);
        KeyPair kp = kpg.genKeyPair();
        Signature signature = Signature.getInstance("SM3WithSM2", SWXA_JCE);
        signature.initVerify(kp.getPublic());
        signature.update(dataInput);
        boolean verify = signature.verify(signData);
        return verify;
    }
    /**
     * 密码卡SM3
     * @param src
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     */
    public static byte[] SM3(byte[] src) throws NoSuchProviderException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SM3", SWXA_JCE);
        messageDigest.update(src);
        byte[] output = messageDigest.digest();
        return output;
    }

    /**
     * 三未信安SM4加密
     * @param key 16进制密钥，256位
     * @param source 16进制需要加密的内容
     * @param iv 16进制向量
     * @param algo 加密算法
     * @param mode 加密算法/模式/填充
     * @return
     * @throws Exception
     */
    public static String sm4Encrypt(String source, String key, String iv, String algo, String mode) throws Exception {
        Security.addProvider(new SwxaProvider());
        SecretKey secretKey = new SecretKeySpec(HexUtils.hexStringToBytes(key), algo);
        Cipher cipher = Cipher.getInstance(mode, SWXA_JCE);
        if (StringUtils.isEmpty(iv)) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(IV));
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(ByteUtils.fromHexString(iv)));
        }
        byte[] resource = cipher.doFinal(source.getBytes("UTF-8"));
        return HexUtils.bytesToHexString(resource);
    }
    public static byte[] sm4Encrypt1(byte[] source, String key, String iv, String algo, String mode) throws Exception {
        Security.addProvider(new SwxaProvider());
        SecretKey secretKey = new SecretKeySpec(HexUtils.hexStringToBytes(key), algo);
        Cipher cipher = Cipher.getInstance(mode, SWXA_JCE);
        if (StringUtils.isEmpty(iv)) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(IV));
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(ByteUtils.fromHexString(iv)));
        }
        byte[] resource = cipher.doFinal(source);
        return resource;
    }
    /**
     * 三未信安SM4解密
     * @param key 16进制密钥，256位
     * @param source 16进制需要的内容
     * @param iv 16进制向量
     * @param algo 加密算法
     * @param mode 加密算法/模式/填充
     * @return
     * @throws Exception
     */
    public static String sm4Decrypt(String source, String key, String iv, String algo, String mode) throws Exception {
        Security.addProvider(new SwxaProvider());
        SecretKey secretKey = new SecretKeySpec(HexUtils.hexStringToBytes(key), algo);
        Cipher cipher = Cipher.getInstance(mode, SWXA_JCE);
        if (StringUtils.isEmpty(iv)) {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(IV));
        } else {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(ByteUtils.fromHexString(iv)));
        }
        byte[] resource = cipher.doFinal(HexUtils.hexStringToBytes(source));
        return new String(resource,"UTF-8");
    }
    public static byte[] sm4Decrypt1(byte[] source, String key, String iv, String algo, String mode) throws Exception {
        Security.addProvider(new SwxaProvider());
        SecretKey secretKey = new SecretKeySpec(HexUtils.hexStringToBytes(key), algo);
        Cipher cipher = Cipher.getInstance(mode, SWXA_JCE);
        if (StringUtils.isEmpty(iv)) {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(IV));
        } else {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(ByteUtils.fromHexString(iv)));
        }
        byte[] resource = cipher.doFinal(source);
        return resource;
    }
    /**
     * SM2私钥字节转私钥对象
     * @param priKeyStr
     * @return
     * @throws Exception
     */
    public static PrivateKey parseSM2PrivateKey(String priKeyStr) throws Exception {
        SM2PrivateKeyStructure sm2PriStr = new SM2PrivateKeyStructure(new BigInteger(priKeyStr,16));
        JCESM2PrivateKey sm2Pri = new JCESM2PrivateKey(sm2PriStr);
        return sm2Pri;
    }
    /**
     * 密码卡 SM2私钥字节转私钥对象
     * @param priKeyStr
     * @return
     * @throws Exception
     */
    public static PrivateKey parseSM2PrivateKey(String priKeyStr,String pubKeyStr) throws Exception {
        DERBitString pubkey = null;
        if (pubKeyStr != null) {
            BigInteger xHex = new BigInteger(pubKeyStr.substring(0,64),16);
            BigInteger yHex = new BigInteger(pubKeyStr.substring(64),16);
            pubkey = new DERBitString((new SM2PublicKeyStructure(new ECPoint(xHex, yHex))).getPublicKey());
        }
        SM2PrivateKeyStructure sm2Structure = new SM2PrivateKeyStructure(
                new BigInteger(priKeyStr,16), pubkey, GBObjectIdentifiers.sm2);
        JCESM2PrivateKey privateKey1 = new JCESM2PrivateKey(sm2Structure);
        return privateKey1;
    }
    /**
     * SM2公钥字节转私钥对象
     * @param pubKey_x
     * @param pubKey_y
     * @return
     * @throws Exception
     */
    public static PublicKey parseSM2PublicKey(byte[] pubKey_x, byte[] pubKey_y) throws Exception {
        BigInteger x = new BigInteger(1, pubKey_x);
        BigInteger y = new BigInteger(1, pubKey_y);
        ECPoint w = new ECPoint(x, y);
//		SubjectPublicKeyInfo
        SubjectPublicKeyInfo    info = new SubjectPublicKeyInfo(new AlgorithmIdentifier(GBObjectIdentifiers.sm2), new SM2PublicKeyStructure(w).getPublicKey());
        byte[] key =  info.getDEREncoded();
        X509EncodedKeySpec spec = new X509EncodedKeySpec(key);
        KeyFactory factory = KeyFactory.getInstance("SM2", SWXA_JCE);
        return factory.generatePublic(spec);
    }
    public static PublicKey parseSM2PublicKey(String pubKeyStr) throws Exception {
        byte[] publickeyx = ByteUtils.fromHexString(pubKeyStr.substring(0,64));
        byte[] publickeyy = ByteUtils.fromHexString(pubKeyStr.substring(64));
        BigInteger x = new BigInteger(1, publickeyx);
        BigInteger y = new BigInteger(1, publickeyy);
        ECPoint w = new ECPoint(x, y);
        SubjectPublicKeyInfo info = new SubjectPublicKeyInfo(new AlgorithmIdentifier(GBObjectIdentifiers.sm2), new SM2PublicKeyStructure(w).getPublicKey());
        byte[] key =  info.getDEREncoded();
        X509EncodedKeySpec spec = new X509EncodedKeySpec(key);
        KeyFactory factory = KeyFactory.getInstance("SM2", SWXA_JCE);
        return factory.generatePublic(spec);
    }

    /**
     * 获取密码卡中对称密钥空的index存储位置
     * keyType：1-对称密钥；3-ECC非对称密钥；4-RSA非对称密钥
     * @return
     */
    public static int getKeyIndex() throws CryptoException {
        int index = 0;
        int [] keyStatus = iSDSCrypto.getKeyStatus(1);
        for (int i=0;i<keyStatus.length;i++){
            if(keyStatus[i] == 0){//该index无密钥存储
                index = i+1;
                break;
            }
        }
        return index;
    }

    /**
     * 获取密码卡中非对称密钥空的index存储位置
     *   keyType：1-加密；2-签名
     * @param keyType
     * @return index为一组一组的
     * @throws CryptoException
     */
    public static int getKeyPairIndex(int keyType) throws CryptoException, QuantumException {
        int index = 0;
        int [] keyStatus = iSDSCrypto.getKeyStatus(3);
        for (int i=0;i<keyStatus.length;i++){
            if(i%2 == 0 && keyStatus[i] == 0 && keyStatus[i+1] == 0){//该index无密钥存储
                index = (i+2)/2;
                break;
            }
        }
        /*switch (keyType){
            case 1:
                for (int i=0;i<keyStatus.length;i++){
                    if(i%2 == 1 && keyStatus[i] == 0){//该index无密钥存储
                        index = (i+1)/2;
                        break;
                    }
                }
                break;
            case 2:
                for (int i=0;i<keyStatus.length;i++){
                    if(i%2 == 0 && keyStatus[i] == 0){//该index无密钥存储
                        index = (i+2)/2;
                        break;
                    }
                }
                break;
        }*/
        if(0 == index){
            throw new QuantumException(ResultHelper.genResult(400,"Rejected.LimitExceeded","请求被拒绝，密码卡可存储非对称加密密钥对资源不足"));
        }
        return index;
    }
    /**
     * 生成密码卡index存储位置
     * kpType 1-加密；2-签名
     * @return
     */
    public static void generateKeyPair(int keyIndex, int kpType) throws MgrException, CryptoException {
        IMGRDevice mgr = MGRFactory.getInstance();
        mgr.generateSm2KeyPair(keyIndex, kpType);
    }

    /**
     * 密码卡全量备份
     *
     * @param password
     * @return
     * @throws CryptoException
     * @throws MgrException
     */
    public static byte[] backup(String password) throws CryptoException, MgrException {
        IMGRDevice mgr = MGRFactory.getInstance();
        byte[] b = mgr.backup(password);
        return b;
    }
    public static void restore(byte[] bakData, String password) throws MgrException, CryptoException {
         mgr.restore(bakData,password);
    }

    /**
     * 导入SM2密钥对
     * keyType：1-加密；2-签名
     * @param index
     * @param keyType
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     */
    public static SM2refKeyPair sm2ImportCard(int index,int keyType) throws CryptoException, MgrException {
        IMGRDevice mgr = MGRFactory.getInstance();
        SM2refKeyPair sm2RefKeyPair = iSDSCrypto.generateSM2KeyPair(256);
        SM2refPublicKey sm2RefPublicKey = sm2RefKeyPair.getPublicKey();
        SM2refPrivateKey sm2RefPrivateKey = sm2RefKeyPair.getPrivateKey();
        mgr.sm2Import(index, keyType, sm2RefPublicKey, sm2RefPrivateKey);
        return sm2RefKeyPair;
    }
    /**
     * 获取非对称密钥公钥
     * keyType：1-加密；2-签名
     * @param index
     * @param keyType
     * @return
     * @throws CryptoException
     */
    public static SM2refPublicKey getSM2PublicKey(int index,int keyType) throws CryptoException {
        SM2refPublicKey pubKey = iSDSCrypto.getSM2PublicKey(index,keyType);
        return pubKey;
    }
    public static SM2refPublicKey getSM2PublicKey(int index) throws CryptoException {
        int keyType = index%2==0?1:2;
        SM2refPublicKey pubKey = iSDSCrypto.getSM2PublicKey((index + 1)/2,keyType);
        return pubKey;
    }

    /**
     * 入参index为组，返回结果是下标
     * 将SM2的 index组 转为纯粹的index始于1，不区分签名加密
     * 签名：1,3,5,7,9
     * 加密：2,4,6,8,10
     * keyType：1-加密；2-签名
     * @param index
     * @param keyType
     * @return
     */
    public static int changeIndex(int index, int keyType){
        int num = 0;
        switch (keyType){
            case 1:
                num = 2 * index;
                break;
            case 2:
                num = 2 * index - 1;
                break;
        }
        return num;
    }
    /**
     * 内部密钥 SM2加密
     * @param index 纯粹的index始于1，不区分签名加密
     * @param refCipher
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] sm2Encrypt(int index,byte[]refCipher) throws NoSuchProviderException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("SM2", SWXA_JCE);
        kpg.initialize(index << 16);
        KeyPair kp = kpg.genKeyPair();
        Cipher cipher = Cipher.getInstance("SM2", SWXA_JCE);
        cipher.init(Cipher.ENCRYPT_MODE, kp.getPublic());
        byte[] tResult = cipher.doFinal(refCipher);
        return tResult;
    }
    public static byte[] sm2Encrypt1(int index,byte[] plain) throws Exception{
        int len = plain.length;
        int yu = len%128;
        int duan = len/128;
        byte[] pTemp;
        String tTemp="";
        for (int i=0;i<duan;i++){
            pTemp = new byte[128];
            System.arraycopy(plain,i*128,pTemp,0,128);
            tTemp += HexUtils.bytesToHexString(sm2Encrypt(index,pTemp)) + "&&";
        }
        if(yu == 0){
            tTemp = tTemp.substring(0,tTemp.lastIndexOf("&&"));
        }else{
            pTemp = new byte[yu];
            System.arraycopy(plain,duan*128,pTemp,0,yu);
            tTemp += HexUtils.bytesToHexString(sm2Encrypt(index,pTemp));
        }
        return tTemp.getBytes();
    }
    /**
     * 内部密钥 SM2解密
     * @param index 纯粹的index始于1，不区分签名加密
     * @param refCipher
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] sm2Decrypt(int index,byte[]refCipher) throws NoSuchProviderException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("SM2", SWXA_JCE);
        kpg.initialize(index << 16);
        KeyPair kp = kpg.genKeyPair();
        Cipher cipher = Cipher.getInstance("SM2", SWXA_JCE);
        cipher.init(Cipher.DECRYPT_MODE, kp.getPrivate());
        System.out.println("0000000000000000000000");
        byte[] tResult = cipher.doFinal(refCipher);
        return tResult;
    }
    public static byte[] sm2Decrypt1(int index,byte[] tTemp) throws Exception{
        String tTempS = new String(tTemp);
        String[] tTempArr = tTempS.split("&&");
        String tResultS="";
        for (String tTempA: tTempArr
        ) {
            byte[]tTempB = HexUtils.hexStringToBytes(tTempA);
            String ming = HexUtils.bytesToHexString(sm2Decrypt(index,tTempB));
            tResultS += ming;
        }
        byte[]tResult = HexUtils.hexStringToBytes(tResultS);
        return tResult;
    }
    /**
     * 密码卡生成SM2非对称密钥
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair generateKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("SM2","SwxaJCE");
        keyPairGenerator.initialize(256);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }
    /**
     * 密码卡生成RSA非对称密钥2048
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair generateRSAKeyPair(int keyLen) throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA","SwxaJCE");
        keyPairGenerator.initialize(keyLen);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    /**
     * 三未信安：64字节公钥转公钥对象
     * @param tmpKey
     * @return
     * @throws Exception
     */
    public static PublicKey transSM2PubKeyGM2DER(byte[]tmpKey)throws Exception{
        System.out.println("tmpKey len:"+tmpKey.length);
        byte[]x1 = new byte[32];
        byte[]y1 = new byte[32];

        int pos = 0;
        System.arraycopy(tmpKey, pos, x1, 0, 32);
        pos = pos + x1.length;
        System.arraycopy(tmpKey, pos, y1, 0, 32);
        pos += y1.length;
        BigInteger x = new BigInteger(1, x1);
        BigInteger y = new BigInteger(1, y1);

        SM2PublicKeyStructure structure = new SM2PublicKeyStructure(new ECPoint(x, y));
        SubjectPublicKeyInfo info = new SubjectPublicKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.ecPublicKey, GBObjectIdentifiers.sm2), structure.getPublicKey());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(info.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("SM2", SWXA_JCE);
        PublicKey key = keyFactory.generatePublic(keySpec);
        return key;
    }

    /**
     * 三未信安：32字节私钥转私钥对象
     * @param tmppriKey
     * @param tmpPubKey
     * @return
     * @throws Exception
     */
    public static PrivateKey transSM2PriKeyGM2DER(byte[]tmppriKey,byte[]tmpPubKey)throws Exception{

        SM2refPrivateKey privateKey = new SM2refPrivateKey();
        try{
            privateKey.decode(tmppriKey);
        }catch(Exception e){
            throw new Exception("不是有效的国密格式SM2私钥，请重新选择");
        }
        BigInteger s = new BigInteger(1, privateKey.getD());
        SM2refPublicKey publicKey = new SM2refPublicKey();
        try{
            publicKey.decode(tmpPubKey);
        }catch(Exception e){
            throw new Exception("不是有效的国密格式SM2公钥，请重新选择");
        }
        BigInteger x = new BigInteger(1, publicKey.getX());
        BigInteger y = new BigInteger(1, publicKey.getY());

        DERBitString pubkey = new DERBitString(new SM2PublicKeyStructure(new ECPoint(x, y)).getPublicKey());

        SM2PrivateKeyStructure structure = new SM2PrivateKeyStructure(s, pubkey, null);
        PrivateKeyInfo info = new PrivateKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.ecPublicKey, GBObjectIdentifiers.sm2), structure.getDERObject());

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(info.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("SM2", SWXA_JCE);
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        return key;
    }

    /**
     *返回C1C3C2 国密标准格式 的 sm2加密结果
     * @param in
     * @return
     */
    public static String makexymc(byte[] in){
        boolean ConvertOK = true;
        ASN1InputStream aIn = new ASN1InputStream(in);
        ASN1Sequence seq = null;

        try {
            DERObject derObj = aIn.readObject();
            seq = (ASN1Sequence)derObj;
        } catch (Exception var22) {
            ConvertOK = false;
            GlobalData.logger.log(Level.WARNING, "以sequence结构方式，解析SM2加密值错误。 ");
        }
        SM2refCipher cipher = null;
        String xycmHex = "";
        if (ConvertOK) {
            SM2CipherStructure cipherSt = new SM2CipherStructure(seq);
            cipher = SM2StructureUtil.convert(cipherSt);
            int cLength = cipher.getCLength();
            byte[]c = new byte[cLength];
            System.arraycopy(cipher.getC(),0,c,0,cLength);
            xycmHex = HexUtils.bytesToHexString(cipher.getX())+HexUtils.bytesToHexString(cipher.getY())
                    +HexUtils.bytesToHexString(cipher.getM())+HexUtils.bytesToHexString(c);
        }
        return  xycmHex;
    }
    /**
     * sm2签名预处理
     * @param pubHex
     * @param id
     * @param source
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     */
    public static byte[] sm3WithId(String pubHex,byte[]id,byte[]source) throws NoSuchProviderException, NoSuchAlgorithmException {
        byte[]pub = HexUtils.hexStringToBytes(pubHex);

        byte[] a =HexUtils.hexStringToBytes(
                "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC");
        byte[] b = HexUtils.hexStringToBytes(
                "28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93");
        byte[] xg = HexUtils.hexStringToBytes(
                "32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7");
        byte[] yg = HexUtils.hexStringToBytes(
                "BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0");
        byte[] za = new byte[id.length+194];
        byte[] idL = HexUtils.short2byte((short)(id.length*8));
        System.arraycopy(idL,0,za,0,2);
        int index = 2;
        System.arraycopy(id,0,za,2,id.length);
        index +=id.length;
        System.arraycopy(a,0,za,index,32);
        index +=32;
        System.arraycopy(b,0,za,index,32);
        index +=32;
        System.arraycopy(xg,0,za,index,32);
        index +=32;
        System.arraycopy(yg,0,za,index,32);
        index +=32;
        System.arraycopy(pub,0,za,index,64);
        index +=64;
        byte[]predata = SM3(za);
        System.out.println("za:"+HexUtils.bytesToHexString(predata));
        byte[] sm3 = new byte[predata.length+source.length];
        System.arraycopy(predata,0,sm3,0,predata.length);
        System.arraycopy(source,0,sm3,predata.length,source.length);
        byte[]pbHashData = SM3(sm3);
        System.out.println("pbHashData:"+HexUtils.bytesToHexString(pbHashData));

        return pbHashData;
    }


    /**
     * 将国密xymc转为加密对象
     * @param in
     * @return
     * @throws Exception
     */
    public static byte[] transSM2CipherGM2DER(byte[]in)throws Exception{
        byte[] x = new byte[32];
        byte[] y = new byte[32];
        byte[] M = new byte[32];
        byte[] C = new byte[in.length-96];
        System.arraycopy(in, 0, x, 0, 32);
        System.arraycopy(in, 32, y, 0, 32);
        System.arraycopy(in, 64, M, 0, 32);
        System.arraycopy(in, 96, C, 0, C.length);
        SM2CipherStructure structure = new SM2CipherStructure(new BigInteger(x), new BigInteger(y), C,M);
        return structure.getDEREncoded();
    }
    public static SM2refSignature makeS(byte[] sigBytes){
        ASN1InputStream aIn = new ASN1InputStream(sigBytes);
        ASN1Sequence seq = null;
        boolean ConvertOK = true;

        try {
            DERObject derObj = aIn.readObject();
            seq = (ASN1Sequence)derObj;
        } catch (Exception var24) {
            ConvertOK = false;
            GlobalData.logger.log(Level.WARNING, "以sequence结构方式，解析SM2签名错误。");
        }

        if (ConvertOK) {
            SM2SignStructure signatureSt = new SM2SignStructure(seq);
            SM2refSignature signature = SM2StructureUtil.convert(signatureSt);
            return signature;
        }
        return null;
    }
    public static byte[] transSM2SignatureGM2DER(byte[]data)throws Exception{
        SM2refSignature sign = new SM2refSignature();
        try{
            sign.decode(data);
        }catch(Exception e){
            throw new Exception("不是有效的国密格式SM2签名，请重新选择");
        }
        BigInteger r = new BigInteger(1, sign.getR());
        BigInteger s = new BigInteger(1, sign.getS());

        SM2SignStructure structure = new SM2SignStructure(r,s);
        return structure.getDEREncoded();
    }
}




