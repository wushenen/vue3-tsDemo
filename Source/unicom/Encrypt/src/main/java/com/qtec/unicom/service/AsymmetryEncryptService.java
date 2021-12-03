package com.qtec.unicom.service;

import com.qtec.unicom.pojo.AsymmetricData;
import com.qtec.unicom.pojo.KeyVersion;
import com.qtec.unicom.pojo.PrimaryKey;

public interface AsymmetryEncryptService {
    byte[] primaryKeyEncryptAsymmetry(PrimaryKey primaryKey, byte[] plaintext) throws Exception;

    byte[] primaryKeyDecryptAsymmetry(PrimaryKey primaryKey, byte[] ciphertextBlobByte) throws Exception;

    AsymmetricData asymmetricSign(AsymmetricData encrypt)throws Exception;

    AsymmetricData asymmetricVerify(AsymmetricData asymmetricData)throws Exception;

    AsymmetricData asymmetricEncrypt(AsymmetricData asymmetricData)throws Exception;

    AsymmetricData asymmetricDecrypt(AsymmetricData asymmetricData)throws Exception;

    AsymmetricData getPublicKey(AsymmetricData asymmetricData)throws Exception;

    KeyVersion getKeyVersionAsymmetry(PrimaryKey primaryKey)throws Exception;

}
