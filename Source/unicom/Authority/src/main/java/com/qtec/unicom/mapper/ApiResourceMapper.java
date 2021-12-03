package com.qtec.unicom.mapper;

import com.qtec.unicom.pojo.ApiResource;
import com.qtec.unicom.pojo.DTO.ApiResourceCategory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ApiResourceMapper {

    /*添加资源分类*/
    int addResourceCategory(String apiName, String comments);

    /*判断分类名称是否重复*/
    boolean resourceNameExist(String apiName);

    /*添加资源信息*/
    int addResourceInfo(String apiName, String apiURL, int parentId, String comments);

    /*判断接口名称是否重复*/
    boolean apiNameExist(String apiName);

    /*删除资源信息*/
    int delResource(int apiId);

    /*修改资源分类信息*/
    int updateResourceCategory(int apiId, String apiName, String comments);

    /*修改资源信息*/
    int updateResourceInfo(int apiId, String apiName, String apiURL, int parentId, String comments);

    /*获取所有资源分类信息*/
    List<ApiResourceCategory> getResourceCategory();

    /*获取所有资源信息*/
    List<ApiResource> getAllResource();

    /*获取该分类资源下的接口资源信息*/
    List<ApiResource> getApiResource(int apiId);

    /*获取特定资源信息,用来获取资源名称*/
    ApiResource getResourceInfo(int apiId);


}
