package com.cucc.unicom.controller;

import com.cucc.unicom.component.Result;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.pojo.DTO.RoleAppUserInfo;
import com.cucc.unicom.service.RoleAppUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cucc.unicom.controller.vo.AddRoleAppUserRequest;
import com.cucc.unicom.controller.vo.CancelRoleUserRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping(value = "/roleAppUser")
@Api(value = "角色应用用户管理接口",tags = {"角色应用用户管理接口"})
public class RoleAppUserController {

    private static final Logger logger = LoggerFactory.getLogger(RoleAppUserController.class);

    @Autowired
    private RoleAppUserService roleAppUserService;

    @ApiOperation(value = "角色赋予应用用户" ,notes = "角色赋予应用用户")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomAddAppRoleUser(@RequestBody AddRoleAppUserRequest addRoleAppUserRequest){
        roleAppUserService.addRoleAppUser(addRoleAppUserRequest.getAppUserId(),addRoleAppUserRequest.getRoleId());
        return ResultHelper.genResultWithSuccess();
    }


    @ApiOperation(value = "删除角色已赋予应用用户",notes = "删除角色已赋予应用用户")
    @PostMapping(value = "/delete")
    @ResponseBody
    public Result unicomDeleteAppRoleUser(@RequestBody CancelRoleUserRequest cancelRoleUserRequest){
        roleAppUserService.deleteRoleAppUser(cancelRoleUserRequest.getIds());
        return ResultHelper.genResultWithSuccess();
    }


    @ApiOperation(value = "获取角色已赋予应用用户信息",notes = "获取角色已赋予应用用户信息")
    @RequestMapping(value = "/list/{offset}/{pageSize}",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomRoleAppUserList(@RequestParam("roleId") int roleId, HttpServletRequest request,
                               @PathVariable("offset") int offset,
                               @PathVariable("pageSize") int pageSize)throws Exception{
        request.setCharacterEncoding("UTF-8");
        if (pageSize > 50) {
            logger.info("--------pageSize太大---------pageSize："+pageSize);
            return ResultHelper.genResult(1, "pageSize过大");
        }
        PageHelper.startPage(offset,pageSize);
        logger.info("--------获取角色已授权用户---------");
        List<RoleAppUserInfo> roleAppUserInfos = roleAppUserService.roleAppUserList(roleId);
        PageInfo<RoleAppUserInfo> pageInfo = new PageInfo<>(roleAppUserInfos);
        return ResultHelper.genResultWithSuccess(pageInfo);
    }

}
