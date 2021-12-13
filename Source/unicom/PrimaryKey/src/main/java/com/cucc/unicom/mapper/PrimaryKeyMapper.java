package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.PrimaryKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface PrimaryKeyMapper {

    int addKey(@Param("primaryKey") PrimaryKey primaryKey);
    int updatePrimaryKeyState(@Param("primaryKey") PrimaryKey primaryKey);
    int updatePrimaryKeyDesc(@Param("primaryKey") PrimaryKey primaryKey);
    PrimaryKey getPrimaryKey(@Param("keyId") String keyId);
    List<Map> listPrimaryKey(@Param("userName") String userName);
    int updateRotationPolicy(@Param("primaryKey") PrimaryKey primaryKey);
    //自动删除
    List<PrimaryKey> listPrimaryKeyOutTime(@Param("now") Date now);
    int deletePrimaryKey(@Param("now") Date now);
    //自动轮转
    List<PrimaryKey> listAutomaticKey();

    List<Map> listKeys1(@Param("keyId") String keyId);
}




