package com.qtec.unicom.pojo.DTO;

import com.qtec.unicom.pojo.ApiResource;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
* 一次性获取资源
 * */
@Data
public class ResourceInfo implements Serializable {

    private Integer apiId;

    private String apiName;

    private String comments;

    private List<ApiResource> apiResources;

}
