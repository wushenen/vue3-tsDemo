package com.cucc.unicom.controller;

import com.alibaba.fastjson.JSONObject;
import com.cucc.unicom.component.Result;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.component.util.JWTUtil;
import com.cucc.unicom.controller.vo.AddAppConfigRequest;
import com.cucc.unicom.controller.vo.QemsOperateRequest;
import com.cucc.unicom.controller.vo.UpdateAppConfigRequest;
import com.cucc.unicom.pojo.AppConfig;
import com.cucc.unicom.service.AppConfigService;
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

    @ApiOperation(value = "添加应用配置",notes = "修改qems配置")
    @RequestMapping(value = "/addConfig",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomAddConfig(@RequestBody AddAppConfigRequest addAppConfigRequest){
        if (addAppConfigRequest.getEndIndex() - addAppConfigRequest.getStartIndex() > 2000)
            return ResultHelper.genResult(1,"充注密钥数不得大于2000");
        appConfigService.addAppConfig(addAppConfigRequest);
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "修改应用配置",notes = "修改应用配置")
    @RequiresPermissions(value = {"/updateConfig","/**"},logical = Logical.OR)
    @RequestMapping(value = "/updateConfig",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomUpdateConfig(@RequestBody UpdateAppConfigRequest updateAppConfigRequest){
        if (updateAppConfigRequest.getEndIndex() - updateAppConfigRequest.getStartIndex() > 2000)
            return ResultHelper.genResult(1,"充注密钥数不得大于2000");
        appConfigService.updateAppConfig(updateAppConfigRequest);
        return ResultHelper.genResultWithSuccess();
    }


    @RequiresPermissions(value = {"/getConfig","/**"},logical = Logical.OR)
    @ApiOperation(value = "获取应用配置信息",notes = "管理员获取应用配置信息")
    @RequestMapping(value = "/getConfig",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomGetConfig(@RequestParam("appId") int appId){
        AppConfig configInfo = appConfigService.getAppConfigByAppId(appId);
        return ResultHelper.genResultWithSuccess(configInfo);
    }

    @RequiresRoles(value = {"admin","deviceUser"},logical = Logical.OR)
    @ApiOperation(value = "SDK获取配置信息",notes = "SDK获取qems配置信息")
    @RequestMapping(value = "/getConfig2",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomGetConfig2(HttpServletRequest request,HttpServletResponse response) {
        JSONObject object = new JSONObject();
        if (response.getHeader("Token") != null) {
            object.put("Token",response.getHeader("Token"));
        }
        String token = request.getHeader("Token");
        String userId = JWTUtil.getUserId(token);
        AppConfig configInfo = appConfigService.getAppConfig(Integer.valueOf(userId));
        object.put("qemsConfig",configInfo);
        return ResultHelper.genResultWithSuccess(object);
    }


    @ApiOperation(value = "设备控制置零或重启",notes ="设备控制置零或重启")
    @RequestMapping(value = "/operation",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomOperation(@RequestBody QemsOperateRequest qemsOperateRequest) {
        int res = appConfigService.addQemsOperation(qemsOperateRequest.getDeviceName(), qemsOperateRequest.getOperation());
        if (res == 1)
            return ResultHelper.genResult(1,"命令已下发，请耐心等待");
        return ResultHelper.genResultWithSuccess();
    }

}