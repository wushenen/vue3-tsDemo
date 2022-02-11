package com.unicom.quantum.jni;

public class KMSJNI {

    static{
        System.loadLibrary("jnikms");
    }

    public KMSJNI() {
    }

    public native byte[] kms_sdk_output_key_plaintext(int len,String ip, String name, String peerName,byte[] devKey,byte[] cryptKey,boolean flag, byte[] keyId);

}
