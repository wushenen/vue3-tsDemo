/*
package com.qtec.mixedquantum.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qtec.mixedquantum.component.Result;
import com.qtec.mixedquantum.component.ResultHelper;
import com.qtec.mixedquantum.controller.vo.AddGroupUserRequest;
import com.qtec.mixedquantum.controller.vo.DeleteGroupUserRequest;
import com.qtec.mixedquantum.pojo.DTO.GroupUserDTO;
import com.qtec.mixedquantum.service.GroupUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "分组用户管理接口",tags = {"分组用户管理接口"})
@RequestMapping(value = "/groupUser")
@RestController
public class GroupUserController {

    private static final Logger logger = LoggerFactory.getLogger(GroupUserController.class);

    @Autowired
    private GroupUserService groupUserService;

    @ApiOperation(value = "添加分组用户")
    @RequestMapping(value = "/addGroupUser",method = RequestMethod.POST)
    @ResponseBody
    public Result addGroupUser(@RequestBody AddGroupUserRequest addGroupUserRequest){
        int i = 0;
        for (int deviceId : addGroupUserRequest.getDeviceId()) {
            if (1 == groupUserService.groupUserExist(deviceId,addGroupUserRequest.getGroupId()))
                i++;
        }
        if(addGroupUserRequest.getDeviceId().size() == i){
            logger.info("--------重复添加分组用户---------");
            return ResultHelper.genResult(1,"请勿重复添加用户");
        }else{
            for (int deviceId : addGroupUserRequest.getDeviceId()) {
                groupUserService.addGroupUser(deviceId,addGroupUserRequest.getGroupId());
            }
            logger.info("--------添加分组用户成功---------");
            return ResultHelper.genResultWithSuccess();
        }
    }

    @ApiOperation(value = "获取所有分组用户")
    @RequestMapping(value = "/groupUserList/{offset}/{pageSize}",method = RequestMethod.GET)
    @ResponseBody
    public Result groupUserList(HttpServletRequest request,
                                @RequestParam("groupId") int groupId,
                                @PathVariable("offset") int offset,
                                @PathVariable("pageSize") int pageSize) throws Exception{
        request.setCharacterEncoding("UTF-8");
        if(pageSize > 50){
            logger.info("--------pageSize太大---------pageSize："+pageSize);
            return ResultHelper.genResult(1,"pageSize过大");
        }
        PageHelper.startPage(offset,pageSize);
        List<GroupUserDTO> groupUserList = groupUserService.groupUserList(groupId);
        PageInfo<GroupUserDTO> pageInfo = new PageInfo<>(groupUserList);
        return ResultHelper.genResultWithSuccess(pageInfo);
    }

    @ApiOperation(value = "删除分组用户（可以批量删除）")
    @RequestMapping(value = "/deleteGroupUser",method = RequestMethod.POST)
    @ResponseBody
    public Result deleteGroupUser(@RequestBody DeleteGroupUserRequest deleteGroupUserRequest){
        int i = 0;
        for (int deviceId : deleteGroupUserRequest.getDeviceId()) {
            groupUserService.deleteGroupUser(deviceId,deleteGroupUserRequest.getGroupId());
            i++;
        }
        if(i == deleteGroupUserRequest.getDeviceId().size()){
            logger.info("--------删除分组用户成功---------");
            return ResultHelper.genResultWithSuccess();
        }else{
            logger.info("--------删除分组用户失败---------");
            return ResultHelper.genResult(1,"删除失败");
        }

    }

}
*/
