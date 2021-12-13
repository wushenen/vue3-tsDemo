package com.cucc.src;

import com.qtec.qtecQr.QtecQr;
import com.qtec.qtecQr.QtecQrException;
import com.cucc.src.jni.Qr901JNI;
import com.sansec.jce.provider.SwxaProvider;
import com.cucc.src.api.Element;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.Security;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wuzh on 2019/12/27.
 * Describe: 抽取SM2、SM3、SM4、SM9中涉及的公共方法
 */
public class SMCommonUtils {

    public static final int BIGINTEGER_LENGTH = 32;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    /**
     * 伪随机数生产器，产生的随机数在区间[1,N-1]
     *
     * @param N
     * @return [1, N-1]区间的随机数
     */
    public static BigInteger pseudoRandomInN(BigInteger N) {
        BigInteger r;
        do {
            // 产生伪随机数r
            r = new BigInteger(N.bitLength(), new SecureRandom());
        } while (r.compareTo(N) >= 0 || r.compareTo(BigInteger.ONE) < 0);
        return r;
    }

    /**
     * 真随机数生成器，产生的随机数在区间[1,N-1]
     *
     * @param N
     * @return
     */
    public static BigInteger randomInN(BigInteger N) {
        BigInteger r;
        do {
            try {
                r = new BigInteger(1, genrateRandom(1, N.bitLength() >> 3));
            } catch (Throwable e) {
                System.err.println(e.getMessage());
                System.err.println("随机数发生器加载异常，下面尝试加载密码卡...");
                try {
                    r = new BigInteger(1, genrateRandom(2, N.bitLength() >> 3));
                } catch (Throwable ex) {
//                    throw new RuntimeException("随机数发生器 + 密码卡 都加载异常");
                    System.err.println("随机数发生器 + 密码卡 都加载异常");
                    System.out.println("32字节伪随机数");
                    r =  pseudoRandomInN(N);
                }
            }
        } while (r.compareTo(N) >= 0 || r.compareTo(BigInteger.ONE) < 0);
        return r;
    }

    /*
     * 将元素转换为byte数组
     */
    public static byte[] GTFiniteElementToByte(Element gt) {
        byte[] result = new byte[BIGINTEGER_LENGTH * 12];
        byte[] source = gt.toBytes();
        for (int i = 11; i >= 0; i--) {
            System.arraycopy(source, i << 5, result, (11 - i) << 5, 32);
        }
        return result;
    }

