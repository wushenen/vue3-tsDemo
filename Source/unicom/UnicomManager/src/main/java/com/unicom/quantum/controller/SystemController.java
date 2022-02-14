package com.unicom.quantum.controller;

import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.controller.vo.LinuxServerRequest;
import com.unicom.quantum.service.SystemManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Api(tags = "系统配置接口")
@RestController
@RequestMapping(value = "/system")
public class SystemController {

    @Autowired
    private SystemManageService systemManageService;

    @Value("${systemVersion}")
    private String systemVersion;

    @ApiOperation(value = "查看系统信息",notes = "查看系统版本信息")
    @GetMapping("/getVersion")
    @ResponseBody
    public Result unicomGetVersion(){
        Map<String, String> map = systemManageService.getQkmVersion();
        map.put("systemVersion",systemVersion);
        return ResultHelper.genResultWithSuccess(map);
    }

    @ApiOperation(value = "修改本机ip,网关,掩码",notes = "修改本机ip,网关,掩码")
    @PostMapping("/updateIpNetmaskAndGateway")
    @ResponseBody
    public Result unicomUpdateIpNetmaskAndGateway(@RequestBody @Valid LinuxServerRequest linuxServerRequest) throws Exception{
        systemManageService.updateIpNetmaskAndGateway(linuxServerRequest);
        return ResultHelper.genResultWithSuccess();
    }


    @ApiOperation(value = "系统初始化",notes = "系统初始化")
    @PostMapping("/init")
    @ResponseBody
    public Result unicomInit() throws Exception{
        systemManageService.init();
        return ResultHelper.genResultWithSuccess();
    }

}




