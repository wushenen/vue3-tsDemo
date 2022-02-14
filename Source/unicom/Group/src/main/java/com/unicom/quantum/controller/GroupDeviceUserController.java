package com.unicom.quantum.controller;

import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.controller.vo.CancelGroupDeviceUserRequest;
import com.unicom.quantum.pojo.DTO.GroupDeviceUserInfo;
import com.unicom.quantum.service.GroupDeviceUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.unicom.quantum.controller.vo.AddGroupDeviceUserRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Api(tags = "分组终端用户管理接口")
@RequestMapping(value = "/groupDeviceUser")
@RestController
public class GroupDeviceUserController {

    @Autowired
    private GroupDeviceUserService groupDeviceUserService;

    @RequiresRoles("systemUser")
    @ApiOperation(value = "添加分组终端用户",notes = "添加分组终端用户")
    @PostMapping(value = "/add")
    @ResponseBody
    public Result unicomAddGroupDeviceUser(@RequestBody AddGroupDeviceUserRequest addGroupDeviceUserRequest){
        groupDeviceUserService.addGroupDeviceUser(addGroupDeviceUserRequest.getDeviceId(),addGroupDeviceUserRequest.getGroupId());
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "获取该分组所有终端用户",notes = "获取该分组所有终端用户")
    @GetMapping(value = "/list/{offset}/{pageSize}")
    @ResponseBody
    public Result unicomGroupDeviceUserList(HttpServletRequest request,
                                @RequestParam("groupId") int groupId,
                                @PathVariable("offset") int offset,
                                @PathVariable("pageSize") int pageSize) throws Exception{
        request.setCharacterEncoding("UTF-8");
        if(pageSize > 50){
            return ResultHelper.genResult(1,"pageSize过大");
        }
        PageHelper.startPage(offset,pageSize);
        List<GroupDeviceUserInfo> groupDeviceUserList = groupDeviceUserService.groupDeviceUserList(groupId);
        PageInfo<GroupDeviceUserInfo> pageInfo = new PageInfo<>(groupDeviceUserList);
        return ResultHelper.genResultWithSuccess(pageInfo);
    }

    @RequiresRoles("systemUser")
    @ApiOperation(value = "删除分组终端用户",notes = "删除分组终端用户")
    @PostMapping(value = "/delete")
    @ResponseBody
    public Result unicomDeleteGroupDeviceUser(@RequestBody CancelGroupDeviceUserRequest cancelGroupDeviceUserRequest){
        groupDeviceUserService.deleteGroupDeviceUser(cancelGroupDeviceUserRequest.getIds());
        return ResultHelper.genResultWithSuccess();
    }

}
