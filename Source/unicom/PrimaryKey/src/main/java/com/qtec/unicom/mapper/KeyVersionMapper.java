package com.qtec.unicom.mapper;

import com.qtec.unicom.pojo.KeyVersion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface KeyVersionMapper {

    int addKeyVersion(@Param("keyVersion") KeyVersion keyVersion);
    KeyVersion getKeyVersion(@Param("keyVersion") KeyVersion keyVersion);
    List<Map> listKeyVersion(@Param("keyVersion") KeyVersion keyVersion);
    int updateKeyVersionCardIndex(@Param("keyVersion") KeyVersion keyVersion);

    int deleteKeyVersion(@Param("keyId") String keyId);


}




