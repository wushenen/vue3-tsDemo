package com.unicom.quantum.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ShiroAuthMapper {

    List<Integer> getGroupInfosByDeviceId(int deviceId);

}
