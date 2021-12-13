package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.KeyLimit;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface KeyLimitMapper {
    Long getKeyLimit(String userName);
    void addKeyLimit(KeyLimit keyLimit);
    boolean existKeyLimit(KeyLimit keyLimit);
    int updateKeyLimit(KeyLimit keyLimit);
    int deleteKeyLimit(KeyLimit keyLimit);

}
