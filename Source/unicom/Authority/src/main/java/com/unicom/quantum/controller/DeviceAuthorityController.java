package com.unicom.quantum.controller;

import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.controller.vo.AddDeviceAuthorityRequest;
import com.unicom.quantum.pojo.DTO.AuthInfo;
import com.unicom.quantum.service.DeviceAuthorityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "终端用户权限管理接口", tags = {"终端用户权限管理接口"})
@RequestMapping("/device")
@RestController
public class DeviceAuthorityController {

    @Autowired
    private DeviceAuthorityService deviceAuthorityService;

    @RequiresRoles("systemUser")
    @ApiOperation(value = "添加终端用户权限", notes = "添加终端用户权限")
    @PostMapping(value = "/addDeviceAuthority")
    @ResponseBody
    public Result unicomAddDeviceAuthority(@RequestBody AddDeviceAuthorityRequest addDeviceAuthorityRequest){
        deviceAuthorityService.addDeviceAuthority(addDeviceAuthorityRequest.getDeviceId(), addDeviceAuthorityRequest.getApiId());
        return ResultHelper.genResultWithSuccess();
    }

    @RequiresRoles("systemUser")
    @ApiOperation(value = "删除权限", notes = "删除权限")
    @GetMapping(value = "/delDeviceAuthority")
    @ResponseBody
    public Result unicomDelDeviceAuthority(@RequestParam("authId") int authId) {
        deviceAuthorityService.delDeviceAuthority(authId);
        return ResultHelper.genResultWithSuccess();
    }

    @RequiresRoles("systemUser")
    @ApiOperation(value = "删除指定终端用户所有权限", notes = "删除指定终端用户所有权限")
    @GetMapping(value = "/delDeviceAllAuthority")
    @ResponseBody
    public Result unicomDelDeviceAllAuthority(@RequestParam("deviceId") int deviceId){
        deviceAuthorityService.delDeviceAuthByDeviceId(deviceId);
        return ResultHelper.genResultWithSuccess();
    }

    @RequiresRoles("systemUser")
    @ApiOperation(value = "获取指定终端用户已添加权限信息", notes = "获取指定终端用户已添加权限信息")
    @GetMapping(value = "/getDeviceAuthority")
    @ResponseBody
    public Result unicomGetDeviceAuthority(@RequestParam("deviceId") int deviceId){
        List<AuthInfo> authInfos = deviceAuthorityService.getDeviceAuthority(deviceId);
        return ResultHelper.genResultWithSuccess(authInfos);
    }

}
