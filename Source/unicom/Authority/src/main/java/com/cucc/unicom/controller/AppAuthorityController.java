package com.cucc.unicom.controller;

import com.cucc.unicom.component.Result;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.pojo.DTO.AuthInfo;
import com.cucc.unicom.service.AppAuthorityService;
import com.cucc.unicom.controller.vo.AddAppAuthorityRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "应用用户权限管理接口", tags = {"应用用户权限管理接口"})
@RequestMapping("/app")
@RestController
public class AppAuthorityController {

    @Autowired
    private AppAuthorityService appAuthorityService;

    @RequiresRoles("systemUser")
    @ApiOperation(value = "添加应用用户权限", notes = "添加应用用户权限")
    @RequestMapping(value = "/addAppAuthority",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomAddAppAuthority(@RequestBody AddAppAuthorityRequest addAppAuthorityRequest){
        appAuthorityService.addAppAuthority(addAppAuthorityRequest.getAppUserId(),addAppAuthorityRequest.getApiId());
        return ResultHelper.genResultWithSuccess();
    }

    @RequiresRoles("systemUser")
    @ApiOperation(value = "删除权限", notes = "删除权限信息")
    @RequestMapping(value = "/delAppAuthority",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomDelAppAuthority(@RequestParam("authId") int authId){
        int i = appAuthorityService.delAppAuthority(authId);
        if (i == 1)
            return ResultHelper.genResultWithSuccess();
        else
            return ResultHelper.genResult(1,"删除权限失败");
    }

    @RequiresRoles("systemUser")
    @ApiOperation(value = "删除所有权限", notes = "删除指定应用用户所有权限")
    @RequestMapping(value = "/delAppAllAuthority",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomDelAppAllAuthority(@RequestParam("appUserId") int appUserId){
        appAuthorityService.delAppAuthByAppUserId(appUserId);
        return ResultHelper.genResultWithSuccess();
    }

    @RequiresRoles("systemUser")
    @ApiOperation(value = "获取指定应用用户已添加权限信息", notes = "获取指定应用用户已添加权限信息")
    @RequestMapping(value = "/getAppAuthority",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomGetAppAuthority(@RequestParam("appUserId") int appUserId){
        List<AuthInfo> deviceAuthInfos = appAuthorityService.getAppAuthority(appUserId);
        return ResultHelper.genResultWithSuccess(deviceAuthInfos);
    }

}
