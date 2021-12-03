package com.qtec.unicom.controller;

import com.alibaba.fastjson.JSONObject;
import com.qtec.unicom.component.Result;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.component.util.JWTUtil;
import com.qtec.unicom.component.util.UtilService;
import com.qtec.unicom.pojo.MailInfo;
import com.qtec.unicom.service.DeviceUserService;
import com.qtec.unicom.controller.vo.ExportKeyInfosRequest;
import com.qtec.unicom.controller.vo.KeyInfoRequest;
import com.qtec.unicom.pojo.KeyInfo;
import com.qtec.unicom.service.KeyInfoService;
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
//            encKey = HexUtils.bytesToHexString(utilService.decryptCBC(HexUtils.hexStringToBytes(encKey),UtilService.SM4KEY));
            if (encKey == null)
                return ResultHelper.genResult(1,"用户加密密钥错误");
            //终端请求的参数都是Base64编码的，需要进行解码才可以使用
            byte[] keyId = Base64.decodeBase64(keyInfoRequest.getKeyId());
            byte[] keyValue;
            KeyInfo key = keyInfoService.getKeyInfo(keyId);
            if (key == null) {
                keyValue = utilService.generateQuantumRandom(32);
//                byte[] encryptKeyValue = UtilService.encryptMessage(keyValue);
                byte[] encryptKeyValue = utilService.encryptCBC(keyValue,UtilService.SM4KEY);
                keyInfoService.addKeyInfo(keyId,encryptKeyValue,deviceName,2);
            }else{
                if (key.getKeyStatus() == 1)
                    return ResultHelper.genResult(1,"此量子密钥不可使用，请更换量子密钥");
//                keyValue = UtilService.decryptMessage(key.getKeyValue());
                keyValue = utilService.decryptCBC(key.getKeyValue(),UtilService.SM4KEY);
                keyInfoService.updateKeyInfo(keyId,2);
            }
            sendEmail(deviceName);
            //使用请求者的加密密钥进行SM4加密
            System.out.println("encKey.length() = " + encKey.length());
            byte[] encryptCBCKeyValue = utilService.encryptCBC(keyValue, encKey.substring(0,32));
            //返回终端的信息，需要Base64编码
            object.put("keyValue",Base64.encodeBase64String(encryptCBCKeyValue));
            return ResultHelper.genResultWithSuccess(object);
        }
        return ResultHelper.genResult(401,"用户未登录");
    }

    @ApiOperation(value = "撤回密钥",notes = "撤回指定密钥")
    @RequestMapping(value = "/recallKey",method = RequestMethod.POST)
    @ResponseBody
    public Result recallKey(@RequestBody KeyInfoRequest keyInfoRequest) {
        keyInfoService.updateKeyInfo(Base64.decodeBase64(keyInfoRequest.getKeyId()),1);
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "量子密钥额度分配",notes = "销毁指定密钥")
    @RequestMapping(value = "/addKey",method = RequestMethod.GET)
    @ResponseBody
    public Result addKey(@RequestParam("applicant") String applicant, @RequestParam("keyNum") int keyNum) throws Exception {
        byte[] random = utilService.generateQuantumRandom(48 * keyNum);
        byte[] keyId = new byte[16];
        byte[] keyValue = new byte[32];
        KeyInfo keyInfo = new KeyInfo();
        for (int i = 0; i < keyNum; i++) {
            System.arraycopy(random,i*48,keyId,0,16);
            System.arraycopy(random,i*48+16,keyValue,0,32);
            keyInfo.setKeyId(keyId);
            keyInfo.setKeyValue(UtilService.encryptMessage(keyValue));
            keyInfoService.addKeyInfo(keyId,keyValue,applicant,0);
        }
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


    @ApiOperation(value = "获取密钥id",notes = "获取量子密钥id")
    @RequestMapping(value = "/getApplicantKeyId",method = RequestMethod.GET)
    @ResponseBody
    public Result getApplicantKeyId(@RequestParam("applicant") String applicant){
        List<KeyInfo> keyInfos = keyInfoService.getKeyInfos(applicant,0);
        List<KeyInfo> keyInfos1 = keyInfoService.getKeyInfos(applicant,0);
        List<String> keyIds = new ArrayList<>();
        for (KeyInfo keyInfo : keyInfos) {
            keyIds.add(Base64.encodeBase64String(keyInfo.getKeyId()));
        }
        for (KeyInfo keyInfo : keyInfos1) {
            keyIds.add(Base64.encodeBase64String(keyInfo.getKeyId()));
        }
        return ResultHelper.genResultWithSuccess(keyIds);
    }


    @ApiOperation(value = "导出量子密钥",notes = "导出指定用户的全部量子密钥")
    @RequestMapping(value = "/exportKeyInfos",method = RequestMethod.POST)
    @ResponseBody
    public Result exportKeyInfos(HttpServletResponse response, @RequestBody ExportKeyInfosRequest exportKeyInfosRequest) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        List<KeyInfo> keyInfos = keyInfoService.getKeyInfos(exportKeyInfosRequest.getApplicant(),0);
        List<KeyInfo> keyInfos1 = keyInfoService.getKeyInfos(exportKeyInfosRequest.getApplicant(),2);

        StringBuffer sb = new StringBuffer();
        for (KeyInfo keyInfo : keyInfos) {
            sb.append(Base64.encodeBase64String(keyInfo.getKeyId()));
            sb.append(Base64.encodeBase64String(UtilService.decryptMessage(keyInfo.getKeyValue())));
            sb.append("\n");
        }
        for (KeyInfo keyInfo : keyInfos1) {
            sb.append(Base64.encodeBase64String(keyInfo.getKeyId()));
            sb.append(Base64.encodeBase64String(UtilService.decryptMessage(keyInfo.getKeyValue())));
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
            if (map.get("totalNum") != 0){
                float warn = map.get("usedNum")/map.get("totalNum");
                String adminEmail = keyInfoService.getAdminEmail();
                if (warn > 0.9)
                    mailService.sendSimpleMail(new MailInfo("量子密钥告警",adminEmail,"用户"+deviceName+"量子密钥剩余可使用已不足10%，请立即充值"));
                if (warn > 0.7)
                    mailService.sendSimpleMail(new MailInfo("量子密钥预警",adminEmail,"用户"+deviceName+"量子密钥剩余可使用已不足30%，为不影响使用，请及时充值"));
            }
        });
    }

}
