package com.qtec.unicom.controller;

import com.qtec.unicom.component.Result;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.controller.vo.KeySourceConfigRequest;
import com.qtec.unicom.pojo.KeySourceConfig;
import com.qtec.unicom.service.KeySourceConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"量子密钥源配置接口"})
@RequestMapping("/sourceConfig")
@RestController
public class KeySourceConfigController {

    @Autowired
    private KeySourceConfigService keySourceConfigService;

    @ApiOperation("获取密钥源配置")
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    @ResponseBody
    public Result get(){
        List<KeySourceConfig> keySourceConfig = keySourceConfigService.getKeySourceConfig();
        return ResultHelper.genResultWithSuccess(keySourceConfig);
    }


    @ApiOperation("修改密钥源配置")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody KeySourceConfigRequest keySourceConfigRequest){
        int i = keySourceConfigService.updateKeySourceConfig(keySourceConfigRequest);
        if (i == 1)
            return ResultHelper.genResult(1,"主IP未设置时禁止设置备用IP");
        if (i == 2)
            return ResultHelper.genResult(1,"请启用该量子密钥后再设置IP");
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation("修改密钥源优先级")
    @RequestMapping(value = "/updatePriority",method = RequestMethod.GET)
    @ResponseBody
    public Result updatePriority(@RequestParam("oldPriority") int oldPriority,@RequestParam("newPriority") int newPriority,@RequestParam("id") int id){
        keySourceConfigService.updateKeySourcePriority(oldPriority,newPriority,id);
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation("禁用密钥源配置")
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    @ResponseBody
    public Result delete(@RequestParam("id") int id,@RequestParam("priority") int priority){
        int i = keySourceConfigService.disableKeySourceConfig(priority, id);
        if (i == 1)
            return ResultHelper.genResult(1,"禁止禁用所有量子密钥源");
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation("启用密钥源配置")
    @RequestMapping(value = "/add",method = RequestMethod.GET)
    @ResponseBody
    public Result add(@RequestParam("id") int id,@RequestParam("priority") int priority){
        int i = keySourceConfigService.enableKeySourceConfig(priority, id);
        if (i == 1)
            return ResultHelper.genResult(1,"密钥源已启用，无需重复重启");
        return ResultHelper.genResultWithSuccess();
    }
}
