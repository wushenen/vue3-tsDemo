package com.cucc.unicom.controller;

import com.cucc.unicom.component.Result;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.pojo.ApiResource;
import com.cucc.unicom.pojo.DTO.ApiResourceCategory;
import com.cucc.unicom.pojo.DTO.ResourceInfo;
import com.cucc.unicom.service.ApiResourceService;
import com.cucc.unicom.controller.vo.AddResourceCategoryRequest;
import com.cucc.unicom.controller.vo.AddResourceInfoRequest;
import com.cucc.unicom.controller.vo.UpdateResourceCategoryRequest;
import com.cucc.unicom.controller.vo.UpdateResourceInfoRequest;
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

    @ApiOperation(value = "添加资源分类", notes = "添加资源分类")
    @RequestMapping(value = "/addCategory",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomAddCategory(@RequestBody AddResourceCategoryRequest addResourceCategoryRequest) {
        boolean exist = apiResourceService.resourceNameExist(addResourceCategoryRequest.getApiName());
        if (!exist) {
            int res = apiResourceService.addResourceCategory(addResourceCategoryRequest.getApiName(), addResourceCategoryRequest.getComments());
            if (res == 1) {
                return ResultHelper.genResultWithSuccess();
            } else
                return ResultHelper.genResult(1, "添加资源分类失败");
        } else
            return ResultHelper.genResult(1, "分类名称重复");
    }

    @ApiOperation(value = "添加接口资源信息", notes = "添加接口资源信息")
    @RequestMapping(value = "/addResource",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomAddResource(@RequestBody AddResourceInfoRequest addResourceInfoRequest) {
        boolean exist = apiResourceService.apiNameExist(addResourceInfoRequest.getApiName());
        if (!exist) {
            int res = apiResourceService.addResourceInfo(addResourceInfoRequest.getApiName(),addResourceInfoRequest.getApiURL(),addResourceInfoRequest.getParentId(),addResourceInfoRequest.getComments());
            if (res == 1) {
                return ResultHelper.genResultWithSuccess();
            } else
                return ResultHelper.genResult(1, "添加接口资源信息失败");
        } else
            return ResultHelper.genResult(1, "接口资源名称重复");
    }

    @ApiOperation(value = "删除资源信息", notes = "删除资源信息")
    @RequestMapping(value = "/deleteResource",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomDeleteResource(@RequestParam("apiId") int apiId) {
        int res = apiResourceService.delResource(apiId);
        if (res > 0) {
            return ResultHelper.genResultWithSuccess();
        } else
            return ResultHelper.genResult(1, "删除资源信息失败");
    }


    @ApiOperation(value = "修改资源分类信息", notes = "修改资源分类信息")
    @RequestMapping(value = "/updateCategory",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomUpdateCategory(@RequestBody UpdateResourceCategoryRequest updateResourceCategoryRequest) {
        boolean exist = apiResourceService.resourceNameExist(updateResourceCategoryRequest.getApiName());
        boolean equals = apiResourceService.getResourceInfo(updateResourceCategoryRequest.getApiId()).getApiName().equals(updateResourceCategoryRequest.getApiName());
        if (!exist || equals){
            int res = apiResourceService.updateResourceCategory(updateResourceCategoryRequest.getApiId(),updateResourceCategoryRequest.getApiName(),updateResourceCategoryRequest.getComments());
            if (res == 1) {
                return ResultHelper.genResultWithSuccess();
            } else
                return ResultHelper.genResult(1, "修改资源信息失败");
        }else
            return ResultHelper.genResult(1,"分类名称重复");
    }


    @ApiOperation(value = "修改接口资源信息", notes = "修改接口资源信息")
    @RequestMapping(value = "/updateResource",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomUpdateResource(@RequestBody UpdateResourceInfoRequest updateResourceInfoRequest) {
        boolean exist = apiResourceService.apiNameExist(updateResourceInfoRequest.getApiName());
        boolean equals = apiResourceService.getResourceInfo(updateResourceInfoRequest.getApiId()).getApiName().equals(updateResourceInfoRequest.getApiName());
        if (!exist || equals){
            int res = apiResourceService.updateResourceInfo(updateResourceInfoRequest.getApiId(),updateResourceInfoRequest.getApiName(),updateResourceInfoRequest.getApiURL(),updateResourceInfoRequest.getParentId(),updateResourceInfoRequest.getComments());
            if (res == 1) {
                return ResultHelper.genResultWithSuccess();
            } else
                return ResultHelper.genResult(1, "修改接口资源信息失败");
        }else
            return ResultHelper.genResult(1,"接口资源名称重复");
    }


    @ApiOperation(value = "获取所有资源分类信息", notes = "获取所有资源分类信息")
    @RequestMapping(value = "/getCategory",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomGetCategory() {
        List<ApiResourceCategory> resourceCategory = apiResourceService.getResourceCategory();
        return ResultHelper.genResultWithSuccess(resourceCategory);
    }

    @ApiOperation(value = "获取该资源分类下的接口信息", notes = "获取该资源分类下的接口信息")
    @RequestMapping(value = "/getApiResource",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomGetApiResource(@RequestParam("apiId") int apiId) {
        List<ApiResource> apiResources = apiResourceService.getApiResource(apiId);
        return ResultHelper.genResultWithSuccess(apiResources);
    }


    @ApiOperation(value = "一次性获取接口资源信息", notes = "一次获取全部接口资源信息")
    @RequestMapping(value = "/getResource",method = RequestMethod.POST)
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
            for (ResourceInfo info : resourceInfos) {
                System.out.println(info);
            }
        }
        return ResultHelper.genResultWithSuccess(resourceInfos);
    }


}
