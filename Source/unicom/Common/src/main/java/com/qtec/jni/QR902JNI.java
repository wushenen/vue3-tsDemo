package com.qtec.jni;

public class QR902JNI {
    static{
        System.loadLibrary("jniqr902");
    }

    public QR902JNI() {
    }

    public native byte[] QRNG_read_random(String ip,  int len);
    public native byte[] QRNG_usb_read_random(int len);
    public native String[] QRNG_get_lib_version();
}
