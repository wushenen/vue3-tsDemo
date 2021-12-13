package com.cucc.unicom.controller;

import com.cucc.unicom.component.Result;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.pojo.DTO.StrategyAuthInfo;
import com.cucc.unicom.pojo.DeviceUser;
import com.cucc.unicom.pojo.Role;
import com.cucc.unicom.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cucc.unicom.controller.vo.AddStrategyAuthorityRequest;
import com.cucc.unicom.pojo.AppUser;
import com.cucc.unicom.pojo.Group;
import com.qtec.unicom.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "策略授权接口", tags = {"策略授权接口"})
@RestController
@RequestMapping(value = "/strategy")
public class StrategyAuthorityController {

    private final int DEVICE = 1;
    private final int ROLE = 2;
    private final int GROUP = 3;
    private final int APP = 4;

    @Autowired
    private StrategyAuthorityService strategyAuthorityService;
    @Autowired
    private DeviceUserService deviceUserService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private AppUserService appUserService;

    @ApiOperation(value = "添加策略授权信息", notes = "添加策略授权信息")
    @RequestMapping(value = "/addStrategyAuth",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomAddStrategyAuth(@RequestBody AddStrategyAuthorityRequest addStrategyAuthorityRequest){

        for (Integer strategyId : addStrategyAuthorityRequest.getStrategyId()) {
            if (DEVICE == addStrategyAuthorityRequest.getAuthType()){
                for (Integer deviceId : addStrategyAuthorityRequest.getDeviceId()) {
                    boolean b = strategyAuthorityService.strategyIsAdd(deviceId, null, null, null,strategyId);
                    System.out.println(b);
                    if (!b){
                        strategyAuthorityService.addStrategyAuth(deviceId, null, null ,null, strategyId);
                    }

                }
            }else if (ROLE == addStrategyAuthorityRequest.getAuthType()){
                for (Integer roleId : addStrategyAuthorityRequest.getRoleId()) {
                    if (!strategyAuthorityService.strategyIsAdd(null, roleId, null,null, strategyId))
                        strategyAuthorityService.addStrategyAuth(null, roleId, null, null, strategyId);
                }
            }else if (GROUP == addStrategyAuthorityRequest.getAuthType()){
                for (Integer groupId : addStrategyAuthorityRequest.getGroupId()) {
                    if (!strategyAuthorityService.strategyIsAdd(null, null, groupId, null, strategyId))
                        strategyAuthorityService.addStrategyAuth(null, null, groupId, null, strategyId);
                }
            }else if (APP == addStrategyAuthorityRequest.getAuthType()) {
                for (Integer appUserId : addStrategyAuthorityRequest.getAppUserId()) {
                    if (!strategyAuthorityService.strategyIsAdd(null, null, null,appUserId, strategyId))
                        strategyAuthorityService.addStrategyAuth(null, null, null, appUserId, strategyId);
                }
            }else{
                return ResultHelper.genResult(1,"授权类型错误");
            }
        }

        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "删除策略授权信息", notes = "删除策略授权信息")
    @RequestMapping(value = "/delStrategyAuth",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomDelStrategyAuth(@RequestParam("strategyAuthId") int strategyAuthId){

        int res = strategyAuthorityService.delStrategyAuth(strategyAuthId);
        if (1 == res)
            return ResultHelper.genResultWithSuccess();
        else
            return ResultHelper.genResult(1,"删除策略授权信息失败");
    }



    @ApiOperation(value = "获取策略授权信息", notes = "获取策略授权信息")
    @RequestMapping(value = "/getStrategyAuthInfo/{offset}/{pageSize}", method = RequestMethod.GET)
    @ResponseBody
    public Result unicomGetStrategyAuthInfo(HttpServletRequest request,
                                      @PathVariable("offset") int offset,
                                      @PathVariable("pageSize") int pageSize) throws Exception{
        request.setCharacterEncoding("utf-8");
        PageHelper.startPage(offset,pageSize);
        List<StrategyAuthInfo> strategyAuthInfo = strategyAuthorityService.getStrategyAuthInfo();
        if (strategyAuthInfo.size() > 0){
            for (StrategyAuthInfo authInfo : strategyAuthInfo) {
                if (authInfo.getDeviceInfo() != null){
                    DeviceUser deviceInfo = deviceUserService.getDeviceInfo(authInfo.getDeviceInfo().getDeviceId());
                    authInfo.getDeviceInfo().setDeviceName(deviceInfo.getDeviceName());
                    authInfo.getDeviceInfo().setComments(deviceInfo.getComments());
                }
                if (authInfo.getRoleInfo() != null){
                    Role roleInfo = roleService.roleInfoById(authInfo.getRoleInfo().getRoleId());
                    authInfo.getRoleInfo().setRoleCode(roleInfo.getRoleCode());
                    authInfo.getRoleInfo().setRoleDescribe(roleInfo.getRoleDescribe());
                }
                if (authInfo.getGroupInfo() != null){
                    Group groupInfo = groupService.getGroupInfo(authInfo.getGroupInfo().getGroupId());
                    authInfo.getGroupInfo().setGroupCode(groupInfo.getGroupCode());
                    authInfo.getGroupInfo().setGroupName(groupInfo.getGroupName());
                    authInfo.getGroupInfo().setGroupDescribe(groupInfo.getGroupDescribe());
                }
                if (authInfo.getAppUserInfo() != null){
                    AppUser appUserInfo = appUserService.getAppUserInfo(authInfo.getAppUserInfo().getAppUserId());
                    authInfo.getAppUserInfo().setAppUserId(Integer.valueOf(appUserInfo.getUserId()));
                    authInfo.getAppUserInfo().setAppUserName(appUserInfo.getUserName());
                    authInfo.getAppUserInfo().setComments(appUserInfo.getCommitInfo());
                }
            }
        }
        PageInfo<StrategyAuthInfo> info = new PageInfo<>(strategyAuthInfo);
        return ResultHelper.genResultWithSuccess(info);
    }


}
