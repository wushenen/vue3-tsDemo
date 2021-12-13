package com.cucc.unicom.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSONObject;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.component.util.UtilService;
import com.cucc.unicom.controller.vo.*;
import com.cucc.unicom.pojo.DTO.ExportDeviceUserInfo;
import com.cucc.unicom.pojo.DeviceUser;
import com.cucc.unicom.service.DeviceUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cucc.unicom.component.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/device")
@Api(value = "终端用户管理接口",tags = {"终端用户管理接口"})
public class DeviceUserController {

    @Autowired
    private DeviceUserService deviceUserService;

    @Autowired
    private UtilService utilService;

    /**
     * 获取所有终端信息
     * @param request
     * @param deviceName
     * @param offset
     * @param pageSize
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取所有终端用户信息" ,notes = "获取所有终端用户信息")
    @RequestMapping(value = "/getAllDevice/{offset}/{pageSize}",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomGetAllDevice(HttpServletRequest request, @RequestParam(value = "deviceName",required = false) String deviceName,
                               @PathVariable("offset") int offset,
                               @PathVariable("pageSize") int pageSize) throws Exception{
        request.setCharacterEncoding("UTF-8");
        if (pageSize > 50) {
            return ResultHelper.genResult(1, "pageSize过大");
        }
        PageHelper.startPage(offset,pageSize);
        List<DeviceUser> list = deviceUserService.listAllDeviceUser(deviceName);
        PageInfo<DeviceUser> pageInfo = new PageInfo<>(list);
        return ResultHelper.genResultWithSuccess(pageInfo);
    }

    /*获取所有终端信息*/
    @ApiOperation(value = "获取所有终端用户信息(添加权限使用)",notes = "获取所有终端用户信息(添加权限使用)")
    @RequestMapping(value = "/getAllDeviceInfo",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomGetAllDeviceInfo(@RequestParam(value = "deviceName",required = false) String deviceName){
        List<DeviceUser> list = deviceUserService.listAllDeviceUser(deviceName);
        return ResultHelper.genResultWithSuccess(list);
    }



    /*获取终端信息*/
    @ApiOperation(value = "获取指定终端用户信息" ,notes = "获取指定终端用户信息")
    @RequestMapping(value = "/getDeviceInfo",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomGetDeviceInfo(@RequestBody GetDeviceInfoRequest getDeviceInfoRequest){
        DeviceUser deviceInfo = deviceUserService.getDeviceInfo(getDeviceInfoRequest.getDeviceId());
        return ResultHelper.genResultWithSuccess(deviceInfo);
    }

    /*添加终端信息*/
    @ApiOperation(value = "添加终端用户信息",notes = "添加终端用户信息")
    @RequestMapping(value = "/addDeviceUser",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomAddDeviceUser(@RequestBody AddDeviceUserRequest addDeviceUserRequest) throws Exception {
        boolean exist = deviceUserService.userNameIsExist(addDeviceUserRequest.getDeviceName());
        if (!exist){
            DeviceUser deviceUser = new DeviceUser();
            deviceUser.setDeviceName(addDeviceUserRequest.getDeviceName());
            deviceUser.setPassword(addDeviceUserRequest.getPassword());
            deviceUser.setComments(addDeviceUserRequest.getComments());
            deviceUser.setUserType(addDeviceUserRequest.getUserType());
            if (addDeviceUserRequest.getUserType() == 1){
                deviceUser.setEncKey(utilService.generateQuantumRandom(32));
            }
            int res = deviceUserService.addDeviceUser(deviceUser);

            if (res == 1){
                return ResultHelper.genResultWithSuccess();
            }else
                return ResultHelper.genResult(1,"添加终端用户失败");
        }else
            return ResultHelper.genResult(1,"用户名已被占用");

    }

    /*批量添加终端信息*/
    @ApiOperation(value = "批量导入终端",notes = "批量导入终端用户信息")
    @RequestMapping(value = "/importDeviceUser",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomImportDeviceUser(@RequestPart("excelFile") MultipartFile excelFile) throws Exception {
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1);
        List<DeviceUser> list = ExcelImportUtil.importExcel(excelFile.getInputStream(), DeviceUser.class, importParams);
        for (DeviceUser insertUser : list) {
            if(insertUser.getDeviceName().trim() == null && insertUser.getPassword().trim() == null && insertUser.getComments().trim() == null)
                return ResultHelper.genResult(1,"导入数据中至少有一条数据的用户名、密码和备注为空");
        }
        //检查用户名是否重复
        for (DeviceUser deviceUser : list) {
            String userName = deviceUser.getDeviceName();
            if (deviceUserService.userNameIsExist(userName))
                return ResultHelper.genResult(1,"用户名" + userName + "已被占用，用户数据导入失败");
        }

        for (DeviceUser deviceUser : list) {
            if (deviceUser.getUserType() == 1)
                deviceUser.setEncKey(utilService.generateQuantumRandom(32));
            deviceUserService.addDeviceUser(deviceUser);
        }
        return ResultHelper.genResultWithSuccess();
    }



    /*删除终端信息*/
    @ApiOperation(value = "删除终端用户信息",notes = "删除终端用户信息")
    @RequestMapping(value = "/deleteDevice",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomDeleteDevice(@RequestBody DeleteDeviceRequest deleteDeviceRequest){
        for (Integer id : deleteDeviceRequest.getDeviceId()) {
            deviceUserService.deleteDevice(id);
        }
        return ResultHelper.genResultWithSuccess();
    }

    /*修改终端用户信息*/
    @ApiOperation(value = "修改终端用户信息" ,notes = "修改终端用户信息")
    @RequestMapping(value = "/updateDevice",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomUpdateDevice(@RequestBody UpdateUserInfoRequest updateUserInfoRequest){

        boolean userNameIsExist = deviceUserService.userNameIsExist(updateUserInfoRequest.getDeviceName());
        boolean equals = deviceUserService.getDeviceInfo(updateUserInfoRequest.getDeviceId()).getDeviceName().equals(updateUserInfoRequest.getDeviceName());
        if ( !userNameIsExist || equals){
            int i = deviceUserService.updateDevice(updateUserInfoRequest);
            if (i == 1)
                return ResultHelper.genResultWithSuccess();
            else
                return ResultHelper.genResult(1,"修改终端信息失败");
        }else
            return ResultHelper.genResult(1,"用户名重复，修改失败");
    }



    @ApiOperation(value = "终端用户导入模板下载",notes = "终端用户导入模板下载")
    @RequestMapping(value = "/getDeviceUserModel", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public void unicomGetDeviceUsersModel(HttpServletResponse response) throws IOException {
        String fileName = "终端用户信息模板.xls";
        response.setCharacterEncoding("UTF-8");
        List<DeviceUser> list = new ArrayList<>();
        list.add(new DeviceUser("deviceUser1","a1234567",0,"用户数据示例"));
        list.add(new DeviceUser("deviceUser2","a1234567",1,"用户数据示例"));
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), DeviceUser.class, list);
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
    }

    @ApiOperation(value = "模糊查询终端用户信息" ,notes = "模糊查询终端用户信息")
    @RequestMapping(value = "/queryDeviceUser",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomQueryDeviceUser(@RequestParam("deviceName") String deviceName){
        List<DeviceUser> deviceUsers = deviceUserService.queryDeviceUser(deviceName);
        return ResultHelper.genResultWithSuccess(deviceUsers);
    }


    @ApiOperation(value = "批量导出终端用户信息",notes = "导出选中的终端用户信息")
    @RequestMapping(value = "/exportDeviceUsers",method = RequestMethod.POST)
    @ResponseBody
    public void unicomExportDeviceUsers(HttpServletResponse response, @RequestBody ExportDeviceUserRequest exportDeviceUserRequest) throws IOException {
        List<ExportDeviceUserInfo> deviceUsers = deviceUserService.exportDeviceUserInfo(exportDeviceUserRequest.getDeviceIds());
        Object o = JSONObject.toJSON(deviceUsers);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("终端用户信息.txt","utf-8"));
        BufferedOutputStream buff =null;
        ServletOutputStream outStr = null;
        try {
            outStr= response.getOutputStream();
            buff = new BufferedOutputStream(outStr);
            buff.write(o.toString().getBytes("utf-8"));
            buff.flush();
            buff.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            buff.close();
            outStr.close();
        }
    }



}
