package com.unicom.quantum.controller;

import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.pojo.User;
import com.unicom.quantum.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.controller.vo.AddUserRequest;
import com.unicom.quantum.controller.vo.UpdateUserRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

@Api(tags = "系统用户管理接口")
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户名重复检测
     * @param userName
     * @return
     */
    @ApiOperation(value = "用户名重复检测", notes = "检测用户名是否已被占用")
    @GetMapping(value = "/userNameCheck")
    @ResponseBody
    public Result unicomUserNameCheck(@RequestParam("userName") String userName){
        if (userService.userExist(userName)) {
            return ResultHelper.genResult(1,"用户名已被占用");
        }
        return ResultHelper.genResultWithSuccess();
    }


    @RequiresRoles("systemUser")
    @ApiOperation(value = "添加用户信息",notes = "添加用户信息")
    @PostMapping(value = "/addUserInfo")
    @ResponseBody
    public Result unicomAddUserInfo(@RequestBody AddUserRequest addUserRequest) throws QuantumException {
        userService.addUser(addUserRequest);
        return ResultHelper.genResultWithSuccess();
    }


    @RequiresRoles("systemUser")
    @ApiOperation(value = "修改系统用户信息",notes = "修改系统用户信息（修改信息包括密码、备注）")
    @PostMapping(value = "/updateUserInfo")
    @ResponseBody
    public Result unicomUpdateUserInfo(@RequestBody UpdateUserRequest updateUserRequest) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        userService.updateUser(updateUserRequest);
        return ResultHelper.genResultWithSuccess();
    }


    @RequiresRoles("systemUser")
    @ApiOperation(value = "删除系统用户",notes = "删除系统用户")
    @GetMapping(value = "/deleteUserInfo")
    @ResponseBody
    public Result unicomDeleteUserInfo(@RequestParam("id") int id) {
        userService.deleteUser(id);
        return ResultHelper.genResultWithSuccess();
    }


    @RequiresRoles("systemUser")
    @ApiOperation(value = "获取所有系统用户信息",notes = "获取所有系统用户信息")
    @GetMapping(value = "/getAllUsers/{offset}/{pageSize}")
    @ResponseBody
    public Result unicomListAllUsers(HttpServletRequest request,
                               @RequestParam(value = "userName", required = false) String userName,
                               @PathVariable("offset") int offset,
                               @PathVariable("pageSize") int pageSize) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        PageHelper.startPage(offset,pageSize);
        List<User> userInfos = userService.getUserInfos(userName);
        PageInfo<User> pageInfo = new PageInfo<>(userInfos);
        return ResultHelper.genResultWithSuccess(pageInfo);
    }


    @RequiresRoles("systemUser")
    @ApiOperation(value = "获取应用管理员信息",notes = "获取应用管理员信息")
    @GetMapping(value = "/getAppManager")
    @ResponseBody
    public Result unicomGetAppManager() {
        List<User> appManager = userService.getAppManager();
        return ResultHelper.genResultWithSuccess(appManager);
    }

}
