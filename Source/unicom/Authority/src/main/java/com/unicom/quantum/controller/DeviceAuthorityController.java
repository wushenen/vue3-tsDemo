package com.unicom.quantum.controller;

import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.pojo.DTO.AuthInfo;
import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.service.DeviceAuthorityService;
import com.unicom.quantum.controller.vo.AddDeviceAuthorityRequest;
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
    @RequestMapping(value = "/addDeviceAuthority",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomAddDeviceAuthority(@RequestBody AddDeviceAuthorityRequest addDeviceAuthorityRequest){
        //先把全部的权限删除之后再添加
        deviceAuthorityService.delDeviceAuthByDeviceId(addDeviceAuthorityRequest.getDeviceId());
        for (Integer apiId : addDeviceAuthorityRequest.getApiId()) {
            deviceAuthorityService.addDeviceAuthority(addDeviceAuthorityRequest.getDeviceId(), apiId);
        }
        return ResultHelper.genResultWithSuccess();
    }

    @RequiresRoles("systemUser")
    @ApiOperation(value = "删除权限", notes = "删除权限")
    @RequestMapping(value = "/delDeviceAuthority",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomDelDeviceAuthority(@RequestParam("authId") int authId) throws QuantumException {
        deviceAuthorityService.delDeviceAuthority(authId);
        return ResultHelper.genResultWithSuccess();
    }

    @RequiresRoles("systemUser")
    @ApiOperation(value = "删除指定终端用户所有权限", notes = "删除指定终端用户所有权限")
    @RequestMapping(value = "/delDeviceAllAuthority",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomDelDeviceAllAuthority(@RequestParam("deviceId") int deviceId){
        deviceAuthorityService.delDeviceAuthByDeviceId(deviceId);
        return ResultHelper.genResultWithSuccess();
    }

    @RequiresRoles("systemUser")
    @ApiOperation(value = "获取指定终端用户已添加权限信息", notes = "获取指定终端用户已添加权限信息")
    @RequestMapping(value = "/getDeviceAuthority",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomGetDeviceAuthority(@RequestParam("deviceId") int deviceId){
        List<AuthInfo> authInfos = deviceAuthorityService.getDeviceAuthority(deviceId);
        return ResultHelper.genResultWithSuccess(authInfos);
    }

}
