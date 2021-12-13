package com.cucc.unicom.controller;

import com.alibaba.fastjson.JSONObject;
import com.cucc.unicom.component.Result;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.pojo.QemsConfig;
import com.cucc.unicom.service.QemsConfigService;
import com.cucc.unicom.controller.vo.QemsOperateRequest;
import com.cucc.unicom.controller.vo.UpdateQemsConfigRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@Api(value = "qems配置接口",tags = {"qems配置接口"})
@RequestMapping("/qems")
public class QemsConfigController {

    @Autowired
    private QemsConfigService qemsConfigService;

    @ApiOperation(value = "修改配置",notes = "修改qems配置")
    @RequiresPermissions(value = {"/updateConfig","/**"},logical = Logical.OR)
    @RequestMapping(value = "/updateConfig",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomUpdateConfig(@RequestBody UpdateQemsConfigRequest updateQemsConfigRequest){
        if (updateQemsConfigRequest.getEndIndex() - updateQemsConfigRequest.getStartIndex() > 2000)
            return ResultHelper.genResult(1,"充注密钥数不得大于2000");
        qemsConfigService.updateQemsConfig(updateQemsConfigRequest);
        return ResultHelper.genResultWithSuccess();
    }


    @RequiresPermissions(value = {"/getConfig","/**"},logical = Logical.OR)
    @ApiOperation(value = "获取配置信息",notes = "获取qems配置信息")
    @RequestMapping(value = "/getConfig",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomGetConfig(){
        QemsConfig configInfo = qemsConfigService.getQemsConfig();
        return ResultHelper.genResultWithSuccess(configInfo);
    }

    @RequiresRoles(value = {"admin","deviceUser"},logical = Logical.OR)
    @ApiOperation(value = "SDK获取配置信息",notes = "SDK获取qems配置信息")
    @RequestMapping(value = "/getConfig2",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomGetConfig2(HttpServletResponse response) {
        JSONObject object = new JSONObject();
        if (response.getHeader("Token") != null) {
            object.put("Token",response.getHeader("Token"));
        }
        //可以考虑先到内存中取配置信息
        QemsConfig configInfo = qemsConfigService.getQemsConfig();
        object.put("qemsConfig",configInfo);
        return ResultHelper.genResultWithSuccess(object);
    }


    @ApiOperation(value = "设备控制置零或重启",notes ="设备控制置零或重启")
    @RequestMapping(value = "/operation",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomOperation(@RequestBody QemsOperateRequest qemsOperateRequest) {
        int res = qemsConfigService.addQemsOperation(qemsOperateRequest.getDeviceName(), qemsOperateRequest.getOperation());
        if (res == 1)
            return ResultHelper.genResult(1,"命令已下发，请耐心等待");
        return ResultHelper.genResultWithSuccess();
    }

}