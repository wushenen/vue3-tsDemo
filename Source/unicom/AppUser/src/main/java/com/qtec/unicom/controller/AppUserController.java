package com.qtec.unicom.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qtec.unicom.component.Result;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.pojo.AppUser;
import com.qtec.unicom.pojo.PageVo;
import com.qtec.unicom.service.AppUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "应用用户管理接口",tags = {"应用用户管理接口"})
@RestController
@RequestMapping(value = "/v1/kms")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    /**
     * 验证注册信息
     * @param request
     * @param param
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "验证注册信息",notes = "注册前需要先验证将要注册信息")
    @RequestMapping(value = "/preRegister",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomPreRegister(HttpServletRequest request, @RequestBody AppUser param) throws Exception {
        AppUser appUser = appUserService.getAppUser(param);
        if (appUser != null) {
            return ResultHelper.genResult(2002,"用户名已存在");
        }
        return ResultHelper.genResultWithSuccess("注册信息校验通过");
    }
    /**
     * 添加用户
     *
     * @return
     */
    @ApiOperation(value = "添加用户",notes = "添加用户")
    @RequestMapping(value = "/AddUser",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomAddUserInfo(HttpServletRequest request, @RequestBody AppUser appUser) throws Exception {
        AppUser reAppUser = appUserService.getAppUser(appUser);
        if (reAppUser != null) {
            return ResultHelper.genResult(2002,"用户名已存在");
        }
        appUserService.addAppUser(appUser);
        return ResultHelper.genResultWithSuccess();
    }

    /**
     * 分页列出用户
     * @param request
     * @param offset
     * @param pageSize
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取用户信息",notes = "分页获取用户信息")
    @RequestMapping(value = "/ListUser/{offset}/{pageSize}", method = RequestMethod.GET)
    @ResponseBody
    public Result unicomListAllUsers(HttpServletRequest request,
                               @PathVariable("offset") int offset,
                               @PathVariable("pageSize") int pageSize) throws Exception {
        request.setCharacterEncoding("UTF-8");
        if (pageSize > 50) {
            return ResultHelper.genResult(1, "pageSize过大");
        }
        String userName = request.getParameter("userName");
        AppUser param = new AppUser();
        param.setUserName(userName);
        PageHelper.startPage(offset,pageSize);
        List<AppUser> list = appUserService.listAppUser(param);
        PageInfo<AppUser> pageInfo = new PageInfo<>(list);
        return ResultHelper.genResultWithSuccess(pageInfo);
    }
    /**
     * SDK POST分页列出用户
     * @param request
     * @param pageVo
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "SDK分页获取用户信息",notes = "SDK分页获取用户信息")
    @RequestMapping(value = "/ListUser", method = RequestMethod.POST)
    @ResponseBody
    public Result unicomListUser(HttpServletRequest request, @RequestBody PageVo pageVo) throws Exception {
        if (pageVo.getPageSize() > 50) {
            return ResultHelper.genResult(1, "pageSize过大");
        }
        String userName = request.getParameter("userName");
        AppUser param = new AppUser();
        param.setUserName(userName);
        PageHelper.startPage(pageVo.getPageNumber(),pageVo.getPageSize());
        List<AppUser> list = appUserService.listAppUser(param);
        PageInfo<AppUser> pageInfo = new PageInfo<>(list);
        return ResultHelper.genResultWithSuccess(pageInfo);
    }
    /**
     *删除应用用户
     * @param appUser
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "删除应用用户",notes = "删除应用用户")
    @RequestMapping(value = "/DelUser", method = RequestMethod.POST)
    @ResponseBody
    public Result unicomDelUser(@RequestBody AppUser appUser) throws Exception {
        int num = appUserService.delUser(appUser);
        return ResultHelper.genResultWithSuccess();
    }

    /**
     * 查询应用用户详情
     * @param appUser
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "查询应用用户详情", notes = "查询应用用户详情")
    @RequestMapping(value = "/DescribeUser", method = RequestMethod.POST)
    @ResponseBody
    public Result unicomDescribeUser(@RequestBody AppUser appUser) throws Exception {
        AppUser re = appUserService.describeUser(appUser);
        return ResultHelper.genResultWithSuccess(re);
    }


    @ApiOperation(value = "模糊查询应用用户信息", notes = "模糊查询应用用户信息")
    @RequestMapping(value = "/queryAppUser",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomQueryDeviceUser(@RequestParam("appUserName") String appUserName){
        List<AppUser> appUsers = appUserService.queryAppUser(appUserName);
        return ResultHelper.genResultWithSuccess(appUsers);
    }

}