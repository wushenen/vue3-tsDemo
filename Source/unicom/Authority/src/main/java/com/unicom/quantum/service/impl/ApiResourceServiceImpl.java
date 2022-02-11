package com.unicom.quantum.service.impl;

import com.unicom.quantum.component.annotation.OperateLogAnno;
import com.unicom.quantum.mapper.ApiResourceMapper;
import com.unicom.quantum.pojo.ApiResource;
import com.unicom.quantum.pojo.DTO.ApiResourceCategory;
import com.unicom.quantum.service.ApiResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiResourceServiceImpl implements ApiResourceService {

    private final String OPERATE_MODEL = "资源模块";

    @Autowired
    private ApiResourceMapper apiResourceMapper;

    @OperateLogAnno(operateDesc = "获取资源分类信息", operateModel = OPERATE_MODEL)
    @Override
    public List<ApiResourceCategory> getResourceCategory() {
        return apiResourceMapper.getResourceCategory();
    }

    @OperateLogAnno(operateDesc = "获取指定资源下的接口资源信息", operateModel = OPERATE_MODEL)
    @Override
    public List<ApiResource> getApiResource(int apiId) {
        return apiResourceMapper.getApiResource(apiId);
    }

    @OperateLogAnno(operateDesc = "获取特定资源信息", operateModel = OPERATE_MODEL)
    @Override
    public ApiResource getResourceInfo(int apiId) {
        return apiResourceMapper.getResourceInfo(apiId);
    }
}
