package com.cucc.unicom.controller;

import com.cucc.unicom.component.Result;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.controller.vo.AddRoleAuthRequest;
import com.cucc.unicom.pojo.DTO.RoleAuthInfo;
import com.cucc.unicom.service.RoleAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "角色权限接口")
@RestController
@RequestMapping("/roleAuth")
public class RoleAuthController {

    @Autowired
    private RoleAuthService roleAuthService;

    @ApiOperation(value = "添加角色权限",notes = "添加角色权限")
    @ResponseBody
    @PostMapping("/add")
    public Result unicomAddRoleAuth(@RequestBody AddRoleAuthRequest addRoleAuthRequest){
        int res = roleAuthService.addRoleAuth(addRoleAuthRequest.getRoleId(), addRoleAuthRequest.getApiId());
        if (res != 0)
            return ResultHelper.genResult(1,"资源"+res+"权限已重复添加");
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "删除角色部分权限",notes = "删除角色部分权限")
    @ResponseBody
    @GetMapping("/deletePart")
    public Result unicomDeletePartRoleAuth(@RequestParam("roleAuthId") int roleAuthId){
        roleAuthService.deleteRoleAuthById(roleAuthId);
        return ResultHelper.genResultWithSuccess();
    }


    @ApiOperation(value = "删除该角色所有权限",notes = "删除该角色所有权限")
    @ResponseBody
    @GetMapping("/deleteAll")
    public Result unicomDeleteAllRoleAuth(@RequestParam("roleId") Integer roleId){
        roleAuthService.deleteRoleAuthByRoleId(roleId);
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "获取该角色下的所有权限信息",notes = "获取该角色下的所有权限信息")
    @ResponseBody
    @GetMapping("/get")
    public Result unicomGetRoleAuth(@RequestParam("roleId") Integer roleId){
        List<RoleAuthInfo> roleAuth = roleAuthService.getRoleAuth(roleId);
        return ResultHelper.genResultWithSuccess(roleAuth);
    }

}
