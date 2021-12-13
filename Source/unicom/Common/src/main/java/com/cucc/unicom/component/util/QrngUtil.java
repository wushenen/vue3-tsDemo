package com.cucc.unicom.component.util;

import com.qtec.qtecQr.QtecQr;
import com.qtec.qtecQr.QtecQrException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QrngUtil {

    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    /**
     * 从真随机数源 qrng 获取指定长度随机数
     * @param keyLen
     * @return
     * @throws Exception
     */
    public static byte[] genrateRandom(int keyLen) throws Exception {
        byte[] data = new byte[keyLen];
        int loopTimes = 1;
        try {
            Class.forName("com.qtec.qtecQr.QtecQr");
        } catch (Throwable e){  // 此处原来的Exception改为Throwable就可以了
            return data;
        }
        int countPci = QtecQr.Count(QtecQr.QtecQrDeviceType.QTEC_QR_DEVICE_PCI);
        int countUsb = QtecQr.Count(QtecQr.QtecQrDeviceType.QTEC_QR_DEVICE_USB);
        if (countUsb == 0 && countPci == 0) {
            return data;
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
            return data;
        }
        return data;
    }
}
