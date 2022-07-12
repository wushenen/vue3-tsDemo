package com.unicom.quantum.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author jerry
 * @version 1.0
 * @note
 * @date 2022/5/13
 */
@Mapper
@Repository
public interface BrainMapper {

    Long getRegisterNum();
    Long getOnlineNum();
    Long getKeyInNum();
    Long getKeyOutNum();

}
