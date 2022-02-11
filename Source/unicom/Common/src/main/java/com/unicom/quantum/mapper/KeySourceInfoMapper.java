package com.unicom.quantum.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface KeySourceInfoMapper {

    int addKeySourceInfo(int keySource, String keyGenerateRate, Long keyGenerateNum);
    boolean keySourceInfoExist(int keySource);
    int updateKeySourceInfo(int keySource, String keyGenerateRate, Long keyGenerateNum);

}
