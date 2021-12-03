package com.qtec.unicom.mapper;

import com.qtec.unicom.pojo.Alias;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AliasMapper {

    int addAlias(@Param("alias") Alias alias);
    int updateAlias(@Param("alias") Alias alias);
    int deleteAlias(@Param("aliasName") String aliasName);

    Alias getAlias(@Param("aliasName") String aliasName);
    List<Alias> listAlias(@Param("alias") Alias alias);

}




