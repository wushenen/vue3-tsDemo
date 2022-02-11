package com.unicom.quantum.mapper;

import com.unicom.quantum.pojo.ApiResource;
import com.unicom.quantum.pojo.DTO.ApiResourceCategory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ApiResourceMapper {
    List<ApiResourceCategory> getResourceCategory();
    List<ApiResource> getApiResource(int apiId);
    ApiResource getResourceInfo(int apiId);
}
