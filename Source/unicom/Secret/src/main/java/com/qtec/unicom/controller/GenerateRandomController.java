package com.qtec.unicom.controller;


import com.alibaba.fastjson.JSONObject;
import com.qtec.unicom.component.Exception.PwspException;
import com.qtec.unicom.component.Result;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.component.util.UtilService;
import com.qtec.unicom.pojo.Random;
import com.qtec.unicom.pojo.dto.TempKeyDTO;
import com.qtec.unicom.service.GenerateRandomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @ApiOperation(value = "获取随机数",notes = "生成一个随机数")
    @RequestMapping(value = "/GenerateRandom",method = RequestMethod.POST)
    @ResponseBody
    public Result createRandomCharData(@RequestBody Random random) throws Exception {
        logger.info("生成一个随机口令字符串");
        Integer passwordLength = random.getPasswordLength();
        if (passwordLength == null || passwordLength.equals("")) {
            passwordLength = 32;
        }
        if (passwordLength >= 8 || passwordLength <= 128) {
                byte[] RandomPassword = utilService.generateQuantumRandom(passwordLength);
                JSONObject object = new JSONObject();
                object.put("randomPassword", Base64.getEncoder().encodeToString(RandomPassword));
                return ResultHelper.genResultWithSuccess(object);
        } else {
                throw new PwspException(ResultHelper.genResult(400, "InvalidParameter", "口令字节数长度应为8~128"));
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
    @RequestMapping(value = "/GenerateTempKey",method = RequestMethod.POST)
    @ResponseBody
    public Result generateTempKey(@RequestBody Random random) throws Exception {
        logger.info("产生临时密钥");
        TempKeyDTO dto = generateRandomService.generateTempKey(random);
        return ResultHelper.genResultWithSuccess(dto);
    }
}





