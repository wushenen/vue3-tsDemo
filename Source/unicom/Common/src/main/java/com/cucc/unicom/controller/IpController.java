package com.cucc.unicom.controller;

import com.cucc.unicom.component.Result;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.controller.vo.IpInfoRequest;
import com.cucc.unicom.pojo.IpInfo;
import com.cucc.unicom.service.IpService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Api(value = "访问控制接口",tags = "访问控制接口")
@RestController
@RequestMapping("/ip")
public class IpController {

    @Autowired
    private IpService ipService;

    @ApiOperation(value = "添加ip",notes = "向ip白名单中添加将被允许访问的ip")
    @RequestMapping(value = "/addIp", method = RequestMethod.POST)
    @ResponseBody
    public Result unicomAddIp(@RequestBody IpInfoRequest ipInfoRequest) throws Exception {
        int result = ipService.addIp(ipInfoRequest.getIpInfo());
        if(result ==2){
            return ResultHelper.genResult(1,"该IP信息已添加，勿重复添加");
        }
        if (result == 3) return ResultHelper.genResult(1,"请参照提示信息添加IP信息");
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "获取所以ip白名单信息" ,notes ="分页获取ip白名单信息")
    @RequestMapping(value = "/getAllIps/{offset}/{pageSize}", method = RequestMethod.GET)
    @ResponseBody
    public Result unicomGetAllIps(HttpServletRequest request,
                            @PathVariable("offset") int offset,
                            @PathVariable("pageSize") int pageSize) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        PageHelper.startPage(offset,pageSize);
        List<IpInfo> allIps = ipService.getAllIps();
        PageInfo<IpInfo> info = new PageInfo<>(allIps);
        return ResultHelper.genResultWithSuccess(info);
    }

    @ApiOperation(value = "删除指定ip",notes = "移除白名单中指定ip")
    @RequestMapping(value = "/delIpById", method = RequestMethod.POST)
    @ResponseBody
    public Result unicomDelIpById(@RequestBody IpInfoRequest ipInfoRequest){
        ipService.deleteIpById(ipInfoRequest.getIpInfo());
        return ResultHelper.genResultWithSuccess();
    }


}
