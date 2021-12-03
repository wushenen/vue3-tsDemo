package com.qtec.unicom.mapper;

import com.qtec.unicom.pojo.Material;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MaterialMapper {

    int addMaterial(@Param("material") Material material);
    int deleteMaterial(@Param("material") Material material);
    Material selectMaterial(@Param("keyId") String keyId);

    void updateMaterial(@Param("material") Material material);
}




