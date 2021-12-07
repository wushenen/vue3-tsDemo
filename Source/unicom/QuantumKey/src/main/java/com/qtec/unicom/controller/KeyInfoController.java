package com.qtec.unicom.controller;

import com.alibaba.fastjson.JSONObject;
import com.qtec.unicom.component.Result;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.component.util.JWTUtil;
import com.qtec.unicom.component.util.UtilService;
import com.qtec.unicom.controller.vo.GetApplicantKeyIdRequest;
import com.qtec.unicom.pojo.KeyLimit;
import com.qtec.unicom.pojo.MailInfo;
import com.qtec.unicom.service.DeviceUserService;
import com.qtec.unicom.controller.vo.ExportKeyInfosRequest;
import com.qtec.unicom.controller.vo.KeyInfoRequest;
import com.qtec.unicom.pojo.KeyInfo;
import com.qtec.unicom.service.KeyInfoService;
import com.qtec.unicom.service.KeyLimitService;
import com.qtec.unicom.service.MailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Api(value = "量子密钥接口",tags = {"量子密钥接口"})
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
    @RequestMapping(value = "/getKey",method = RequestMethod.POST)
    @ResponseBody
    public Result generateKey(@RequestBody KeyInfoRequest keyInfoRequest, HttpServletResponse response, HttpServletRequest request) throws Exception {
        JSONObject object = new JSONObject();
        if (response.getHeader("Token") != null) {
            object.put("Token",response.getHeader("Token"));
        }
        String token = request.getHeader("Token");
        if (token != null){
            String deviceName = JWTUtil.getUsername(token);
            String encKey = deviceUserService.getEncKey(deviceName);
            if (encKey == null)
                return ResultHelper.genResult(1,"用户加密密钥错误");
            //终端请求的参数都是Base64编码的，需要进行解码才可以使用
            byte[] keyId = Base64.decodeBase64(keyInfoRequest.getKeyId());
            byte[] keyValue;
            KeyInfo key = keyInfoService.getKeyInfo(keyId);
            if (key == null) {
                keyValue = utilService.generateQuantumRandom(32);
//                byte[] encryptKeyValue = UtilService.encryptMessage(keyValue);
                byte[] encryptKeyValue = utilService.encryptCBCWithPadding(keyValue,UtilService.SM4KEY);
                keyInfoService.addKeyInfo(keyId,encryptKeyValue,deviceName,2);
            }else{
                if (key.getKeyStatus() == 1)
                    return ResultHelper.genResult(1,"此量子密钥不可使用，请更换量子密钥");
//                keyValue = UtilService.decryptMessage(key.getKeyValue());
                keyValue = utilService.decryptCBCWithPadding(key.getKeyValue(),UtilService.SM4KEY);
                keyInfoService.updateKeyInfo(keyId,2);
            }
            sendEmail(deviceName);
            //使用请求者的加密密钥进行SM4加密
            byte[] encryptCBCKeyValue = utilService.encryptCBC(keyValue, encKey.substring(0,32));
            //返回终端的信息，需要Base64编码
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
    @RequestMapping(value = "/getKeyForSDK",method = RequestMethod.POST)
    @ResponseBody
    public Result getKeyForSDK(@RequestBody KeyInfoRequest keyInfoRequest, HttpServletRequest request) throws Exception {
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
    @RequestMapping(value = "/recallKey",method = RequestMethod.POST)
    @ResponseBody
    public Result recallKey(@RequestBody KeyInfoRequest keyInfoRequest) {
        keyInfoService.updateKeyInfo(Base64.decodeBase64(keyInfoRequest.getKeyId()),1);
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "还原密钥",notes = "还原指定密钥")
    @RequestMapping(value = "/reductionKey",method = RequestMethod.POST)
    @ResponseBody
    public Result reductionKey(@RequestBody KeyInfoRequest keyInfoRequest) {
        keyInfoService.updateKeyInfo(Base64.decodeBase64(keyInfoRequest.getKeyId()),0);
        return ResultHelper.genResultWithSuccess();
    }


    @ApiOperation(value = "量子密钥额度分配",notes = "分配在线量子密钥额度")
    @RequestMapping(value = "/updateKeyLimit",method = RequestMethod.GET)
    @ResponseBody
    public Result updateKeyLimit(@RequestParam("applicant") String applicant, @RequestParam("keyNum") int keyNum) throws Exception {
        if (keyNum > 2000)
            return ResultHelper.genResult(1,"量子密钥额度不可大于2000");
        KeyLimit keyLimit = new KeyLimit();
        keyLimit.setLimitNum(keyNum);
        keyLimit.setUserName(applicant);
        keyLimit.setUserType(1);
        keyLimitService.updateKeyLimit(keyLimit);
        CompletableFuture.runAsync(()->{
            Long applicantKeyNum = keyInfoService.getApplicantKeyNum(applicant);
            if (applicantKeyNum < keyNum){
                int generateNum = keyNum - applicantKeyNum.intValue();
                byte[] random = new byte[48*generateNum];
                try {
                    random = utilService.generateQuantumRandom(48 * generateNum);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                byte[] keyId = new byte[16];
                byte[] keyValue = new byte[32];
                KeyInfo keyInfo = new KeyInfo();
                for (int i = 0; i < generateNum; i++) {
                    System.arraycopy(random,i*48,keyId,0,16);
                    System.arraycopy(random,i*48+16,keyValue,0,32);
                    keyInfo.setKeyValue(utilService.encryptCBCWithPadding(keyValue,UtilService.SM4KEY));
                    keyInfoService.addKeyInfo(keyId,keyValue,applicant,0);
                }
            }
        });
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "销毁密钥",notes = "销毁指定密钥")
    @RequestMapping(value = "/destroyKey",method = RequestMethod.POST)
    @ResponseBody
    public Result destroyKey(@RequestBody KeyInfoRequest keyInfoRequest){
        keyInfoService.deleteKeyInfo(Base64.decodeBase64(keyInfoRequest.getKeyId()));
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "获取密钥使用情况",notes = "获取指定用户密钥使用情况")
    @RequestMapping(value = "/getKeyUsedInfo",method = RequestMethod.GET)
    @ResponseBody
    public Result getKeyUsedInfo(@RequestParam("applicant") String applicant){
        Map<String, Long> res = keyInfoService.keyInfoStatistics(applicant);
        return ResultHelper.genResultWithSuccess(res);
    }


    @ApiOperation(value = "获取可撤回密钥id",notes = "获取量子密钥id")
    @RequestMapping(value = "/getApplicantKeyId",method = RequestMethod.POST)
    @ResponseBody
    public Result getApplicantKeyId(@RequestBody GetApplicantKeyIdRequest getApplicantKeyIdRequest){
        List<KeyInfo> keyInfos = keyInfoService.getKeyInfosNotInKeyStatus(getApplicantKeyIdRequest.getApplicant(),1);
        List<String> keyIds = new ArrayList<>();
        for (KeyInfo keyInfo : keyInfos) {
            keyIds.add(Base64.encodeBase64String(keyInfo.getKeyId()));
        }
        return ResultHelper.genResultWithSuccess(keyIds);
    }

    @ApiOperation(value = "获取可还原密钥id",notes = "获取量子密钥id")
    @RequestMapping(value = "/getCanReductionApplicantKeyId",method = RequestMethod.POST)
    @ResponseBody
    public Result getCanReductionApplicantKeyId(@RequestBody GetApplicantKeyIdRequest getApplicantKeyIdRequest){
        List<KeyInfo> keyInfos = keyInfoService.getKeyInfos(getApplicantKeyIdRequest.getApplicant(),1);
        List<String> keyIds = new ArrayList<>();
        for (KeyInfo keyInfo : keyInfos) {
            keyIds.add(Base64.encodeBase64String(keyInfo.getKeyId()));
        }
        return ResultHelper.genResultWithSuccess(keyIds);
    }

    @ApiOperation(value = "获取可销毁密钥id",notes = "获取量子密钥id")
    @RequestMapping(value = "/getCanDeleteApplicantKeyId",method = RequestMethod.POST)
    @ResponseBody
    public Result getCanDeleteApplicantKeyId(@RequestBody GetApplicantKeyIdRequest getApplicantKeyIdRequest){
        List<KeyInfo> keyInfos = keyInfoService.getKeyInfosNotInKeyStatus(getApplicantKeyIdRequest.getApplicant(),3);
        List<String> keyIds = new ArrayList<>();
        for (KeyInfo keyInfo : keyInfos) {
            keyIds.add(Base64.encodeBase64String(keyInfo.getKeyId()));
        }
        return ResultHelper.genResultWithSuccess(keyIds);
    }


    @ApiOperation(value = "导出量子密钥",notes = "导出指定用户的全部量子密钥")
    @RequestMapping(value = "/exportKeyInfos",method = RequestMethod.POST)
    @ResponseBody
    public Result exportKeyInfos(HttpServletResponse response, @RequestBody ExportKeyInfosRequest exportKeyInfosRequest) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        List<KeyInfo> keyInfos = keyInfoService.getKeyInfosNotInKeyStatus(exportKeyInfosRequest.getApplicant(),1);
        StringBuffer sb = new StringBuffer();
        for (KeyInfo keyInfo : keyInfos) {
            sb.append("keyId:"+Base64.encodeBase64String(keyInfo.getKeyId()));
            sb.append("    ");
            sb.append("keyValue:"+Base64.encodeBase64String(utilService.decryptCBCWithPadding(keyInfo.getKeyValue(),UtilService.SM4KEY)));
            sb.append("\n");
        }
        //将密钥信息进行base64编码
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
                float warn = usedNum / totalNum;
                String adminEmail = keyInfoService.getAdminEmail();
                if (warn > 0.7){
                    mailService.sendSimpleMail(new MailInfo("量子密钥预警",adminEmail,"用户"+deviceName+"量子密钥剩余可使用已不足30%，为不影响使用，请及时充值"));
                }else if (warn > 0.9){
                    mailService.sendSimpleMail(new MailInfo("量子密钥告警",adminEmail,"用户"+deviceName+"量子密钥剩余可使用已不足10%，请立即充值"));
                }else if (usedNum > totalNum){
                    if ((usedNum-totalNum)%(0.2*totalNum) == 0)
                        mailService.sendSimpleMail(new MailInfo("量子密钥告警",adminEmail,"用户"+deviceName+"量子密钥已超额使用，请立即充值"));
                }
            }
        });
    }

}
