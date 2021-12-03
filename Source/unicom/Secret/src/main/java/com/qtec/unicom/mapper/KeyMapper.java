package com.qtec.unicom.mapper;

import com.qtec.unicom.pojo.Key;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface KeyMapper {

//    @Select("select p.key_spec keySpec,p.primary_key_version versionId,p.arn,p.origin,p.owner,v.card_index cardIndex " +
//            "from t_primary_key p right join t_key_version v on p.key_id = v.key_id where p.key_id = #{keyId}")
    Key getKey(@Param("keyId") String keyId);

}
