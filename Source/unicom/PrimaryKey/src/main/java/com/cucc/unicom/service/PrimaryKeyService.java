package com.cucc.unicom.service;

import com.cucc.unicom.pojo.KeyVersion;
import com.cucc.unicom.pojo.PrimaryKey;
import com.cucc.unicom.pojo.dto.PrimaryKeyDTO;
import com.cucc.unicom.component.Exception.PwspException;

import java.util.List;
import java.util.Map;

public interface PrimaryKeyService {

    PrimaryKey addKey(PrimaryKey primaryKey) throws Exception;

    Object enableKey(PrimaryKey primaryKey)throws Exception;

    Object disableKey(PrimaryKey primaryKey)throws Exception;

    Object scheduleKeyDeletion(PrimaryKey primaryKey)throws Exception;

    Object cancelKeyDeletion(PrimaryKey primaryKey)throws Exception;

    PrimaryKeyDTO describeKey(PrimaryKey primaryKey) throws Exception;

    List<Map> listKeys(String userName)throws Exception;

    List<Map> listKeys1(String keyId)throws Exception;


    void updateKeyDescription(PrimaryKey primaryKey) throws Exception;

    KeyVersion describeKeyVersion(KeyVersion kv) throws Exception;

    List<Map> listKeyVersions(KeyVersion kv)throws Exception;

    void updateRotationPolicy(PrimaryKey primaryKey)throws Exception;

    KeyVersion createKeyVersion(PrimaryKey primaryKey)throws Exception;

    PrimaryKey getPrimaryKeybyKeyIdORalias(String keyIdORalias) throws PwspException;
    boolean keyStatsEnable(PrimaryKey primaryKey, int level)throws PwspException;

    void autoDeleteKey() throws Exception;

    void automatic() throws Exception;

}
