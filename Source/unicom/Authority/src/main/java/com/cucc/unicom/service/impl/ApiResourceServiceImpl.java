package com.cucc.unicom.service.impl;

import com.cucc.unicom.mapper.ApiResourceMapper;
import com.cucc.unicom.pojo.ApiResource;
import com.cucc.unicom.pojo.DTO.ApiResourceCategory;
import com.cucc.unicom.service.ApiResourceService;
import com.cucc.unicom.component.annotation.OperateLogAnno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiResourceServiceImpl implements ApiResourceService {

    private final String OPERATE_MODEL = "资源模块";

    @Autowired
    private ApiResourceMapper apiResourceMapper;

    @OperateLogAnno(operateDesc = "添加资源分类", operateModel = OPERATE_MODEL)
    @Override
    public int addResourceCategory(String apiName, String comments) {
        return apiResourceMapper.addResourceCategory(apiName, comments);
    }

    @Override
    public boolean resourceNameExist(String apiName) {
        return apiResourceMapper.resourceNameExist(apiName);
    }

    @OperateLogAnno(operateDesc = "添加资源操作信息", operateModel = OPERATE_MODEL)
    @Override
    public int addResourceInfo(String apiName, String apiURL, int parentId, String comments) {
        return apiResourceMapper.addResourceInfo(apiName, apiURL, parentId, comments);
    }

    @Override
    public boolean apiNameExist(String apiName) {
        return apiResourceMapper.apiNameExist(apiName);
    }


    @OperateLogAnno(operateDesc = "删除资源", operateModel = OPERATE_MODEL)
    @Override
    public int delResource(int apiId) {
        return apiResourceMapper.delResource(apiId);
    }

    @OperateLogAnno(operateDesc = "修改资源分类信息", operateModel = OPERATE_MODEL)
    @Override
    public int updateResourceCategory(int apiId, String apiName, String comments) {
        return apiResourceMapper.updateResourceCategory(apiId, apiName, comments);
    }


    @OperateLogAnno(operateDesc = "修改资源操作信息", operateModel = OPERATE_MODEL)
    @Override
    public int updateResourceInfo(int apiId, String apiName, String apiURL, int parentId, String comments) {
        return apiResourceMapper.updateResourceInfo(apiId, apiName, apiURL, parentId, comments);
    }

    @OperateLogAnno(operateDesc = "获取资源分类信息", operateModel = OPERATE_MODEL)
    @Override
    public List<ApiResourceCategory> getResourceCategory() {
        return apiResourceMapper.getResourceCategory();
    }


    @OperateLogAnno(operateDesc = "获取所有资源信息", operateModel = OPERATE_MODEL)
    @Override
    public List<ApiResource> getAllResource() {
        return apiResourceMapper.getAllResource();
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
