package com.qtec.unicom.controller;

import com.qtec.unicom.component.Result;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.controller.vo.AddAppAuthorityRequest;
import com.qtec.unicom.pojo.DTO.AuthInfo;
import com.qtec.unicom.service.AppAuthorityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "应用用户权限管理接口", tags = {"应用用户权限管理接口"})
@RequestMapping("/app")
@RestController
public class AppAuthorityController {

    @Autowired
    private AppAuthorityService appAuthorityService;

    @ApiOperation(value = "添加应用用户权限", notes = "添加应用用户权限")
    @RequestMapping(value = "/addAppAuthority",method = RequestMethod.POST)
    @ResponseBody
    public Result addAppAuthority(@RequestBody AddAppAuthorityRequest addAppAuthorityRequest){
        appAuthorityService.addAppAuthority(addAppAuthorityRequest.getAppUserId(),addAppAuthorityRequest.getApiId());
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "删除权限", notes = "删除权限信息")
    @RequestMapping(value = "/delAppAuthority",method = RequestMethod.GET)
    @ResponseBody
    public Result delAppAuthority(@RequestParam("authId") int authId){
        int i = appAuthorityService.delAppAuthority(authId);
        if (i == 1)
            return ResultHelper.genResultWithSuccess();
        else
            return ResultHelper.genResult(1,"删除权限失败");
    }

    @ApiOperation(value = "删除所有权限", notes = "删除指定应用用户所有权限")
    @RequestMapping(value = "/delAppAllAuthority",method = RequestMethod.GET)
    @ResponseBody
    public Result delAppAllAuthority(@RequestParam("appUserId") int appUserId){
        appAuthorityService.delAppAuthByAppUserId(appUserId);
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "获取指定应用用户已添加权限信息", notes = "获取指定应用用户已添加权限信息")
    @RequestMapping(value = "/getAppAuthority",method = RequestMethod.GET)
    @ResponseBody
    public Result getAppAuthority(@RequestParam("appUserId") int appUserId){
        List<AuthInfo> deviceAuthInfos = appAuthorityService.getAppAuthority(appUserId);
        return ResultHelper.genResultWithSuccess(deviceAuthInfos);
    }

}
