package com.qtec.unicom.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qtec.unicom.component.Result;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.controller.vo.AddGroupAppUserRequest;
import com.qtec.unicom.controller.vo.CancelGroupUserRequest;
import com.qtec.unicom.pojo.DTO.GroupAppUserInfo;
import com.qtec.unicom.service.GroupAppUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Api(tags = {"分组应用用户管理接口"})
@RequestMapping(value = "/groupAppUser")
@RestController
public class GroupAppUserController {

    @Autowired
    private GroupAppUserService groupAppUserService;

    @ApiOperation(value = "添加分组应用用户",notes = "添加分组应用用户")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public Result addGroupAppUser(@RequestBody AddGroupAppUserRequest addGroupAppUserRequest){
        groupAppUserService.addGroupAppUser(addGroupAppUserRequest.getAppUserId(),addGroupAppUserRequest.getGroupId());
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "获取所有分组应用用户",notes = "获取所有分组应用用户")
    @RequestMapping(value = "/list/{offset}/{pageSize}",method = RequestMethod.GET)
    @ResponseBody
    public Result groupAppUserList(HttpServletRequest request,
                                @RequestParam("groupId") int groupId,
                                @PathVariable("offset") int offset,
                                @PathVariable("pageSize") int pageSize) throws Exception{
        request.setCharacterEncoding("UTF-8");
        if(pageSize > 50){
            return ResultHelper.genResult(1,"pageSize过大");
        }
        PageHelper.startPage(offset,pageSize);
        List<GroupAppUserInfo> groupAppUserList = groupAppUserService.groupAppUserList(groupId);
        PageInfo<GroupAppUserInfo> pageInfo = new PageInfo<>(groupAppUserList);
        return ResultHelper.genResultWithSuccess(pageInfo);
    }

    @ApiOperation(value = "删除分组应用用户",notes = "删除分组应用用户")
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public Result deleteGroupAppUser(@RequestBody CancelGroupUserRequest cancelGroupUserRequest){
        groupAppUserService.deleteGroupAppUser(cancelGroupUserRequest.getIds());
        return ResultHelper.genResultWithSuccess();
    }

}
