package com.qtec.unicom.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qtec.unicom.component.Result;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.controller.vo.AddRoleDeviceUserRequest;
import com.qtec.unicom.controller.vo.CancelRoleUserRequest;
import com.qtec.unicom.pojo.DTO.RoleDeviceUserInfo;
import com.qtec.unicom.service.RoleDeviceUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by jerry on 2020/11/26.
 */
@RestController
@RequestMapping(value = "/roleDeviceUser")
@Api(value = "角色终端用户管理接口",tags = {"角色终端用户管理接口"})
public class RoleDeviceUserController {

    private static final Logger logger = LoggerFactory.getLogger(RoleDeviceUserController.class);

    @Autowired
    private RoleDeviceUserService roleDeviceUserService;

    @ApiOperation(value = "角色赋予终端用户",notes = "角色赋予终端用户")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public Result addDeviceRoleUser(@RequestBody AddRoleDeviceUserRequest addRoleDeviceUserRequest){
        roleDeviceUserService.addRoleDeviceUser(addRoleDeviceUserRequest.getDeviceId(),addRoleDeviceUserRequest.getRoleId());
        return ResultHelper.genResultWithSuccess();
    }


    @ApiOperation(value = "删除角色已赋予终端用户",notes = "删除角色已赋予终端用户")
    @PostMapping(value = "/delete")
    @ResponseBody
    public Result deleteDeviceRoleUser(@RequestBody CancelRoleUserRequest cancelRoleUserRequest){
        roleDeviceUserService.deleteRoleDeviceUser(cancelRoleUserRequest.getIds());
        return ResultHelper.genResultWithSuccess();
    }


    @ApiOperation(value = "获取角色已授权终端用户",notes = "获取角色已授权终端用户")
    @RequestMapping(value = "/list/{offset}/{pageSize}",method = RequestMethod.GET)
    @ResponseBody
    public Result roleDeviceUserList(@RequestParam("roleId") int roleId, HttpServletRequest request,
                               @PathVariable("offset") int offset,
                               @PathVariable("pageSize") int pageSize)throws Exception{
        request.setCharacterEncoding("UTF-8");
        if (pageSize > 50) {
            logger.info("--------pageSize太大---------pageSize："+pageSize);
            return ResultHelper.genResult(1, "pageSize过大");
        }
        PageHelper.startPage(offset,pageSize);
        logger.info("--------获取角色已授权用户---------");
        List<RoleDeviceUserInfo> roleDeviceUserInfos = roleDeviceUserService.roleDeviceUserList(roleId);
        PageInfo<RoleDeviceUserInfo> pageInfo = new PageInfo<>(roleDeviceUserInfos);
        return ResultHelper.genResultWithSuccess(pageInfo);
    }

}
