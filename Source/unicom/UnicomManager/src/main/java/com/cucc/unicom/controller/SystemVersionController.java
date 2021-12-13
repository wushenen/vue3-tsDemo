package com.cucc.unicom.controller;

import com.cucc.unicom.component.Result;
import com.cucc.unicom.component.ResultHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Api(tags = "系统信息管理接口")
@RestController
@RequestMapping("/sys")
public class SystemVersionController {

    @Value("${systemVersion}")
    private String systemVersion;

    @Value("${mysqlVersion}")
    private String mysqlVersion;

    @Value("${systemStatus}")
    private String systemStatus;


    @ApiOperation(value = "查看系统信息",notes = "查看系统版本信息")
    @GetMapping("/getVersion")
    @ResponseBody
    public Result unicomGetVersion(){
        HashMap<String, String> map = new HashMap<>();
        map.put("systemVersion",systemVersion);
        map.put("mysqlVersion",mysqlVersion);
        map.put("systemStatus",systemStatus);
        return ResultHelper.genResultWithSuccess(map);
    }

}
