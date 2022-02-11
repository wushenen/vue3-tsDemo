package com.unicom.quantum.mapper;

import com.unicom.quantum.pojo.IpInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IpMapper {

    @Insert("insert into t_ip(ip_info) values(#{ipInfo})")
    int addIp(@Param("ipInfo") String ipInfo);

    @Select("select * from t_ip order by create_time desc")
    List<IpInfo> getAllIps();

    @Delete("delete from t_ip where ip_info = #{ipInfo}")
    int deleteIpById(@Param("ipInfo") String ipInfo);

}




