package com.cucc.src.sm2;

import com.cucc.src.SMCommonUtils;
import com.cucc.src.sm3.SM3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by wuzh on 2019/12/30.
 * Describe: SM2中使用的各种杂凑函数、派生函数
 */
public class Sm2Utils {

    /**
     * 密钥派生函数
     *
     * @param Z     比特串Z——双方共享的数据
     * @param klen  生成klen字节数长度的密钥
     * @return
     */
    public static byte[] KDF(byte[] Z, int klen) {
        int ct = 1;
        int end = (int) Math.ceil(klen * 1.0 / 32);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            for (int i = 1; i < end; i++) {
                baos.write(sm3hash(Z, SM3.toByteArray(ct)));
                ct++;
            }
            byte[] last = sm3hash(Z, SM3.toByteArray(ct));
            if (klen % 32 == 0) {
                baos.write(last);
            } else
                baos.write(last, 0, klen % 32);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * sm3摘要
     *
     * @param params
     * @return
     */
    public static byte[] sm3hash(byte[]... params) {
        byte[] res = null;
        try {
            res = SM3.hash(SMCommonUtils.join(params));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
