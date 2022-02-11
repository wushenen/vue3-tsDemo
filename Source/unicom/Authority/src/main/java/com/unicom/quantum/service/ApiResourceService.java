package com.unicom.quantum.service;

import com.unicom.quantum.pojo.ApiResource;
import com.unicom.quantum.pojo.DTO.ApiResourceCategory;

import java.util.List;

public interface ApiResourceService {

    List<ApiResourceCategory> getResourceCategory();
    List<ApiResource> getApiResource(int apiId);
    ApiResource getResourceInfo(int apiId);

}
