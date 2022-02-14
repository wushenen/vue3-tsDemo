package com.unicom.quantum.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.component.util.JWTUtil;
import com.unicom.quantum.component.util.NetworkUtil;
import com.unicom.quantum.controller.vo.DeviceStatusDataResponse;
import com.unicom.quantum.controller.vo.UploadDataRequest;
import com.unicom.quantum.pojo.AppConfig;
import com.unicom.quantum.pojo.DeviceOperation;
import com.unicom.quantum.pojo.DeviceStatus;
import com.unicom.quantum.service.AppConfigService;
import com.unicom.quantum.service.AppStatusService;
import com.unicom.quantum.service.DeviceStatusService;
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

@Api(value = "应用数据监测接口",tags = {"应用数据监测接口"})
@RestController
@RequestMapping(value = "/status")
public class DeviceStatusController {

    @Autowired
    private DeviceStatusService deviceStatusService;

    @Autowired
    private AppConfigService appConfigService;

    @Autowired
    private AppStatusService appStatusService;

    @RequiresRoles("deviceUser")
    @ApiOperation(value = "上报数据",notes = "终端上报数据")
    @PostMapping(value = "/uploadData")
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
        DeviceOperation operation = appConfigService.getOperation(deviceName);
        if (operation != null){
            appConfigService.delQemsOperation(deviceName);
            object.put("operation",operation.getOperation());
            deviceStatusService.checkDeviceStatus(deviceName,operation.getOperation());
            return ResultHelper.genResultWithSuccess(object);
        }

        AppConfig appConfig = appConfigService.getAppConfig(JWTUtil.getUserId(request.getHeader("Token")));
        if (appConfig.getVersion() != uploadDataRequest.getVersion()) {
            object.put("qemsConfig", appConfig);
            return ResultHelper.genResultWithSuccess(object);
        }
        return ResultHelper.genResultWithSuccess(object);
    }


    @ApiOperation(value = "获取密钥云终端数据信息",notes = "获取密钥云终端数据信息")
    @GetMapping(value = "/listDeviceInfo/{offset}/{pageSize}")
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
    @GetMapping(value = "/getDeviceStatusInfo")
    @ResponseBody
    public Result unicomGetDeviceStatusInfo(){
        DeviceStatusDataResponse deviceStatusInfo = deviceStatusService.getStatusShowInfo();
        JSONObject object = new JSONObject();
        object.put("deviceStatusInfo",deviceStatusInfo);
        return ResultHelper.genResultWithSuccess(object);
    }


    @ApiOperation(value = "获取当前应用数据信息",notes = "获取当前应用数据信息")
    @GetMapping(value = "/getCurrentAppStatusInfo")
    @ResponseBody
    public Result unicomGetCurrentAppStatusInfo(@RequestParam("appId") int appId){
        DeviceStatusDataResponse currentAppStatus = appStatusService.getCurrentAppStatus(appId);
        JSONObject object = new JSONObject();
        object.put("deviceStatusInfo",currentAppStatus);
        return ResultHelper.genResultWithSuccess(object);
    }


    @ApiOperation(value = "获取当前应用设备信息",notes = "获取当前应用绑定的设备信息")
    @GetMapping(value = "/getCurrentAppDeviceInfo/{offset}/{pageSize}")
    @ResponseBody
    public Result unicomGetCurrentAppStatusInfo(@RequestParam("appId") int appId, @PathVariable("offset") int offset, @PathVariable("pageSize") int pageSize){
        PageHelper.startPage(offset,pageSize);
        List<DeviceStatus> deviceStatuses = appStatusService.listDeviceStatusInfo(appId);
        if (deviceStatuses.size()!=0) {
            PageInfo<DeviceStatus> info = new PageInfo<>(deviceStatuses);
            return ResultHelper.genResultWithSuccess(info);
        }else
            return ResultHelper.genResultWithSuccess();

    }

}
