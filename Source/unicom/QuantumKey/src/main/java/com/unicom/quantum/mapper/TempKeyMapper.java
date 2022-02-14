package com.unicom.quantum.mapper;

import com.unicom.quantum.controller.vo.GenerateTempKeyRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TempKeyMapper {

    int addAppSecret(GenerateTempKeyRequest generateTempKeyRequest);

}




