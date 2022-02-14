package com.unicom.quantum.controller;

import com.alibaba.fastjson.JSONObject;
import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.controller.vo.QemsOperateRequest;
import com.unicom.quantum.controller.vo.UpdateAppConfigRequest;
import com.unicom.quantum.pojo.AppConfig;
import com.unicom.quantum.service.AppConfigService;
import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.component.util.JWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(value = "qems配置接口",tags = {"qems配置接口"})
@RequestMapping("/qems")
public class AppConfigController {

    @Autowired
    private AppConfigService appConfigService;

    @ApiOperation(value = "修改应用配置",notes = "修改应用配置")
    @RequiresPermissions(value = {"/updateConfig","/**"},logical = Logical.OR)
    @PostMapping(value = "/updateConfig")
    @ResponseBody
    public Result unicomUpdateConfig(@RequestBody UpdateAppConfigRequest updateAppConfigRequest){
        if (updateAppConfigRequest.getEndIndex() - updateAppConfigRequest.getStartIndex() > 2000)
            return ResultHelper.genResult(1,"充注密钥数不得大于2000");
        appConfigService.updateAppConfig(updateAppConfigRequest);
        return ResultHelper.genResultWithSuccess();
    }

    @RequiresPermissions(value = {"/getConfig","/**"},logical = Logical.OR)
    @ApiOperation(value = "获取应用配置信息",notes = "管理员获取应用配置信息")
    @GetMapping(value = "/getConfig")
    @ResponseBody
    public Result unicomGetConfig(@RequestParam("appId") int appId){
        AppConfig configInfo = appConfigService.getAppConfigByAppId(appId);
        return ResultHelper.genResultWithSuccess(configInfo);
    }

    @RequiresRoles(value = {"admin","deviceUser"},logical = Logical.OR)
    @ApiOperation(value = "SDK获取配置信息",notes = "SDK获取qems配置信息")
    @PostMapping(value = "/getConfig2")
    @ResponseBody
    public Result unicomGetConfig2(HttpServletRequest request,HttpServletResponse response) {
        JSONObject object = new JSONObject();
        if (response.getHeader("Token") != null) {
            object.put("Token",response.getHeader("Token"));
        }
        String token = request.getHeader("Token");
        int userId = JWTUtil.getUserId(token);
        AppConfig configInfo = appConfigService.getAppConfig(userId);
        object.put("qemsConfig",configInfo);
        return ResultHelper.genResultWithSuccess(object);
    }

    @ApiOperation(value = "设备控制置零或重启",notes ="设备控制置零或重启")
    @PostMapping(value = "/operation")
    @ResponseBody
    public Result unicomOperation(@RequestBody QemsOperateRequest qemsOperateRequest) throws QuantumException {
        appConfigService.addQemsOperation(qemsOperateRequest.getDeviceName(), qemsOperateRequest.getOperation());
        return ResultHelper.genResultWithSuccess();
    }

}