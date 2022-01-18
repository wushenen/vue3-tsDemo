package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.Random;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TempKeyMapper {

    @Insert("insert into t_temp_key(key_id,temp_key) " +
            "values(#{random.keyId},#{random.tempKey})")
    int addAppSecret(@Param("random") Random random);

}




