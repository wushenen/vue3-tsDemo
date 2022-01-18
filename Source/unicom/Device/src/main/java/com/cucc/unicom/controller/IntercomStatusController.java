package com.cucc.unicom.controller;

import com.cucc.unicom.component.Result;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.pojo.IntercomStatus;
import com.cucc.unicom.service.IntercomStatusService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"对讲集群状态接口"})
@RestController
@RequestMapping("/intercom")
public class IntercomStatusController {

    @Autowired
    private IntercomStatusService intercomStatusService;

    @RequiresRoles("systemUser")
    @ApiOperation(value = "获取对讲集群信息" ,notes = "获取对讲集群信息")
    @RequestMapping(value = "/getIntercomStatus/{offset}/{pageSize}",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomGetIntercomStatus(@PathVariable("offset") int offset, @PathVariable("pageSize") int pageSize){
        PageHelper.startPage(offset,pageSize);
        List<IntercomStatus> intercomStatus = intercomStatusService.getIntercomStatus();
        PageInfo<IntercomStatus> info = new PageInfo<>(intercomStatus);
        return ResultHelper.genResultWithSuccess(info);
    }

}
