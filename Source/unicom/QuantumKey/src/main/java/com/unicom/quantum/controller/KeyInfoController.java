package com.unicom.quantum.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.component.util.DataTools;
import com.unicom.quantum.component.util.JWTUtil;
import com.unicom.quantum.component.util.UtilService;
import com.unicom.quantum.controller.vo.DeviceKeyUsedInfoResponse;
import com.unicom.quantum.controller.vo.ExportKeyInfosRequest;
import com.unicom.quantum.controller.vo.GetApplicantKeyIdRequest;
import com.unicom.quantum.controller.vo.KeyInfoRequest;
import com.unicom.quantum.pojo.KeyInfo;
import com.unicom.quantum.pojo.KeyLimit;
import com.unicom.quantum.pojo.MailInfo;
import com.unicom.quantum.service.DeviceUserService;
import com.unicom.quantum.service.KeyInfoService;
import com.unicom.quantum.service.KeyLimitService;
import com.unicom.quantum.service.MailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Api(tags = "量子密钥接口")
@RestController
@RequestMapping("/keyInfo")
public class KeyInfoController {

    @Autowired
    private KeyInfoService keyInfoService;

    @Autowired
    private DeviceUserService deviceUserService;

    @Autowired
    private KeyLimitService keyLimitService;

    @Autowired
    private UtilService utilService;

    @Autowired
    private MailService mailService;

    @ApiOperation(value = "获取密钥",notes = "获取指定密钥")
    @PostMapping(value = "/getKey")
    @ResponseBody
    public Result unicomGenerateKey(@RequestBody KeyInfoRequest keyInfoRequest, HttpServletResponse response, HttpServletRequest request) throws Exception {
        JSONObject object = new JSONObject();
        if (response.getHeader("Token") != null) {
            object.put("Token",response.getHeader("Token"));
        }
        String token = request.getHeader("Token");
        if (token != null){
            String deviceName = JWTUtil.getUsername(token);
            String encKey = deviceUserService.getEncKey(deviceName);
            byte[] keyId = Base64.decodeBase64(keyInfoRequest.getKeyId());
            KeyInfo key = keyInfoService.getKeyInfo(keyId,deviceName);
            if (key.getKeyStatus() == 1)
                return ResultHelper.genResult(1,"此量子密钥不可使用，请更换量子密钥");
            sendEmail(deviceName);
            byte[] encryptCBCKeyValue = utilService.encryptCBC(key.getKeyValue(), encKey.substring(0,32));
            object.put("keyValue",Base64.encodeBase64String(encryptCBCKeyValue));
            return ResultHelper.genResultWithSuccess(object);
        }
        return ResultHelper.genResult(401,"用户未登录");
    }


