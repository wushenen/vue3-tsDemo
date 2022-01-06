package com.cucc.unicom.controller;

import com.cucc.unicom.component.Result;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.controller.vo.CancelGroupUserRequest;
import com.cucc.unicom.pojo.DTO.GroupDeviceUserInfo;
import com.cucc.unicom.service.GroupDeviceUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cucc.unicom.controller.vo.AddGroupDeviceUserRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Api(tags = {"分组终端用户管理接口"})
@RequestMapping(value = "/groupDeviceUser")
@RestController
public class GroupDeviceUserController {

    @Autowired
    private GroupDeviceUserService groupDeviceUserService;

    @RequiresRoles("systemUser")
    @ApiOperation(value = "添加分组终端用户",notes = "添加分组终端用户")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomAddGroupDeviceUser(@RequestBody AddGroupDeviceUserRequest addGroupDeviceUserRequest){
        groupDeviceUserService.addGroupDeviceUser(addGroupDeviceUserRequest.getDeviceId(),addGroupDeviceUserRequest.getGroupId());
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "获取该分组所有终端用户",notes = "获取该分组所有终端用户")
    @RequestMapping(value = "/list/{offset}/{pageSize}",method = RequestMethod.GET)
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
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomDeleteGroupDeviceUser(@RequestBody CancelGroupUserRequest cancelGroupUserRequest){
        groupDeviceUserService.deleteGroupDeviceUser(cancelGroupUserRequest.getIds());
        return ResultHelper.genResultWithSuccess();
    }

}
