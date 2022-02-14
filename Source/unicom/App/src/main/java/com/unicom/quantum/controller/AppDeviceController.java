package com.unicom.quantum.controller;

import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.controller.vo.AppDeviceRequest;
import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.pojo.dto.AppDeviceDTO;
import com.unicom.quantum.service.AppDeviceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "应用绑定终端管理接口")
@RestController
@RequestMapping("/appDevice")
public class AppDeviceController {

    @Autowired
    private AppDeviceService appDeviceService;

    @ApiOperation(value = "应用绑定终端",notes = "应用绑定终端")
    @PostMapping(value = "/addAppDevice")
    @ResponseBody
    public Result unicomAddAppDevice(@RequestBody AppDeviceRequest appDeviceRequest) throws QuantumException {
        appDeviceService.addAppDevice(appDeviceRequest);
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "解除应用绑定终端",notes = "解除应用绑定终端")
    @PostMapping(value = "/deleteAppDevice")
    @ResponseBody
    public Result unicomDeleteAppDevice(@RequestBody AppDeviceRequest appDeviceRequest) {
        appDeviceService.deleteAppDevice(appDeviceRequest);
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "获取指定应用绑定终端信息",notes = "获取指定应用已绑定的终端信息")
    @GetMapping(value = "/getAppDevice/{offset}/{pageSize}")
    @ResponseBody
    public Result unicomGetAppDevice(@RequestParam("appId") int appId, @PathVariable("offset") int offset, @PathVariable("pageSize") int pageSize) {
        PageHelper.startPage(offset,pageSize);
        List<AppDeviceDTO> appDevice = appDeviceService.getAppDevice(appId);
        PageInfo<AppDeviceDTO> info = new PageInfo<>(appDevice);
        return ResultHelper.genResultWithSuccess(info);
    }
}
