package com.cucc.unicom.controller;

import com.cucc.unicom.pojo.User;
import com.cucc.unicom.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cucc.unicom.component.Result;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.controller.vo.AddUserRequest;
import com.cucc.unicom.controller.vo.UpdateUserRequest;
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
    @RequestMapping(value = "/userNameCheck",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomUserNameCheck(@RequestParam("userName") String userName){
        if (userService.userExist(userName)) {
            return ResultHelper.genResult(1,"用户名已被占用");
        }
        return ResultHelper.genResultWithSuccess();
    }


    /**
     * 添加用户信息
     * @param addUserRequest
     * @return
     */
    @RequiresRoles("systemUser")
    @ApiOperation(value = "添加用户信息",notes = "添加用户信息")
    @RequestMapping(value = "/addUserInfo",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomAddUserInfo(@RequestBody AddUserRequest addUserRequest) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        int i = userService.addUser(addUserRequest);
        if (i == 1)
            return ResultHelper.genResult(1,"用户名已被占用");
        return ResultHelper.genResultWithSuccess();
    }


    /**
     * 修改用户信息
     * @param updateUserRequest
     * @return
     */
    @ApiOperation(value = "修改系统用户信息",notes = "修改系统用户信息（修改信息包括密码、备注）")
    @RequestMapping(value = "/updateUserInfo",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomUpdateUserInfo(@RequestBody UpdateUserRequest updateUserRequest) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        userService.updateUser(updateUserRequest);
        return ResultHelper.genResultWithSuccess();
    }



    @ApiOperation(value = "删除系统用户",notes = "删除系统用户")
    @RequestMapping(value = "/deleteUserInfo",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomDeleteUserInfo(@RequestParam("id") int id) {
        userService.deleteUser(id);
        return ResultHelper.genResultWithSuccess();
    }


    /**
     * 获取所有用户信息
     * @param request
     * @param offset
     * @param pageSize
     * @return
     * @throws UnsupportedEncodingException
     */
    @ApiOperation(value = "获取所有系统用户信息",notes = "获取所有系统用户信息")
    @RequestMapping(value = "/getAllUsers/{offset}/{pageSize}", method = RequestMethod.GET)
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

}
