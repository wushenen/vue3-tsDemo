package com.unicom.quantum.controller;

import com.alibaba.fastjson.JSONObject;
import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.component.util.JWTUtil;
import com.unicom.quantum.component.util.UtilService;
import com.unicom.quantum.controller.vo.BatchKeyInfoSDKRequest;
import com.unicom.quantum.pojo.KeyInfo;
import com.unicom.quantum.service.KeyInfoService;
import com.unicom.quantum.controller.vo.KeyInfoSDKRequest;
import com.unicom.quantum.pojo.MailInfo;
import com.unicom.quantum.service.DeviceUserService;
import com.unicom.quantum.service.MailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Api(value = "量子密钥接口",tags = {"量子密钥接口"})
@RestController
@RequestMapping("/keyInfo")
public class KeyInfoController {

    @Autowired
    private KeyInfoService keyInfoService;

    @Autowired
    private DeviceUserService deviceUserService;

    @Autowired
    private UtilService utilService;

    @Autowired
    private MailService mailService;

    @ApiOperation(value = "获取密钥",notes = "获取指定密钥")
    @RequestMapping(value = "/getKey",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomGenerateKey(@RequestBody KeyInfoSDKRequest keyInfoRequest, HttpServletResponse response, HttpServletRequest request) throws Exception {
        JSONObject object = new JSONObject();
        String token = keyInfoRequest.getToken();
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


    @ApiOperation(value = "获取密钥",notes = "获取指定密钥")
    @RequestMapping(value = "/batchGetKey",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomBatchGetKey(@RequestBody BatchKeyInfoSDKRequest batchKeyInfoSDKRequest, HttpServletResponse response, HttpServletRequest request) throws Exception {
        JSONObject object = new JSONObject();
        String token = batchKeyInfoSDKRequest.getToken();
        if (token != null){
            String deviceName = JWTUtil.getUsername(token);
            String encKey = deviceUserService.getEncKey(deviceName);
            if (encKey == null)
                return ResultHelper.genResult(1,"用户加密密钥错误");
            ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
            for (String batchKeyId : batchKeyInfoSDKRequest.getKeyIds()) {
                byte[] keyId = Base64.decodeBase64(batchKeyId);
                KeyInfo key = keyInfoService.getKeyInfo(keyId);
                if (key == null) {
                    byte[] keyValue = utilService.generateQuantumRandom(32);
                    //byte[] encryptKeyValue = UtilService.encryptMessage(keyValue);
                    byte[] encryptKeyValue = utilService.encryptCBCWithPadding(keyValue,UtilService.SM4KEY);
                    keyInfoService.addKeyInfo(keyId,encryptKeyValue,deviceName,2);
                    byte[] encryptCBCKeyValue = utilService.encryptCBC(keyValue, encKey.substring(0,32));
                    map.put(batchKeyId,Base64.encodeBase64String(encryptCBCKeyValue));
                }else{
                    if (key.getKeyStatus() == 1)
                        map.put(batchKeyId,Base64.encodeBase64String("This keyId is not available".getBytes()));
                    //keyValue = UtilService.decryptMessage(key.getKeyValue());
                    byte[] keyValue = utilService.decryptCBCWithPadding(key.getKeyValue(), UtilService.SM4KEY);
                    keyInfoService.updateKeyInfo(keyId,2);
                    byte[] encryptCBCKeyValue = utilService.encryptCBC(keyValue, encKey.substring(0,32));
                    map.put(batchKeyId,Base64.encodeBase64String(encryptCBCKeyValue));
                }
            }
            sendEmail(deviceName);
            object.put("keyValues", map);
            return ResultHelper.genResultWithSuccess(object);
        }
        return ResultHelper.genResult(401,"用户未登录");
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
                    if ((usedNum-totalNum)%(0.2*totalNum) == 0){
                        mailService.sendSimpleMail(new MailInfo("量子密钥告警",adminEmail,"用户"+deviceName+"量子密钥已超额使用，请立即充值"));
                    }
                }
            }
        });
    }

}
