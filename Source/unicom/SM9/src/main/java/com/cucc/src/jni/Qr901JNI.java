//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cucc.src.jni;

public class Qr901JNI {
    public Qr901JNI() {
    }

    public native void QRNG_init();

    public native int QRNG_getRandom(long var1, byte[] var3, long var4);

    public native int QRNG_net_init(int[] var1, long var2);

    public native int QRNG_read_random(int[] var1, byte[] var2, long var3);

    public native int QRNG_disconncet_net(int[] var1);

    public native int QRNG_destroy(int[] var1);

    public native int QRNG_get_dev_sta(int[] var1, int var2);

    static {
        System.loadLibrary("QR901JNI");
    }
}
