package com.qtec.unicom.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qtec.unicom.component.Result;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.controller.vo.AddGroupRequest;
import com.qtec.unicom.controller.vo.UpdateGroupInfoRequest;
import com.qtec.unicom.pojo.Group;
import com.qtec.unicom.service.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by jerry on 2020/11/27.
 */
@RestController
@RequestMapping(value = "/group")
@Api(value = "分组管理接口",tags = {"分组管理接口"})
public class GroupController {

    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private GroupService groupService;

    @ApiOperation(value = "创建分组",notes = "创建新分组")
    @RequestMapping(value = "/addGroup",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomAddGroup(@RequestBody AddGroupRequest addGroupRequest){
        if(groupService.groupNameExist(addGroupRequest.getGroupName()) == 1){
            logger.info("--------分组名称重复---------");
            return ResultHelper.genResult(1,"分组名称已存在");
        }else if(groupService.groupCodeExist(addGroupRequest.getGroupCode()) == 1){
            logger.info("--------分组标识重复---------");
            return ResultHelper.genResult(1,"分组标识不唯一");
        }else if(1 == groupService.addGroup(addGroupRequest.getGroupName(),addGroupRequest.getGroupCode(),addGroupRequest.getGroupDescribe())){
            return ResultHelper.genResultWithSuccess();
        }else{
            logger.info("--------添加分组失败---------");
            return ResultHelper.genResult(1,"添加分组失败");
        }

    }



    @ApiOperation(value = "查看分组信息",notes = "查看分组信息")
    @RequestMapping(value = "/groupList/{offset}/{pageSize}",method = RequestMethod.GET)
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


    @ApiOperation(value = "修改分组信息",notes = "修改分组信息")
    @RequestMapping(value = "/updateGroupInfo",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomUpdateGroupInfo(@RequestBody UpdateGroupInfoRequest updateGroupInfoRequest){
        if(1 == groupService.groupNameExist(updateGroupInfoRequest.getGroupName())){
            if(groupService.getGroupInfo(updateGroupInfoRequest.getGroupId()).getGroupName().equals(updateGroupInfoRequest.getGroupName())){
                int res = groupService.updateGroupInfo(updateGroupInfoRequest.getGroupId(),updateGroupInfoRequest.getGroupName(),updateGroupInfoRequest.getGroupDescribe());
                if(res == 1)
                    return ResultHelper.genResultWithSuccess();
                else
                    logger.info("--------更新分组信息失败---------");
                    return ResultHelper.genResult(1,"更新分组信息失败");
            }else{
                logger.info("--------分组名称已存在---------");
                return ResultHelper.genResult(1,"分组名称已存在");
            }
        }else if(1 == groupService.updateGroupInfo(updateGroupInfoRequest.getGroupId(),updateGroupInfoRequest.getGroupName(),updateGroupInfoRequest.getGroupDescribe())){
            return ResultHelper.genResultWithSuccess();
        }else
        return ResultHelper.genResult(1,"更新分组信息失败");
    }



    @ApiOperation(value = "删除分组",notes = "删除分组")
    @RequestMapping(value = "/deleteGroup",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomDeleteGroup(@RequestParam("groupId") int groupId) {
        groupService.deleteGroup(groupId);
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "模糊查询分组信息",notes = "模糊查询分组信息")
    @RequestMapping(value = "/queryGroupInfo",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomQueryGroupInfo(@RequestParam("groupName") String groupName) {
        List<Group> groups = groupService.queryGroupInfo(groupName);
        return ResultHelper.genResultWithSuccess(groups);
    }

}
