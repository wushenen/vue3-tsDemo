package com.qtec.unicom.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface KeyLimitMapper {
    Long getKeyLimit(String userName);
}
