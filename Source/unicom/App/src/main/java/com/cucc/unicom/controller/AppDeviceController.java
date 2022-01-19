package com.cucc.unicom.controller;

import com.cucc.unicom.component.Result;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.controller.vo.AppDeviceRequest;
import com.cucc.unicom.pojo.dto.AppDeviceDTO;
import com.cucc.unicom.service.AppDeviceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"应用绑定终端管理接口"})
@RestController
@RequestMapping("/appDevice")
public class AppDeviceController {

    @Autowired
    private AppDeviceService appDeviceService;

    @ApiOperation(value = "应用绑定终端",notes = "应用绑定终端")
    @RequestMapping(value = "/addAppDevice",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomAddAppDevice(@RequestBody AppDeviceRequest appDeviceRequest) {
        int i = appDeviceService.addAppDevice(appDeviceRequest);
        if (i == 1)
            return ResultHelper.genResult(1,"部分终端已绑定其它应用，若想绑定至该应用请先解除其他应用绑定");
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "解除应用绑定终端",notes = "解除应用绑定终端")
    @RequestMapping(value = "/deleteAppDevice",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomDeleteAppDevice(@RequestBody AppDeviceRequest appDeviceRequest) {
        appDeviceService.deleteAppDevice(appDeviceRequest);
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "获取指定应用绑定终端信息",notes = "获取指定应用已绑定的终端信息")
    @RequestMapping(value = "/getAppDevice/{offset}/{pageSize}",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomGetAppDevice(@RequestParam("appId") int appId, @PathVariable("offset") int offset, @PathVariable("pageSize") int pageSize) {
        PageHelper.startPage(offset,pageSize);
        List<AppDeviceDTO> appDevice = appDeviceService.getAppDevice(appId);
        PageInfo<AppDeviceDTO> info = new PageInfo<>(appDevice);
        return ResultHelper.genResultWithSuccess(info);
    }
}
