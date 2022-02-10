package com.cucc.unicom.controller;

import com.cucc.unicom.component.Result;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.controller.vo.OperateLogRequest;
import com.cucc.unicom.pojo.OperateLog;
import com.cucc.unicom.service.OperateLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "日志管理接口")
@RestController
@RequestMapping(value = "/logger")
public class LoggerController {

    @Autowired
    private OperateLogService operateLogService;

    @ApiOperation(value = "获取日志信息",notes = "获取指定页和页面大小的日志信息")
    @PostMapping(value = "listOperateLogs/{offset}/{pageSize}")
    @ResponseBody
    public Result unicomListOperateLogs(@RequestBody OperateLogRequest operateLogRequest, @PathVariable("offset") int offset, @PathVariable("pageSize") int pageSize){
        if (pageSize > 50) return ResultHelper.genResult(1, "pageSize过大");
//        PageHelper.startPage(offset,pageSize);
        List<OperateLog> operateLogs = operateLogService.getOperateLogs(operateLogRequest);
//        PageInfo<OperateLog> pageInfo = new PageInfo<>(operateLogs);
        return ResultHelper.genResultWithSuccess(operateLogs);
    }
}
