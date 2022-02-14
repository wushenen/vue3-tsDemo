package com.unicom.quantum.mapper;

import com.unicom.quantum.pojo.Random;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TempKeyMapper {

    int addAppSecret(@Param("random") Random random);

}




