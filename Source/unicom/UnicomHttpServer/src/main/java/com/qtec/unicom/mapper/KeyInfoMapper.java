package com.qtec.unicom.mapper;

import com.qtec.unicom.pojo.KeyInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface KeyInfoMapper {
    void addKeyInfo(byte[] keyId, byte[] keyValue, String applicant, int keyStatus);
    KeyInfo getKeyInfo(byte[] keyId);
    int updateKeyInfo(byte[] keyId, int keyStatus);
    void deleteKeyInfo(byte[] keyId);
    List<KeyInfo> getKeyInfos(String applicant, int keyStatus);

    List<KeyInfo> getKeyInfosNotInKeyStatus(String applicant, int keyStatus);

    Long getTotalNum(String applicant);
    Long getUsedNum(String applicant);

    String getAdminEmail();

    Long getDeviceStatusKeyNum(String deviceName);
}
