package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.EncryptionContext;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EncryptionContextMapper {

    int addEncryptionContext(@Param("encryptionContext") EncryptionContext encryptionContext);
    EncryptionContext getEncryptionContext(@Param("keyHash") String keyHash);
}