    /*
     * 拼接2个byte数组
     */
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        int byte1Length = byte_1.length;
        int byte2Length = byte_2.length;
        byte[] byte_3 = new byte[byte1Length + byte2Length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte1Length);
        System.arraycopy(byte_2, 0, byte_3, byte1Length, byte2Length);
        return byte_3;
    }

    /*
     * 多个byte数组拼接
     */
    public static byte[] join(byte[]... params) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] res = null;
        try {
            for (int i = 0, length = params.length; i < length; i++) {
                baos.write(params[i]);
            }
            res = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    /*
     * 以16进制打印字节数组byte[]
     */
    public static String printHexString(byte[] b) {
        int bLength = b.length;
        StringBuilder buf = new StringBuilder(bLength << 1);
        String hex;
        for (int i = 0; i < bLength; i++) {
            hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            buf.append(hex);
        }
        return buf.toString();
    }

    /*
     * 将16进制字符串转换为byte[]
     */
    public static byte[] hexString2Bytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }
        int srtLength = str.length() >> 1;
        byte[] bytes = new byte[srtLength];
        String subStr;
        for (int i = 0; i < srtLength; i++) {
            subStr = str.substring(i << 1, (i << 1) + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }

    /**
     * 从随机数源获取真随机数
     *
     * @param rngSource 真随机数源，1表示从QRNG(随机数发生器)获取，2表示从加密卡获取
     * @param keyLen    产生的真随机数字节长度，256位(bit)的数，一般为32字节(Byte)
     * @return
     */
    public static byte[] genrateRandom(int rngSource, int keyLen,String... param) throws Exception {
        byte[] data = new byte[keyLen];
        int loopTimes = 1;
        if (rngSource == 1) {
            try {
                Class.forName("com.qtec.qtecQr.QtecQr");
            } catch (Throwable e){  // 此处原来的Exception改为Throwable就可以了
                throw new RuntimeException("Try to load QtecQr failed!\n" + e.getMessage());
            }
            int countPci = QtecQr.Count(QtecQr.QtecQrDeviceType.QTEC_QR_DEVICE_PCI);
            int countUsb = QtecQr.Count(QtecQr.QtecQrDeviceType.QTEC_QR_DEVICE_USB);

            if (countUsb == 0 && countPci == 0) {
                throw new RuntimeException("No QtecQr device installed.\n");
            }
            QtecQr[] qtecQrs = new QtecQr[countPci + countUsb];
            if (countPci > 0) {
                System.out.printf("Using {%d} QtecQr PCI device.\n", countPci);
                for (int i = 0; i < countPci; i++) {
                    qtecQrs[i] = new QtecQr(QtecQr.QtecQrDeviceType.QTEC_QR_DEVICE_PCI, i);
                }
            }
            if (countUsb > 0) {
                for (int i = countPci, j = 0; j < countUsb; i++, j++) {
                    qtecQrs[i] = new QtecQr(QtecQr.QtecQrDeviceType.QTEC_QR_DEVICE_USB, j);
                }
            }
            CountDownLatch countDownLatch = new CountDownLatch(qtecQrs.length);
            for (QtecQr qtecQr : qtecQrs) {
                threadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            byte[] buffer;
                            for (int i = 0; i < loopTimes; i++) {
                                try {
                                    buffer = qtecQr.Read(keyLen);
                                } catch (QtecQrException e) {
                                    throw new RuntimeException("QtecQrException:\n" + e.getMessage());
                                }
                                System.arraycopy(buffer, 0, data, 0, keyLen);
                            }
                        } finally {
                            countDownLatch.countDown();
                        }
                    }
                });
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException("InterruptedException:\n" + e.getMessage());
            }
        } else if (rngSource == 2) {
            System.out.printf("Get RandomDigit from SWXA\n");
            try {
                Security.addProvider(new SwxaProvider());
            } catch (Throwable e) {
                throw new RuntimeException("Add provider failed:\n" + e.getMessage());
            }
            SecureRandom random;
            try {
                random = SecureRandom.getInstance("RND", "SwxaJCE");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
            byte[] buffer;
            for (int i = 0; i < loopTimes; i++) {
                try {
                    buffer = random.generateSeed(keyLen);
                    System.arraycopy(buffer, 0, data, 0, keyLen);
                } catch (Exception e) {
                    throw new RuntimeException("generateSeed Error:\n" + e.getMessage());
                }
            }
        }else if (rngSource == 3) {
            System.out.printf("Get Random from 901\n");

            try {
                Qr901JNI t = new Qr901JNI();
                String ip = param[0];
                String[] iparr = ip.split(".");
                long server_ip = (long)((Integer.parseInt(iparr[0]) << 24) + (Integer.parseInt(iparr[1]) << 16) + (Integer.parseInt(iparr[2]) << 8) + Integer.parseInt(iparr[3]));
                byte[] buffer = new byte[keyLen];
                System.out.println("----------获取901随机数0-------------");
                t.QRNG_getRandom(server_ip, buffer, (long)buffer.length);
                System.arraycopy(buffer, 0, data, 0, keyLen);
            } catch (Throwable var13) {
                throw new RuntimeException("Add provider failed:\n" + var13.getMessage());
            }
        }else{
            BigInteger bigint = pseudoRandomInN(new BigInteger("B640000002A3A6F1d603AB4FF58EC74449F2934B18EA8BEEE56EE19CD69ECF25", 16));
            System.arraycopy(bigint.toByteArray(), 0, data, 0, keyLen);
        }
        return data;
    }


}