    /**
     * SDK 获取量子密钥，若 RequestBody中带keyId,则使用原来的KeyId，若无，则一并由后端生成
     * @param keyInfoRequest
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "SDK获取密钥",notes = "获取量子密钥，若请求中存在keyId，则使用该keyId")
    @PostMapping(value = "/getKeyForSDK")
    @ResponseBody
    public Result unicomGetKeyForSDK(@RequestBody KeyInfoRequest keyInfoRequest, HttpServletRequest request) throws Exception {
        JSONObject object = new JSONObject();
        String token = request.getHeader("Token");
        if (token != null) {
            String deviceName = JWTUtil.getUsername(token);
            byte[] keyId = new byte[16];
            byte[] keyValue = new byte[32];
            if (keyInfoRequest.getKeyId() != null) {
                keyId = Base64.decodeBase64(keyInfoRequest.getKeyId());
                keyValue= utilService.generateQuantumRandom(32);
            }else {
                byte[] keyInfo = utilService.generateQuantumRandom(48);
                System.arraycopy(keyInfo,0,keyId,0,16);
                System.arraycopy(keyInfo,16,keyValue,0,32);
            }
            keyInfoService.addKeyInfo(keyId,keyValue,deviceName,2);
            object.put("keyId",Base64.encodeBase64String(keyId));
            object.put("keyValue",Base64.encodeBase64String(keyValue));
            return ResultHelper.genResultWithSuccess(object);
        }else{
            return ResultHelper.genResult(401,"用户未登录");
        }
    }

    @ApiOperation(value = "撤回密钥",notes = "撤回指定密钥")
    @PostMapping(value = "/recallKey")
    @ResponseBody
    public Result unicomRecallKey(@RequestBody KeyInfoRequest keyInfoRequest) {
        keyInfoService.updateKeyInfo(Base64.decodeBase64(keyInfoRequest.getKeyId()),1);
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "还原密钥",notes = "还原指定密钥")
    @PostMapping(value = "/reductionKey")
    @ResponseBody
    public Result unicomReductionKey(@RequestBody KeyInfoRequest keyInfoRequest) {
        keyInfoService.updateKeyInfo(Base64.decodeBase64(keyInfoRequest.getKeyId()),0);
        return ResultHelper.genResultWithSuccess();
    }


    @ApiOperation(value = "量子密钥额度分配",notes = "分配在线量子密钥额度")
    @GetMapping(value = "/updateKeyLimit")
    @ResponseBody
    public Result unicomUpdateKeyLimit(@RequestParam("applicant") String applicant, @RequestParam("keyNum") int keyNum) throws Exception {
        if (keyNum > 2000)
            return ResultHelper.genResult(1,"量子密钥额度不可大于2000");
        KeyLimit keyLimit = new KeyLimit();
        keyLimit.setLimitNum(keyNum);
        keyLimit.setUserName(applicant);
        keyLimit.setUserType(1);
        keyLimitService.updateKeyLimit(keyLimit);
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "销毁密钥",notes = "销毁指定密钥")
    @PostMapping(value = "/destroyKey")
    @ResponseBody
    public Result unicomDestroyKey(@RequestBody KeyInfoRequest keyInfoRequest){
        keyInfoService.deleteKeyInfo(Base64.decodeBase64(keyInfoRequest.getKeyId()));
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "获取密钥使用情况",notes = "获取指定用户密钥使用情况")
    @GetMapping(value = "/getKeyUsedInfo")
    @ResponseBody
    public Result unicomGetKeyUsedInfo(@RequestParam("applicant") String applicant){
        Map<String, Long> res = keyInfoService.keyInfoStatistics(applicant);
        return ResultHelper.genResultWithSuccess(res);
    }


    @ApiOperation(value = "获取可撤回密钥id",notes = "获取量子密钥id")
    @PostMapping(value = "/getApplicantKeyId")
    @ResponseBody
    public Result unicomGetApplicantKeyId(@RequestBody GetApplicantKeyIdRequest getApplicantKeyIdRequest){
        List<KeyInfo> keyInfos = keyInfoService.getKeyInfosNotInKeyStatus(getApplicantKeyIdRequest.getApplicant(),1);
        List<String> keyIds = new ArrayList<>();
        for (KeyInfo keyInfo : keyInfos) {
            keyIds.add(Base64.encodeBase64String(keyInfo.getKeyId()));
        }
        return ResultHelper.genResultWithSuccess(keyIds);
    }

    @ApiOperation(value = "获取可还原密钥id",notes = "获取量子密钥id")
    @PostMapping(value = "/getCanReductionApplicantKeyId")
    @ResponseBody
    public Result unicomGetCanReductionApplicantKeyId(@RequestBody GetApplicantKeyIdRequest getApplicantKeyIdRequest){
        List<KeyInfo> keyInfos = keyInfoService.getKeyInfos(getApplicantKeyIdRequest.getApplicant(),1);
        List<String> keyIds = new ArrayList<>();
        for (KeyInfo keyInfo : keyInfos) {
            keyIds.add(Base64.encodeBase64String(keyInfo.getKeyId()));
        }
        return ResultHelper.genResultWithSuccess(keyIds);
    }

    @ApiOperation(value = "获取可销毁密钥id",notes = "获取量子密钥id")
    @PostMapping(value = "/getCanDeleteApplicantKeyId")
    @ResponseBody
    public Result unicomGetCanDeleteApplicantKeyId(@RequestBody GetApplicantKeyIdRequest getApplicantKeyIdRequest){
        List<KeyInfo> keyInfos = keyInfoService.getKeyInfosNotInKeyStatus(getApplicantKeyIdRequest.getApplicant(),3);
        List<String> keyIds = new ArrayList<>();
        for (KeyInfo keyInfo : keyInfos) {
            keyIds.add(Base64.encodeBase64String(keyInfo.getKeyId()));
        }
        return ResultHelper.genResultWithSuccess(keyIds);
    }

    @ApiOperation(value = "获取当前终端密钥使用情况",notes = "获取当前终端密钥使用情况")
    @GetMapping(value = "/getDeviceKeyUsedInfo/{offset}")
    @ResponseBody
    public Result unicomGetDeviceKeyUsedInfo(@RequestParam("deviceName") String deviceName,@PathVariable("offset") int offset){
        PageHelper.startPage(offset,10);
        List<DeviceKeyUsedInfoResponse> info = keyInfoService.getDeviceKeyUsedInfo(deviceName);
        PageInfo<DeviceKeyUsedInfoResponse> pageInfo = new PageInfo<>(info);
        return ResultHelper.genResultWithSuccess(pageInfo);
    }


    @ApiOperation(value = "导出量子密钥",notes = "导出指定用户的全部量子密钥")
    @PostMapping(value = "/exportKeyInfos")
    @ResponseBody
    public Result unicomExportKeyInfos(HttpServletResponse response, @RequestBody ExportKeyInfosRequest exportKeyInfosRequest) throws Exception {
        List<KeyInfo> keyInfos = keyInfoService.getKeyInfosNotInKeyStatus(exportKeyInfosRequest.getApplicant(),1);
        StringBuffer sb = new StringBuffer();
        for (KeyInfo keyInfo : keyInfos) {
            sb.append("keyId:"+Base64.encodeBase64String(keyInfo.getKeyId()));
            sb.append("    ");
            sb.append("keyValue:"+Base64.encodeBase64String(DataTools.decryptMessage(keyInfo.getKeyValue())));
            sb.append("\n");
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(exportKeyInfosRequest.getApplicant()+"量子密钥信息.txt","utf-8"));
        BufferedOutputStream buff = null;
        ServletOutputStream outStr = null;
        try {
            outStr= response.getOutputStream();
            buff = new BufferedOutputStream(outStr);
            buff.write(sb.toString().getBytes("utf-8"));
            buff.flush();
            buff.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            buff.close();
            outStr.close();
        }
        return null;
    }

    private void sendEmail(String deviceName){
        CompletableFuture.runAsync(()->{
            Map<String, Long> map = keyInfoService.keyInfoStatistics(deviceName);
            Long totalNum = map.get("totalNum");
            if (totalNum != 0){
                Long usedNum = map.get("usedNum");
                float warn = (float) usedNum / totalNum;
                String adminEmail = keyInfoService.getAdminEmail();
                if (warn > 0.7 && warn < 0.9){
                    mailService.sendSimpleMail(new MailInfo("量子密钥预警",adminEmail,"用户"+deviceName+"量子密钥剩余可使用已不足30%，为不影响使用，请及时充值"));
                }else if (warn > 0.9 && warn < 1.0){
                    mailService.sendSimpleMail(new MailInfo("量子密钥告警",adminEmail,"用户"+deviceName+"量子密钥剩余可使用已不足10%，请立即充值"));
                }else if (usedNum >= totalNum){
                    if (Math.floor((usedNum-totalNum)%(0.2*totalNum)) == 0){
                        mailService.sendSimpleMail(new MailInfo("量子密钥告警",adminEmail,"用户"+deviceName+"量子密钥已超额使用，请立即充值"));
                    }
                }
            }
        });
    }

}
