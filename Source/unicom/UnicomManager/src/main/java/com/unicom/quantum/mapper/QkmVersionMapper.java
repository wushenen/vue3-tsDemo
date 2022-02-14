package com.unicom.quantum.mapper;

import com.unicom.quantum.pojo.QkmVersion;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface QkmVersionMapper {

    QkmVersion getQkmVersion(String macAddr);
    int updateStateOk(String macAddr);
    int addQkmVersion(QkmVersion qkmVersion);
    String getMysqlVersion();
}




