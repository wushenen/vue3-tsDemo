package com.qtec.unicom.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qtec.unicom.component.Result;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.controller.vo.AddRoleRequest;
import com.qtec.unicom.controller.vo.UpdateRoleInfoRequest;
import com.qtec.unicom.pojo.Role;
import com.qtec.unicom.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by jerry on 2020/11/25.
 */
@RestController
@RequestMapping(value = "/role")
@Api(value = "角色管理接口", tags = {"角色管理接口"})
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "新建角色",notes = "新建角色")
    @RequestMapping(value = "/addRole",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomAddRole(@RequestBody AddRoleRequest addRoleRequest) {
        //判断角色code是否唯一
        int unique = roleService.uniqueRole(addRoleRequest.getRoleCode());
        if (unique == 1) {
            logger.info("--------角色code不唯一---------");
            return ResultHelper.genResult(1, "角色名称已存在");
        } else{
            int res = roleService.addRole(addRoleRequest.getRoleCode(), addRoleRequest.getRoleDescribe());
            if (res == 1){
                logger.info("--------新建角色成功---------");
                return ResultHelper.genResultWithSuccess();
            }else
                logger.info("--------角色添加失败---------");
                return ResultHelper.genResult(1, "角色添加失败");
        }
    }

    @ApiOperation(value = "获取所有角色信息列表",notes = "获取所有角色信息列表")
    @RequestMapping(value = "/roleList/{offset}/{pageSize}",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomRoleList(HttpServletRequest request,
                           @PathVariable("offset") int offset,
                           @PathVariable("pageSize") int pageSize)throws Exception{
        request.setCharacterEncoding("UTF-8");
        if (pageSize > 50) {
            logger.info("--------pageSize太大---------pageSize："+pageSize);
            return ResultHelper.genResult(1, "pageSize过大");
        }
        PageHelper.startPage(offset,pageSize);
        List<Role> roleList = roleService.roleList();
        PageInfo<Role> pageInfo = new PageInfo<>(roleList);
        return ResultHelper.genResultWithSuccess(pageInfo);
    }

    @ApiOperation(value = "获取角色详细信息",notes = "获取角色详细信息")
    @RequestMapping(value = "/roleInfo",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomRoleInfo(@RequestParam("roleCode") String roleCode){
        Role roleInfo = roleService.roleInfo(roleCode);
        if(roleInfo != null){
            logger.info("--------获取角色信息成功---------");
            return  ResultHelper.genResultWithSuccess(roleInfo);
        }else{
            logger.info("--------获取角色信息失败---------");
            return ResultHelper.genResult(1,"获取角色信息失败");
        }
    }

    @ApiOperation(value = "删除角色信息",notes = "删除角色信息")
    @RequestMapping(value = "/deleteRole",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomDeleteRole(@RequestParam("roleId") int roleId){
        //删除角色信息需要同时删除该角色信息下的所有用户
        roleService.delRole(roleId);
        logger.info("--------成功删除角色信息以及该角色下的用户信息---------");
        return ResultHelper.genResultWithSuccess();
    }


    @ApiOperation(value = "修改角色信息",notes = "修改角色信息")
    @RequestMapping(value = "/updateRoleInfo",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomUpdateRoleInfo(@RequestBody UpdateRoleInfoRequest updateRoleInfoRequest){

        if (1 == roleService.uniqueRole(updateRoleInfoRequest.getRoleCode())){
            if (updateRoleInfoRequest.getRoleCode().equals(roleService.roleInfoById(updateRoleInfoRequest.getRoleId()).getRoleCode())) {
                int res = roleService.updateRoleInfo(updateRoleInfoRequest.getRoleId(), updateRoleInfoRequest.getRoleCode(), updateRoleInfoRequest.getRoleDescribe());
                if (res == 1) {
                    logger.info("--------成功修改角色信息---------");
                    return ResultHelper.genResultWithSuccess();
                } else {
                    logger.info("--------修改角色信息失败---------");
                    return ResultHelper.genResult(1, "修改角色信息失败");
                }
            }else {
                logger.info("--------角色code已存在---------");
                return ResultHelper.genResult(1, "角色名称已存在");
            }
        }else if (1 == roleService.updateRoleInfo(updateRoleInfoRequest.getRoleId(), updateRoleInfoRequest.getRoleCode(), updateRoleInfoRequest.getRoleDescribe())){
            return ResultHelper.genResultWithSuccess();
        }else
            return ResultHelper.genResult(1, "修改角色信息失败");
    }


    @ApiOperation(value = "模糊查询角色信息",notes = "模糊查询角色信息")
    @RequestMapping(value = "/searchRole",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomSearchRole(@RequestParam("roleCode") String roleCode){
        List<Role> roles = roleService.searchRole(roleCode);
        return  ResultHelper.genResultWithSuccess(roles);
    }


}
