package com.qtec.unicom.service;


import com.qtec.unicom.pojo.SM9Encrypt;
import com.qtec.unicom.pojo.dto.SM9ParamsDTO;

public interface SM9EncryptService {

    SM9ParamsDTO sm9params(String IDB, SM9Encrypt sm9Encrypt)throws Exception;

    SM9Encrypt sm9Encrypt(String idb, SM9Encrypt sm9Encrypt)throws Exception;

    SM9Encrypt sm9Decrypt(String idb, SM9Encrypt sm9Encrypt)throws Exception;

    SM9Encrypt sm9Sign(String idb, SM9Encrypt sm9Encrypt)throws Exception;

    SM9Encrypt sm9VeritySign(String idb, SM9Encrypt sm9Encrypt)throws Exception;
}
