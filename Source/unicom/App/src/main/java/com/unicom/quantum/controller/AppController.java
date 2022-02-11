package com.unicom.quantum.controller;

import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.controller.vo.AddAppRequest;
import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.pojo.App;
import com.unicom.quantum.service.AppService;
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
    public Result unicomAddApp(@RequestBody AddAppRequest addAppRequest) throws QuantumException {
        App app = new App();
        BeanUtils.copyProperties(addAppRequest,app);
        appService.addApp(app);
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

}
