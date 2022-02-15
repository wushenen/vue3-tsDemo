package com.unicom.quantum.controller;

import com.unicom.quantum.pojo.dto.CurrentAppManager;
import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.controller.vo.UserAppRequest;
import com.unicom.quantum.pojo.App;
import com.unicom.quantum.service.UserAppService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

@Api(tags = "管理员管理应用接口")
@RestController
@RequestMapping("/userApp")
public class UserAppController {

    @Autowired
    private UserAppService userAppService;

    @ApiOperation(value = "应用添加管理员",notes = "应用添加管理员")
    @PostMapping(value = "/addUserApp")
    @ResponseBody
    public Result unicomAddUserApp(@RequestBody UserAppRequest userAppRequest) {
        userAppService.addUserApp(userAppRequest);
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "应用删除管理员",notes = "应用删除管理员")
    @PostMapping(value = "/deleteUserApp")
    @ResponseBody
    public Result unicomDeleteAppDevice(@RequestBody UserAppRequest userAppRequest) {
        userAppService.deleteUserApp(userAppRequest);
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "获取当前应用已添加的管理员",notes = "获取指定应用已添加的管理员")
    @GetMapping(value = "/getAppManagers/{offset}/{pageSize}")
    @ResponseBody
    public Result unicomGetAppManagers(@RequestParam("appId") int appId, @PathVariable("offset") int offset, @PathVariable("pageSize") int pageSize) {
        PageHelper.startPage(offset,pageSize);
        List<CurrentAppManager> currentAppManager = userAppService.getCurrentAppManager(appId);
        PageInfo<CurrentAppManager> info = new PageInfo<>(currentAppManager);
        return ResultHelper.genResultWithSuccess(info);
    }

    @ApiOperation(value = "获取管理员所管理的应用",notes = "获取管理员所管理的应用")
    @GetMapping(value = "/getManagerApps/{offset}/{pageSize}")
    @ResponseBody
    public Result unicomGetManagerApps(@RequestParam("userId") int userId, @PathVariable("offset") int offset, @PathVariable("pageSize") int pageSize) throws Exception {
        PageHelper.startPage(offset,pageSize);
        List<App> currentManagerApp = userAppService.getCurrentManagerApp(userId);
        PageInfo<App> info = new PageInfo<>(currentManagerApp);
        return ResultHelper.genResultWithSuccess(info);
    }
}
