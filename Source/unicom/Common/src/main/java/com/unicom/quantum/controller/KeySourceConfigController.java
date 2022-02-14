package com.unicom.quantum.controller;

import com.alibaba.fastjson.JSONObject;
import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.controller.vo.KeySourceConfigRequest;
import com.unicom.quantum.controller.vo.UpdateQKDRequest;
import com.unicom.quantum.service.KeySourceConfigService;
import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.pojo.dto.KeySourceDetail;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"量子密钥源配置接口"})
@RequestMapping("/sourceConfig")
@RestController
public class KeySourceConfigController {

    @Autowired
    private KeySourceConfigService keySourceConfigService;

    @RequiresRoles("systemUser")
    @ApiOperation("获取密钥源配置")
    @GetMapping(value = "/get")
    @ResponseBody
    public Result unicomGet(){
        List<KeySourceDetail> keySourceConfig = keySourceConfigService.getKeySourceConfig();
        return ResultHelper.genResultWithSuccess(keySourceConfig);
    }

    @ApiOperation("获取QKD配置")
    @GetMapping(value = "/getQKD")
    @ResponseBody
    public Result unicomGetQKD(){
        String qkdConfig = keySourceConfigService.getQKDConfig();
        JSONObject configs = JSONObject.parseObject(qkdConfig);
        return ResultHelper.genResultWithSuccess(configs);
    }

    @RequiresRoles("systemUser")
    @ApiOperation("修改密钥源配置")
    @PostMapping(value = "/update")
    @ResponseBody
    public Result unicomUpdate(@RequestBody KeySourceConfigRequest keySourceConfigRequest) throws QuantumException {
        keySourceConfigService.updateKeySourceConfig(keySourceConfigRequest);
        return ResultHelper.genResultWithSuccess();
    }


    @RequiresRoles("systemUser")
    @ApiOperation("修改QKD配置")
    @PostMapping(value = "/updateQKD")
    @ResponseBody
    public Result unicomUpdateQKD(@RequestBody UpdateQKDRequest updateQKDRequest){
        String configInfo = JSONObject.toJSONString(updateQKDRequest);
        keySourceConfigService.updateQKDConfig(configInfo);
        return ResultHelper.genResultWithSuccess();
    }

    @RequiresRoles("systemUser")
    @ApiOperation("修改密钥源优先级")
    @RequestMapping(value = "/updatePriority")
    @ResponseBody
    public Result unicomUpdatePriority(@RequestParam("oldPriority") int oldPriority,@RequestParam("newPriority") int newPriority,@RequestParam("id") int id){
        keySourceConfigService.updateKeySourcePriority(oldPriority,newPriority,id);
        return ResultHelper.genResultWithSuccess();
    }

    @RequiresRoles("systemUser")
    @ApiOperation("禁用密钥源配置")
    @GetMapping(value = "/delete")
    @ResponseBody
    public Result unicomDelete(@RequestParam("id") int id,@RequestParam("priority") int priority) throws QuantumException {
        keySourceConfigService.disableKeySourceConfig(priority, id);
        return ResultHelper.genResultWithSuccess();
    }

    @RequiresRoles("systemUser")
    @ApiOperation("启用密钥源配置")
    @GetMapping(value = "/add")
    @ResponseBody
    public Result unicomAdd(@RequestParam("id") int id,@RequestParam("priority") int priority) throws QuantumException {
        keySourceConfigService.enableKeySourceConfig(priority, id);
        return ResultHelper.genResultWithSuccess();
    }
}
