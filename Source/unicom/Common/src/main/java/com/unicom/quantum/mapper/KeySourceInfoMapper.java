package com.unicom.quantum.mapper;

import com.unicom.quantum.pojo.dto.KeySourceDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface KeySourceInfoMapper {

    int addKeySourceInfo(int keySource, String keyGenerateRate, Long keyGenerateNum);
    boolean keySourceInfoExist(int keySource);
    int updateKeySourceInfo(int keySource, String keyGenerateRate, Long keyGenerateNum);
    List<KeySourceDetail> getKeySourceInfo();

}
