package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("获取随机数请求参数")
@Data
public class GenerateRandomRequest {

    private Integer randomLength;

}
