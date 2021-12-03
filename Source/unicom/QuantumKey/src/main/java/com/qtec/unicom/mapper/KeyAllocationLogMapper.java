package com.qtec.unicom.mapper;

import com.qtec.unicom.pojo.KeyAllocationLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface KeyAllocationLogMapper {
    void addAllocationLog(KeyAllocationLog keyAllocationLog);
    List<KeyAllocationLog> getAllocationLog(String assignedName);
}
