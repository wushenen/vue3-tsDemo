package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("获取临时密钥请求参数")
@Data
public class GenerateTempKeyRequest {

    private Integer keyLen;

    private String keyId;

    private byte[]tempKey;
}
