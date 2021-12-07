package com.qtec.unicom.component.util;

import com.alibaba.fastjson.JSONObject;
import com.qtec.jni.KMSJNI;
import com.qtec.jni.QR902JNI;
import com.qtec.unicom.component.Exception.PwspException;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.component.init.Init;
import com.qtec.unicom.component.jni.Qr901JNI;
import com.qtec.unicom.mapper.KeySourceConfigMapper;
import com.qtec.unicom.pojo.KeySourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class UtilService {
    private static final Logger logger = LoggerFactory.getLogger(UtilService.class);

    @Value("${rngSource}")
    public int rngSource;
    @Value("${server901IP}")
    private String server901IP;
    @Value("${server902IP}")
    private String server902IP;
    @Autowired
    private KeySourceConfigMapper keySourceConfigMapper;

    private final String SM4IV = "00000000000000000000000000000000";
    public static final String SM4KEY = "304C1673505BF98B894DC2C496D24B33";


    /**
     * 读取配置文件随机数来源，来获取随机数
     * @param length
     * @return
     */
    public byte[] generateRandom(int length) throws Exception {
        logger.info("随机数获取源：{}",rngSource);
        byte[] random = new byte[length];
        switch (rngSource){
            case 1://qrng
                random = QrngUtil.genrateRandom(length);
                break;
            case 2://密码卡
                random = SwsdsTools.generateRandom(length);
                break;
            case 3://901
                try {
                    Qr901JNI t = new Qr901JNI();
                    String ip = server901IP;
                    String[] iparr = ip.split(".");
                    long server_ip = (long)((Integer.parseInt(iparr[0]) << 24) + (Integer.parseInt(iparr[1]) << 16) + (Integer.parseInt(iparr[2]) << 8) + Integer.parseInt(iparr[3]));
                    byte[] buffer = new byte[length];
                    System.out.println("----------获取901随机数-------------");
                    t.QRNG_getRandom(server_ip, buffer, (long)buffer.length);
                    System.arraycopy(buffer, 0, random, 0, length);
                } catch (Throwable var13) {
                    throw new RuntimeException("Add provider failed:\n" + var13.getMessage());
                }
                break;
            case 4:
                QR902JNI qr902JNI = new QR902JNI();
                byte[] bytes = qr902JNI.QRNG_read_random(server902IP, 8192);
                System.arraycopy(bytes, 0, random, 0, length);
                break;
            default:
                random = randomByte(length);
        }
        return random;
    }


    public byte[] generateQuantumRandom(int length) throws Exception {
        System.out.println("length = " + length);
        List<KeySourceConfig> keySourceConfigs = keySourceConfigMapper.getKeySourceConfigs();
        byte[] random = new byte[length];
        byte[] random2 = new byte[length];
        System.out.println("random.length = " + random.length);
        System.out.println("keySourceConfigs.size() = " + keySourceConfigs.size());
        if (keySourceConfigs.size() != 0) {
            for (int i = 0; i < keySourceConfigs.size(); i++) {
                if(keySourceConfigs.get(i).getPriority() != 5){
                    logger.info("随机数获取源：{}",keySourceConfigs.get(i).getKeySource());
                    switch (keySourceConfigs.get(i).getKeySource()) {
                        case 1://qrng
                            System.out.println("---qrng---");
                            random = QrngUtil.genrateRandom(length);
                            break;
                        case 2://密码卡
                            System.out.println("---密码卡---");
                            random = SwsdsTools.generateRandom(length);
                            break;
                        case 3://902
                            System.out.println("---902---");
                            QR902JNI qr902JNI = new QR902JNI();
                            String sourceIp = keySourceConfigs.get(i).getSourceIp();
                            String sourceIp2 = keySourceConfigs.get(i).getSourceIp2();
                            if (sourceIp == null)
                                break;
                            if (length < 8192) {
                                byte[] bytes = new byte[8192];
                                bytes = qr902JNI.QRNG_read_random(sourceIp, bytes.length);
                                random = Arrays.copyOf(bytes, length);
                                if (Arrays.equals(random2,random)) {
                                    if (sourceIp2 != null){
                                        bytes = qr902JNI.QRNG_read_random(sourceIp2, bytes.length);
//                                        random = Arrays.copyOf(bytes, length);
                                    }
                                }
                                System.arraycopy(bytes, 0, random, 0, length);
                            } else {
                                int num = length/8192 ;
                                if (length%8192 != 0) {
                                    num += 1;
                                }
                                byte[] bytes = qr902JNI.QRNG_read_random(sourceIp, num*8192);
                                random = Arrays.copyOf(bytes, length);
                                if (Arrays.equals(random2,random)) {
                                    if (sourceIp2 != null)
                                        bytes = qr902JNI.QRNG_read_random(sourceIp2, num*8192);
                                }
                                System.arraycopy(bytes, 0, random, 0, length);
                            }
                            break;
                        case 4: //QKD
                            KMSJNI kmsjni = new KMSJNI();
                            String qkdConfig = keySourceConfigs.get(i).getConfigInfo();
                            JSONObject configInfo = JSONObject.parseObject(qkdConfig);
                            JSONObject config = configInfo.getJSONObject("config");
                            JSONObject config2 = configInfo.getJSONObject("config2");
                            String localName = config.getString("localName");
                            String peerName = config.getString("peerName");
                            String devKey = config.getString("devKey");
                            String cryptKey = config.getString("cryptKey");
                            int num = length/32;
                            if (length%32 != 0)
                                num += 1;
                            byte[] bytes = new byte[num*32];
                            byte[] keyId = new byte[num*16];
                            if (localName != null && peerName != null && devKey != null && cryptKey != null){
                                bytes = kmsjni.kms_sdk_output_key_plaintext(num*32, keySourceConfigs.get(i).getSourceIp(),
                                        localName, peerName, HexUtils.hexStringToBytes(devKey),
                                        HexUtils.hexStringToBytes(cryptKey), false, keyId);
                            }
                            random = Arrays.copyOf(bytes, length);
                            if (Arrays.equals(random2,random)) {
                                localName = config2.getString("localName");
                                peerName = config2.getString("peerName");
                                devKey = config2.getString("devKey");
                                cryptKey = config2.getString("cryptKey");
                                if (keySourceConfigs.get(i).getSourceIp2() != null){
                                    bytes = kmsjni.kms_sdk_output_key_plaintext(num*32, keySourceConfigs.get(i).getSourceIp2(),
                                            localName, peerName, HexUtils.hexStringToBytes(devKey),
                                            HexUtils.hexStringToBytes(cryptKey), false, keyId);
                                }
                            }
                            System.arraycopy(bytes, 0, random, 0, length);
                            break;
                        default:
                            random = randomByte(length);
                    }
                    if (!Arrays.equals(random,random2))
                        return random;
                }
            }
        }
        return null;
    }

    /**
     * 根据指定长度生成字母和数字的随机数
     *   0~9的ASCII为48~57
     *   A~Z的ASCII为65~90
     *   a~z的ASCII为97~122
     * @param length
     * @return
     */
    public  String createRandomCharData(int length)
    {
        StringBuilder sb=new StringBuilder();
        Random rand=new Random();//随机用以下三个随机生成器
        Random randdata=new Random();
        int data=0;
        for(int i=0;i<length;i++)
        {
            int index=rand.nextInt(3);
            //目的是随机选择生成数字，大小写字母
            switch(index)
            {
                case 0:
                    data=randdata.nextInt(10);//仅仅会生成0~9
                    sb.append(data);
                    break;
                case 1:
                    data=randdata.nextInt(26)+65;//保证只会产生65~90之间的整数
                    sb.append((char)data);
                    break;
                case 2:
                    data=randdata.nextInt(26)+97;//保证只会产生97~122之间的整数
                    sb.append((char)data);
                    break;
            }
        }
        String result=sb.toString();
        logger.info("生成的随机数兑换码为{}",result);
        return result;
    }

    /**
     * 获取当前用户
     * jwt 或 secretKey
     * @param request
     * @return
     */
    public static String getCurrentUserName(HttpServletRequest request) throws PwspException {
        String token = request.getHeader("Token");//Authorization
        String userName = null;
        if(token != null){
            try {
                userName = JWTUtil.getUsername(token);
                return userName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new PwspException(ResultHelper.genResult(403,"用户ID无效"));
    }

    /**
     * 将普通字符串的base64 转为 16进制字符串的Base64
     * @param base64Str
     * @return
     * @throws PwspException
     * @throws UnsupportedEncodingException
     */
    public static String stringToHexStr(String base64Str) throws PwspException, UnsupportedEncodingException {
        byte[]a = Base64.getDecoder().decode(base64Str);
        String hexStr = new String(a,"UTF-8");
        byte[] hexByte = HexUtils.hexStringToBytes(hexStr);
        return Base64.getEncoder().encodeToString(hexByte);
    }


    /**
     * SM4 加密
     * @param plainTextBytes
     * @param key
     * @return
     */
    public byte[] encryptCBC(byte[] plainTextBytes, String key) {
        try {
            SM4Util sm4Util = new SM4Util();
            return sm4Util.encryptDataCBC(plainTextBytes, key, SM4IV);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String encryptCBC(String plainTextBytes, String key) {
        try {
            SM4Util sm4Util = new SM4Util();
            return HexUtils.bytesToHexString(sm4Util.encryptDataCBC(plainTextBytes.getBytes(), key, SM4IV));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public byte[] encryptCBCWithPadding(byte[] plainTextBytes, String key) {
        try {
            SM4Util sm4Util = new SM4Util();
            return sm4Util.encryptDataCBCWithPadding(plainTextBytes, key, SM4IV);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String encryptCBCWithPadding(String plainTextBytes, String key) {
        try {
            SM4Util sm4Util = new SM4Util();
            return HexUtils.bytesToHexString(sm4Util.encryptDataCBCWithPadding(plainTextBytes.getBytes(), key, SM4IV));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * SM4 解密
     * @param cipherText
     * @param key
     * @return
     */
    public byte[] decryptCBC(byte[] cipherText, String key) {
        try {
            SM4Util sm4Util = new SM4Util();
            return sm4Util.decryptDataCBC(cipherText, key, SM4IV);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decryptCBC(String cipherText, String key) {
        try {
            SM4Util sm4Util = new SM4Util();
            return new String(sm4Util.decryptDataCBC(HexUtils.hexStringToBytes(cipherText), key, SM4IV)).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public byte[] decryptCBCWithPadding(byte[] cipherText, String key) {
        try {
            SM4Util sm4Util = new SM4Util();
            return sm4Util.decryptDataCBCWithPadding(cipherText, key, SM4IV);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decryptCBCWithPadding(String cipherText, String key) {
        try {
            SM4Util sm4Util = new SM4Util();
            return new String(sm4Util.decryptDataCBCWithPadding(HexUtils.hexStringToBytes(cipherText), key, SM4IV)).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 随机生成指定长度的byte数组
     * @param length
     * @return
     */
    private byte[] randomByte(int length){
        byte[] randomBytes = new byte[length];
        ThreadLocalRandom.current().nextBytes(randomBytes);
        return randomBytes;
    }

    /**
     * 使用密码卡内部加密，返回 hexString
     * @param plain
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws NoSuchProviderException
     * @throws InvalidKeyException
     */
    public static String encryptMessage(String plain) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        byte[] bytes = SwsdsTools.SDF_Encrypt1(plain.getBytes(), 1);
        return HexUtils.bytesToHexString(bytes);
    }

    /**
     * 使用密码卡内部解密，返回 String
     * @param cipher
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws NoSuchProviderException
     * @throws InvalidKeyException
     */
    public static String decryptMessage(String cipher) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        byte[] bytes = HexUtils.hexStringToBytes(cipher);
        byte[] decrypt = SwsdsTools.SDF_Decrypt1(bytes, 1);
        return new String(decrypt);
    }

    /**
     * 使用密码卡内部加密，返回 byte
     * @param plain
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws NoSuchProviderException
     * @throws InvalidKeyException
     */
    public static byte[] encryptMessage(byte[] plain) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        return SwsdsTools.SDF_Encrypt1(plain, 1);
    }

    /**
     * 使用密码卡内部解密，返回 byte
     * @param cipher
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws NoSuchProviderException
     * @throws InvalidKeyException
     */
    public static byte[] decryptMessage(byte[] cipher) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        return SwsdsTools.SDF_Decrypt1(cipher, 1);
    }




}


