package com.cucc.unicom.service;

import com.cucc.unicom.pojo.AsymmetricData;
import com.cucc.unicom.pojo.KeyVersion;
import com.cucc.unicom.pojo.PrimaryKey;

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
