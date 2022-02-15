package com.unicom.quantum.component.util;

import com.sansec.device.SDSFactory;
import com.sansec.device.crypto.CryptoException;
import com.sansec.device.crypto.ISDSCrypto;
import com.sansec.device.crypto.MgrException;
import com.sansec.device.mgr.CardManager;
import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.component.ResultHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;


public class DataTools {

    public static final String SWXA_JCE = "SwxaJCE";
    private static String transformation;
    private static Key keySM4 = null;
    private static byte[] IV = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static ISDSCrypto iSDSCrypto;
    private static CardManager mgr ;

    private static final Logger logger = LoggerFactory.getLogger(DataTools.class);

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
     */
    private static void sm4CBCBySwsds(int keynum) throws NoSuchProviderException, NoSuchAlgorithmException {

        String alg = "SM4";
        transformation = "SM4/ECB/PKCS5PADDING";
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

    public static byte[] SDF_Encrypt1( byte[] plain,int keynum) throws Exception {
        if(keySM4 == null)sm4CBCBySwsds(keynum) ;
        Cipher cipher = null;
        logger.info("密码卡SM4加密");
        cipher = Cipher.getInstance(transformation, SWXA_JCE);
        cipher.init(1, keySM4);
        byte[] tTemp = cipher.doFinal(plain);
        if (tTemp == null) {
            throw new QuantumException(ResultHelper.genResult(1, transformation+"模式加密错误!"));
        } else {
            return tTemp;
        }
    }

    public static byte[] SDF_Decrypt1(byte[] tTemp,int keynum) throws Exception {
        if(keySM4 == null) sm4CBCBySwsds(keynum) ;
        Cipher cipher = null;
        cipher = Cipher.getInstance(transformation, SWXA_JCE);
        cipher.init(2, keySM4);
        byte[] tResult = cipher.doFinal(tTemp);
        if (tResult == null) {
            throw new QuantumException(ResultHelper.genResult(1, transformation+"模式加密错误!"));
        }
        return tResult;
    }

    public static String encryptMessage(String plain) throws Exception {
        byte[] bytes = DataTools.SDF_Encrypt1(plain.getBytes(), 1);
        return HexUtils.bytesToHexString(bytes);
    }

    public static String decryptMessage(String cipher) throws Exception {
        byte[] bytes = HexUtils.hexStringToBytes(cipher);
        byte[] decrypt = DataTools.SDF_Decrypt1(bytes, 1);
        return new String(decrypt);
    }

    public static byte[] encryptMessage(byte[] plain) throws Exception {
        return DataTools.SDF_Encrypt1(plain, 1);
    }


    public static byte[] decryptMessage(byte[] cipher) throws Exception {
        return DataTools.SDF_Decrypt1(cipher, 1);
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
            if(keyStatus[i] == 0){      //该index无密钥存储
                index = i+1;
                break;
            }
        }
        return index;
    }

}
