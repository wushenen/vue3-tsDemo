package com.unicom.quantum.service;

import com.unicom.quantum.pojo.App;
import com.unicom.quantum.pojo.DeviceUser;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public interface LoginService {
    DeviceUser deviceUserLogin(String deviceName) throws Exception;
    App appLogin(String appKey) throws Exception;
}
