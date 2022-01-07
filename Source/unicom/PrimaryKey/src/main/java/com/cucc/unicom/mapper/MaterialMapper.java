package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.Material;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MaterialMapper {
    int deleteMaterial(@Param("material") Material material);
    Material selectMaterial(@Param("keyId") String keyId);
}




