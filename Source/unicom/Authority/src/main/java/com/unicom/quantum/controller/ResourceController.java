package com.unicom.quantum.controller;

import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.pojo.ApiResource;
import com.unicom.quantum.pojo.DTO.ApiResourceCategory;
import com.unicom.quantum.pojo.DTO.ResourceInfo;
import com.unicom.quantum.service.ApiResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(value = "资源管理接口",tags = {"资源管理接口"})
@RestController
@RequestMapping(value = "/resource")
public class ResourceController {

    @Autowired
    private ApiResourceService apiResourceService;


    @ApiOperation(value = "获取所有资源分类信息", notes = "获取所有资源分类信息")
    @PostMapping(value = "/getCategory")
    @ResponseBody
    public Result unicomGetCategory() {
        List<ApiResourceCategory> resourceCategory = apiResourceService.getResourceCategory();
        return ResultHelper.genResultWithSuccess(resourceCategory);
    }

    @ApiOperation(value = "获取该资源分类下的接口信息", notes = "获取该资源分类下的接口信息")
    @GetMapping(value = "/getApiResource")
    @ResponseBody
    public Result unicomGetApiResource(@RequestParam("apiId") int apiId) {
        List<ApiResource> apiResources = apiResourceService.getApiResource(apiId);
        return ResultHelper.genResultWithSuccess(apiResources);
    }


    @ApiOperation(value = "一次性获取接口资源信息", notes = "一次获取全部接口资源信息")
    @GetMapping(value = "/getResource")
    @ResponseBody
    public Result unicomGetResource() {
        List<ResourceInfo> resourceInfos = new ArrayList<>();
        List<ApiResourceCategory> resourceCategory = apiResourceService.getResourceCategory();
        for (ApiResourceCategory category : resourceCategory) {
            ResourceInfo resourceInfo = new ResourceInfo();
            resourceInfo.setApiId(category.getApiId());
            resourceInfo.setApiName(category.getApiName());
            resourceInfo.setComments(category.getComments());
            resourceInfo.setApiResources(apiResourceService.getApiResource(category.getApiId()));
            resourceInfos.add(resourceInfo);
        }
        return ResultHelper.genResultWithSuccess(resourceInfos);
    }


}
