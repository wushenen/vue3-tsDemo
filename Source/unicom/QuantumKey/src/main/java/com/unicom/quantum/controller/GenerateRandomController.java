package com.unicom.quantum.controller;


import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.component.util.UtilService;
import com.unicom.quantum.controller.vo.GenerateRandomRequest;
import com.unicom.quantum.controller.vo.GenerateTempKeyRequest;
import com.unicom.quantum.pojo.DTO.TempKeyDTO;
import com.unicom.quantum.service.GenerateRandomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Base64;


/**
 * 生成一个随机口令字符串
 */
@Api(value = "量子密钥接口",tags = {"量子密钥接口"})
@Controller
@RequestMapping("/v1/kms")
public class GenerateRandomController {

    private static final Logger logger = LoggerFactory.getLogger(GenerateRandomController.class);

    @Autowired
    private UtilService utilService;
    @Autowired
    private GenerateRandomService generateRandomService;

    @RequiresPermissions(value = {"/**","GenerateRandom"} ,logical = Logical.OR)
    @ApiOperation(value = "获取随机数",notes = "生成一个8~128字节随机数")
    @PostMapping(value = "/GenerateRandom")
    @ResponseBody
    public Result unicomGenerateRandom(@RequestBody GenerateRandomRequest generateRandomRequest) throws Exception {
        Integer passwordLength = generateRandomRequest.getRandomLength();
        if (passwordLength == null || passwordLength.equals("")) {
            passwordLength = 32;
        }
        if (passwordLength >= 8 || passwordLength <= 128) {
                byte[] RandomPassword = utilService.generateQuantumRandom(passwordLength);
                return ResultHelper.genResultWithSuccess(Base64.getEncoder().encodeToString(RandomPassword));
        } else {
                throw new QuantumException(ResultHelper.genResult(1, "口令字节数长度应为8~128"));
        }
    }

    /**
     * 产生临时密钥
     * @param random
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","GenerateTempKey"} ,logical = Logical.OR)
    @ApiOperation(value = "获取临时密钥",notes = "产生一个临时密钥并返回")
    @PostMapping(value = "/GenerateTempKey")
    @ResponseBody
    public Result unicomGenerateTempKey(@RequestBody GenerateTempKeyRequest generateTempKeyRequest) throws Exception {
        TempKeyDTO dto = generateRandomService.generateTempKey(generateTempKeyRequest);
        return ResultHelper.genResultWithSuccess(dto);
    }
}





