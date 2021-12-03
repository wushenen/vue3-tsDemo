package com.qtec.unicom.mapper;

import com.qtec.unicom.pojo.ImportApiInfoDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ApiMapper {

    @Insert("insert into t_api_resource (api_name,api_URL,parent_id,comments) values (#{apiName},#{apiURL},#{parentId},#{comments});")
    int add(ImportApiInfoDTO importApiInfoDTO);

}
