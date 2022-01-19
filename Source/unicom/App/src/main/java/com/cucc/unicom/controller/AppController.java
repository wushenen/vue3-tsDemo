package com.cucc.unicom.controller;

import com.cucc.unicom.component.Result;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.controller.vo.AddAppRequest;
import com.cucc.unicom.pojo.App;
import com.cucc.unicom.pojo.dto.AppBaseInfo;
import com.cucc.unicom.service.AppService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"应用管理接口"})
@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    private AppService appService;

    @ApiOperation(value = "添加应用",notes = "添加应用")
    @RequestMapping(value = "/addApp",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomAddApp(@RequestBody AddAppRequest addAppRequest) {
        App app = new App();
        BeanUtils.copyProperties(addAppRequest,app);
        int i = appService.addApp(app);
        if (i==1) {
            return ResultHelper.genResult(1,"应用名称重复");
        }
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "删除应用",notes = "删除应用")
    @RequestMapping(value = "/deleteApp",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomDeleteApp(@RequestParam("appId") int appId) {
        appService.deleteApp(appId);
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "获取应用",notes = "获取应用")
    @RequestMapping(value = "/getApps/{offset}/{pageSize}",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomGetApps(@PathVariable("offset") int offset, @PathVariable("pageSize") int pageSize) {
        PageHelper.startPage(offset,pageSize);
        List<App> apps = appService.getApps();
        PageInfo<App> info = new PageInfo<>(apps);
        return ResultHelper.genResultWithSuccess(info);
    }

    @ApiOperation(value = "获取应用基本信息",notes = "获取应用基本信息")
    @RequestMapping(value = "/getAppBaseInfo",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomGetAppBaseInfo() {
        List<AppBaseInfo> appBaseInfos = appService.getAppBaseInfo();
        return ResultHelper.genResultWithSuccess(appBaseInfos);
    }
}
