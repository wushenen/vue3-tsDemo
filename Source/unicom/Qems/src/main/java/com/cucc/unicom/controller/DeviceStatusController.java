package com.cucc.unicom.controller;

import com.alibaba.fastjson.JSONObject;
import com.cucc.unicom.component.Result;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.component.util.NetworkUtil;
import com.cucc.unicom.controller.vo.DeviceStatusDataResponse;
import com.cucc.unicom.controller.vo.UploadDataRequest;
import com.cucc.unicom.pojo.DeviceOperation;
import com.cucc.unicom.pojo.DeviceStatus;
import com.cucc.unicom.pojo.OperateLog;
import com.cucc.unicom.pojo.QemsConfig;
import com.cucc.unicom.service.DeviceStatusService;
import com.cucc.unicom.service.OperateLogService;
import com.cucc.unicom.service.QemsConfigService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@Api(value = "密码态势感知接口",tags = {"密码态势感知接口"})
@RestController
@RequestMapping(value = "/status")
public class DeviceStatusController {

    @Autowired
    private DeviceStatusService deviceStatusService;

    @Autowired
    private QemsConfigService qemsConfigService;

    @Autowired
    private OperateLogService operateLogService;


    @RequiresRoles("deviceUser")
    @ApiOperation(value = "上报数据",notes = "终端上报数据")
    @RequestMapping(value = "/uploadData",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomUploadData(@RequestBody UploadDataRequest uploadDataRequest, HttpServletRequest request, HttpServletResponse response) {
        //判断token是否是重新签发的
        JSONObject object = new JSONObject();
        if (response.getHeader("Token") != null) {
            object.put("Token",response.getHeader("Token"));
        }
        //终端请求的参数都是Base64编码的，需要进行解码才可以使用
        String deviceName = StringUtils.newStringUtf8(Base64.decodeBase64(uploadDataRequest.getDeviceName()));
        //1.根据deviceId更新数据
        uploadDataRequest.setDeviceName(deviceName);
        DeviceStatus deviceStatus = new DeviceStatus();
        BeanUtils.copyProperties(uploadDataRequest,deviceStatus);
        deviceStatus.setDeviceIp(NetworkUtil.getIpAddress(request));
        deviceStatusService.updateDeviceStatusInfo(deviceStatus);
        //查找是否有重启或者置零操作
        DeviceOperation operation = qemsConfigService.getOperation(deviceName);
        if (operation != null){
            qemsConfigService.delQemsOperation(deviceName);
            object.put("operation",operation.getOperation());
            deviceStatusService.checkDeviceStatus(deviceName,operation.getOperation());
            return ResultHelper.genResultWithSuccess(object);
        }
        //2.比较内存中的配置信息是否一致
        QemsConfig qemsConfig = qemsConfigService.getQemsConfig();
        if (qemsConfig.getVersion() != uploadDataRequest.getVersion()) {
            object.put("qemsConfig", qemsConfig);
            return ResultHelper.genResultWithSuccess(object);
        }
        return ResultHelper.genResultWithSuccess(object);
    }


    @ApiOperation(value = "获取密钥云终端数据信息",notes = "获取密钥云终端数据信息")
    @RequestMapping(value = "/listDeviceInfo/{offset}/{pageSize}",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomListDeviceInfo(HttpServletRequest request, @PathVariable("offset") int offset, @PathVariable("pageSize") int pageSize) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        if (pageSize > 50){
            return ResultHelper.genResult(1,"pageSize太大");
        }
        PageHelper.startPage(offset,pageSize);
        List<DeviceStatus> list = deviceStatusService.listDeviceStatusInfo();
        for (DeviceStatus deviceStatus : list) {
            if (deviceStatus.getOnlineTime() != null){
                long time = (new Date().getTime() - deviceStatus.getOnlineTime().getTime())/1000;
                long hours = time / 3600;
                long minutes = (time - (hours*3600))/60;
                long seconds = time - (hours*3600)- (minutes*60) ;
                deviceStatus.setWorkTime(hours+"时"+minutes+"分"+seconds+"秒");
            }else{
                deviceStatus.setDeviceIp(null);
                deviceStatus.setWorkTime(null);
                deviceStatus.setEncRate(null);
                deviceStatus.setDecRate(null);
            }

        }
        PageInfo<DeviceStatus> pageInfo = new PageInfo<>(list);
        return ResultHelper.genResultWithSuccess(pageInfo);
    }


    @ApiOperation(value = "获取密码态势感知数据信息",notes = "获取密码态势感知数据信息")
    @RequestMapping(value = "/getDeviceStatusInfo",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomGetDeviceStatusInfo(){
        DeviceStatusDataResponse deviceStatusInfo = deviceStatusService.getStatusShowInfo();
        List<OperateLog> operateLogs = operateLogService.getOperateLogsForDeviceInfoShow();
        JSONObject object = new JSONObject();
        object.put("deviceStatusInfo",deviceStatusInfo);
        object.put("logs",operateLogs);
        return ResultHelper.genResultWithSuccess(object);
    }


}
