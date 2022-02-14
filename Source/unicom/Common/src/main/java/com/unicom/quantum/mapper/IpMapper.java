package com.unicom.quantum.mapper;

import com.unicom.quantum.pojo.IpInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IpMapper {

    int addIp(@Param("ipInfo") String ipInfo);
    List<IpInfo> getAllIps();
    int deleteIpById(@Param("ipInfo") String ipInfo);

}




