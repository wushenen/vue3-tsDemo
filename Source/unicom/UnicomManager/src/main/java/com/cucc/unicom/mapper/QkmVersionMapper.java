package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.QkmVersion;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;


/**
 * Describe:
 */
@Mapper
@Repository
public interface QkmVersionMapper {

    @Select("select id,version,state,param,key_control_code,name,verify_war,number,public_key,sign_war,mac_addr " +
            " from t_qkm_version where mac_addr = #{macAddr}")
    QkmVersion getQkmVersion(@Param("macAddr") String macAddr);
    @Update("update t_qkm_version set "
            + "state = 0," +
            "param = '初始' "
            + " where mac_addr = #{macAddr}")
    int updateState(@Param("macAddr") String macAddr);
    @Update("update t_qkm_version set "
            + "state = 1," +
            "param = '就绪' "
            + " where mac_addr = #{macAddr}")
    int updateStateOk(@Param("macAddr") String macAddr);
    @Update("update t_qkm_version set "
            + "state = 1," +
            "param = '就绪' "
            + " where mac_addr = #{macAddr}")
    int updateStateError(@Param("state") Integer state, @Param("param") String param, @Param("macAddr") String macAddr);
    @Insert("insert into t_qkm_version(mac_addr,verify_war,public_key,sign_war) " +
            " values(#{qk.macAddr},#{qk.verifyWar},#{qk.publicKey},#{qk.signWar})")
    int addQkmVersion(@Param("qk") QkmVersion qk);
}




