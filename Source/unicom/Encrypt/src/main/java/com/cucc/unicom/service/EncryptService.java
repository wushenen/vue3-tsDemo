package com.cucc.unicom.service;

import com.cucc.unicom.pojo.EncryptionContext;

public interface EncryptService {
    EncryptionContext encrypt(EncryptionContext encrypt)throws Exception;

    EncryptionContext decrypt(EncryptionContext encrypt)throws Exception;
}
