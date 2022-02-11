package com.unicom.quantum.mapper;

import com.unicom.quantum.pojo.QkmVersion;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;


/**
 * Describe:
 */
@Mapper
@Repository
public interface QkmVersionMapper {

    @Select("select * from t_qkm_version where mac_addr = #{macAddr}")
    QkmVersion getQkmVersion(@Param("macAddr") String macAddr);
    @Update("update t_qkm_version set state = 1,param = '就绪' where mac_addr = #{macAddr}")
    int updateStateOk(@Param("macAddr") String macAddr);
    @Insert("insert into t_qkm_version(version,mac_addr) values(#{qk.version},#{qk.macAddr})")
    int addQkmVersion(@Param("qk") QkmVersion qk);
    @Select("select version()")
    String getMysqlVersion();
}




