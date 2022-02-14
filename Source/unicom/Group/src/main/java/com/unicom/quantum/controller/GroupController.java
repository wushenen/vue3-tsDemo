package com.unicom.quantum.controller;

import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.controller.vo.AddGroupRequest;
import com.unicom.quantum.controller.vo.UpdateGroupInfoRequest;
import com.unicom.quantum.pojo.Group;
import com.unicom.quantum.service.GroupService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "分组管理接口")
@RestController
@RequestMapping(value = "/group")
public class GroupController {

    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private GroupService groupService;

    @RequiresRoles("systemUser")
    @ApiOperation(value = "创建分组",notes = "创建新分组")
    @PostMapping(value = "/addGroup")
    @ResponseBody
    public Result unicomAddGroup(@RequestBody AddGroupRequest addGroupRequest) throws QuantumException {
        groupService.addGroup(addGroupRequest.getGroupName(),addGroupRequest.getGroupCode(),addGroupRequest.getGroupDescribe());
        return ResultHelper.genResultWithSuccess();
    }



    @ApiOperation(value = "查看分组信息",notes = "查看分组信息")
    @GetMapping(value = "/groupList/{offset}/{pageSize}")
    @ResponseBody
    public Result unicomGroupList(HttpServletRequest request,
                            @PathVariable("offset") int offset,
                            @PathVariable("pageSize") int pageSize) throws Exception{
        request.setCharacterEncoding("UTF-8");
        if(pageSize > 50){
            logger.info("--------pageSize太大---------pageSize："+pageSize);
            return ResultHelper.genResult(1,"pageSize过大");
        }
        PageHelper.startPage(offset,pageSize);
        List<Group> groups = groupService.groupList();
        PageInfo<Group> groupPageInfo = new PageInfo<>(groups);
        return ResultHelper.genResultWithSuccess(groupPageInfo);
    }

    @RequiresRoles("systemUser")
    @ApiOperation(value = "修改分组信息",notes = "修改分组信息")
    @PostMapping(value = "/updateGroupInfo")
    @ResponseBody
    public Result unicomUpdateGroupInfo(@RequestBody UpdateGroupInfoRequest updateGroupInfoRequest) throws QuantumException {
        groupService.updateGroupInfo(updateGroupInfoRequest.getGroupId(),updateGroupInfoRequest.getGroupName(),updateGroupInfoRequest.getGroupDescribe());
        return ResultHelper.genResultWithSuccess();
    }


    @RequiresRoles("systemUser")
    @ApiOperation(value = "删除分组",notes = "删除分组")
    @GetMapping(value = "/deleteGroup")
    @ResponseBody
    public Result unicomDeleteGroup(@RequestParam("groupId") int groupId) {
        groupService.deleteGroup(groupId);
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "模糊查询分组信息",notes = "模糊查询分组信息")
    @GetMapping(value = "/queryGroupInfo")
    @ResponseBody
    public Result unicomQueryGroupInfo(@RequestParam("groupName") String groupName) {
        List<Group> groups = groupService.queryGroupInfo(groupName);
        return ResultHelper.genResultWithSuccess(groups);
    }

}
