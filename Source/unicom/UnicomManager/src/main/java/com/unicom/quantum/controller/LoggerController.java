package com.unicom.quantum.controller;

import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.controller.vo.OperateLogRequest;
import com.unicom.quantum.pojo.OperateLog;
import com.unicom.quantum.service.OperateLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
        PageHelper.startPage(offset,pageSize);
        List<OperateLog> operateLogs = operateLogService.getOperateLogs(operateLogRequest);
        PageInfo<OperateLog> pageInfo = new PageInfo<>(operateLogs);
        return ResultHelper.genResultWithSuccess(pageInfo);
    }

    @ApiOperation(value = "获取操作员信息",notes = "获取日志操作员信息")
    @GetMapping(value = "getOperator")
    @ResponseBody
    public Result unicomsGetOperator(){
        List<String> operator = operateLogService.getOperator();
        return ResultHelper.genResultWithSuccess(operator);
    }

    @ApiOperation(value = "获取操作模块",notes = "获取日志操作模块")
    @GetMapping(value = "getOperateModel")
    @ResponseBody
    public Result unicomsGetOperateModel(@RequestParam(value = "operator",required = false) String operator){
        List<String> operateModel = operateLogService.getOperateModel(operator);
        return ResultHelper.genResultWithSuccess(operateModel);
    }


    @ApiOperation(value = "获取操作功能",notes = "获取日志操作功能信息")
    @GetMapping(value = "getOperateDetail")
    @ResponseBody
    public Result unicomsGetOperateDetail(@RequestParam(value = "operator",required = false) String operator,@RequestParam(value = "operateModel",required = false) String operateModel){
        List<String> operateDetail = operateLogService.getOperateDetail(operator,operateModel);
        return ResultHelper.genResultWithSuccess(operateDetail);
    }
}
