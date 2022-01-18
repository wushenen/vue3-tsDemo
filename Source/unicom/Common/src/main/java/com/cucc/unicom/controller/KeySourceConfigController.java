package com.cucc.unicom.controller;

import com.alibaba.fastjson.JSONObject;
import com.cucc.unicom.component.Result;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.controller.vo.KeySourceConfigRequest;
import com.cucc.unicom.controller.vo.UpdateQKDRequest;
import com.cucc.unicom.pojo.KeySourceConfig;
import com.cucc.unicom.service.KeySourceConfigService;
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
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomGet(){
        List<KeySourceConfig> keySourceConfig = keySourceConfigService.getKeySourceConfig();
        return ResultHelper.genResultWithSuccess(keySourceConfig);
    }

    @ApiOperation("获取QKD配置")
    @RequestMapping(value = "/getQKD",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomGetQKD(){
        String qkdConfig = keySourceConfigService.getQKDConfig();
        JSONObject configs = JSONObject.parseObject(qkdConfig);
        return ResultHelper.genResultWithSuccess(configs);
    }

    @RequiresRoles("systemUser")
    @ApiOperation("修改密钥源配置")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomUpdate(@RequestBody KeySourceConfigRequest keySourceConfigRequest){
        int i = keySourceConfigService.updateKeySourceConfig(keySourceConfigRequest);
        if (i == 1)
            return ResultHelper.genResult(1,"主IP未设置时禁止设置备用IP");
        if (i == 2)
            return ResultHelper.genResult(1,"请启用该量子密钥后再设置IP");
        return ResultHelper.genResultWithSuccess();
    }


    @RequiresRoles("systemUser")
    @ApiOperation("修改QKD配置")
    @RequestMapping(value = "/updateQKD",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomUpdateQKD(@RequestBody UpdateQKDRequest updateQKDRequest){
        String configInfo = JSONObject.toJSONString(updateQKDRequest);
        keySourceConfigService.updateQKDConfig(configInfo);
        return ResultHelper.genResultWithSuccess();
    }

    @RequiresRoles("systemUser")
    @ApiOperation("修改密钥源优先级")
    @RequestMapping(value = "/updatePriority",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomUpdatePriority(@RequestParam("oldPriority") int oldPriority,@RequestParam("newPriority") int newPriority,@RequestParam("id") int id){
        keySourceConfigService.updateKeySourcePriority(oldPriority,newPriority,id);
        return ResultHelper.genResultWithSuccess();
    }

    @RequiresRoles("systemUser")
    @ApiOperation("禁用密钥源配置")
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomDelete(@RequestParam("id") int id,@RequestParam("priority") int priority){
        int i = keySourceConfigService.disableKeySourceConfig(priority, id);
        if (i == 1)
            return ResultHelper.genResult(1,"禁止禁用所有量子密钥源");
        return ResultHelper.genResultWithSuccess();
    }

    @RequiresRoles("systemUser")
    @ApiOperation("启用密钥源配置")
    @RequestMapping(value = "/add",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomAdd(@RequestParam("id") int id,@RequestParam("priority") int priority){
        int i = keySourceConfigService.enableKeySourceConfig(priority, id);
        if (i == 1)
            return ResultHelper.genResult(1,"密钥源已启用，无需重复启用");
        return ResultHelper.genResultWithSuccess();
    }
}
