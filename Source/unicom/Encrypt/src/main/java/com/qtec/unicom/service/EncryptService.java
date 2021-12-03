package com.qtec.unicom.service;

import com.qtec.unicom.pojo.DataKey;
import com.qtec.unicom.pojo.EncryptionContext;

public interface EncryptService {
    EncryptionContext encrypt(EncryptionContext encrypt)throws Exception;

    EncryptionContext decrypt(EncryptionContext encrypt)throws Exception;

    DataKey generateDataKey(DataKey dataKey)throws Exception;

    DataKey exportDataKey(DataKey dataKey)throws Exception;

    DataKey generateAndExportDataKey(DataKey dataKey)throws Exception;

    DataKey reEncrypt(DataKey dataKey)throws Exception;
}
