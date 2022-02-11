package com.unicom.quantum.controller;

import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.controller.vo.AddGroupAuthRequest;
import com.unicom.quantum.pojo.DTO.GroupAuthInfo;
import com.unicom.quantum.service.GroupAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "分组权限接口")
@RestController
@RequestMapping("/groupAuth")
public class GroupAuthController {

    @Autowired
    private GroupAuthService groupAuthService;

    @RequiresRoles("systemUser")
    @ApiOperation(value = "添加分组权限",notes = "添加分组权限")
    @ResponseBody
    @PostMapping("/add")
    public Result unicomAddGroupAuth(@RequestBody AddGroupAuthRequest addGroupAuthRequest){
        String res = groupAuthService.addGroupAuth(addGroupAuthRequest.getGroupId(), addGroupAuthRequest.getApiId());
        if (!"0".equals(res))
            return ResultHelper.genResult(1,res+"  权限重复添加");
        return ResultHelper.genResultWithSuccess();
    }

    @RequiresRoles("systemUser")
    @ApiOperation(value = "删除分组部分权限",notes = "删除分组部分权限")
    @ResponseBody
    @GetMapping("/deletePart")
    public Result unicomDeletePartGroupAuth(@RequestParam("groupAuthId") Integer groupAuthId){
        groupAuthService.deleteGroupAuthById(groupAuthId);
        return ResultHelper.genResultWithSuccess();
    }


    @RequiresRoles("systemUser")
    @ApiOperation(value = "删除该分组所有权限",notes = "删除该分组所有权限")
    @ResponseBody
    @GetMapping("/deleteAll")
    public Result unicomDeleteAllGroupAuth(@RequestParam("groupId") Integer groupId){
        groupAuthService.deleteGroupAuthByGroupId(groupId);
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "获取该分组下的所有权限信息",notes = "获取该分组下的所有权限信息")
    @ResponseBody
    @GetMapping("/get")
    public Result unicomGetGroupAuth(@RequestParam("groupId") Integer groupId){
        List<GroupAuthInfo> groupAuth = groupAuthService.getGroupAuth(groupId);
        return ResultHelper.genResultWithSuccess(groupAuth);
    }
}
