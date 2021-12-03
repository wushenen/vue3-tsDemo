package com.qtec.unicom.controller;

import com.qtec.unicom.component.Exception.PwspException;
import com.qtec.unicom.component.Result;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.component.util.HexUtils;
import com.qtec.unicom.component.util.SwsdsTools;
import com.qtec.unicom.pojo.dto.EncryptDto;
import com.qtec.unicom.pojo.SM9Encrypt;
import com.qtec.unicom.pojo.dto.*;
import com.qtec.unicom.service.SM9EncryptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Api(value = "密码运算接口",tags = {"密码运算接口"})
@RestController
@RequestMapping(value = "/arithmetic")
public class ArithmeticController {
	private static final Logger logger = LoggerFactory.getLogger(ArithmeticController.class);

    @Autowired
    private SM9EncryptService sm9EncryptService;
    /**
     * 在线算法验证SM3
     * @param encryptDto
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","sm3"},logical = Logical.OR)
    @ApiOperation(value = "在线验证SM3算法",notes = "在线验证SM3算法")
    @RequestMapping(value="sm3", method = RequestMethod.POST)
    @ResponseBody
    public Result encryptOnlineSm3(@RequestBody EncryptDto encryptDto) throws Exception {
        byte[] data = SwsdsTools.SM3(encryptDto.getSource().getBytes());
        return ResultHelper.genResultWithSuccess(HexUtils.bytesToHexString(data));
    }
    /**
     * 在线算法验证SM4
     * @param encryptDto
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","sm4"},logical = Logical.OR)
    @ApiOperation(value = "在线验证SM4算法",notes = "在线验证SM4算法")
    @RequestMapping(value="sm4", method = RequestMethod.POST)
    @ResponseBody
    public Result encryptOnlineSm4(@RequestBody EncryptDto encryptDto,
                                   @RequestParam(value = "type") int type) throws Exception {
        byte[] result = null;
        switch (type) {
            case 1://加密
                result = SwsdsTools.sm4Encrypt1(HexUtils.hexStringToBytes(encryptDto.getSource()),encryptDto.getKey(),encryptDto.getIv(),
                        encryptDto.getAlgo(),encryptDto.getMode());
                break;
            case 2://解密
                result = SwsdsTools.sm4Decrypt1(HexUtils.hexStringToBytes(encryptDto.getSource()),encryptDto.getKey(),encryptDto.getIv(),
                        encryptDto.getAlgo(),encryptDto.getMode());
                break;
            default:
        }
        return ResultHelper.genResultWithSuccess(HexUtils.bytesToHexString(result));
    }
    /**
     * 在线国密算法验证SM2功能
     * @param encryptDto
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequiresPermissions(value = {"/**","sm2"},logical = Logical.OR)
    @ApiOperation(value = "在线验证SM2算法",notes = "在线验证SM2算法")
	@RequestMapping(value="sm2", method = RequestMethod.POST)
    @ResponseBody
    public Result encryptOnlineSm2(@RequestBody EncryptDto encryptDto,
                                   @RequestParam(value = "type") int type) throws UnsupportedEncodingException {
        try {
        	Object resData = "";
            byte[] data = null;
        	switch (type) {
            case 1://加密
                try{
                    data = SwsdsTools.sm2_gm_encrypt(HexUtils.hexStringToBytes(encryptDto.getSource()),encryptDto.getPubKey());
                    resData = HexUtils.bytesToHexString(data);
                }catch (Exception e){
                    e.printStackTrace();
                    logger.info(e.getMessage());
                    resData = "加密失败";
                }
                break;
            case 2://解密
                try{
                    data = SwsdsTools.sm2_gm_decrypt(HexUtils.hexStringToBytes(encryptDto.getSource()),encryptDto.getPriKey());
                    resData = HexUtils.bytesToHexString(data);
                }catch (Exception e){
                    e.printStackTrace();
                    logger.info(e.getMessage());
                    resData = "解密失败";
                }
                break;
            case 3://签名
                byte[] source = HexUtils.hexStringToBytes(encryptDto.getSource());
                if(encryptDto.getSignId() != null&&!"".equalsIgnoreCase(encryptDto.getSignId())){
                    source = SwsdsTools.sm3WithId(encryptDto.getPubKey(),HexUtils.hexStringToBytes(encryptDto.getSignId()),HexUtils.hexStringToBytes(encryptDto.getSource()));
                }
                data = SwsdsTools.sm2_gm_sign(encryptDto.getPriKey(),source);
                resData = HexUtils.bytesToHexString(data);
                break;
            case 4://验签
                source = HexUtils.hexStringToBytes(encryptDto.getSource());
                if(encryptDto.getSignId() != null&&!"".equalsIgnoreCase(encryptDto.getSignId())){
                    source = SwsdsTools.sm3WithId(encryptDto.getPubKey(),HexUtils.hexStringToBytes(encryptDto.getSignId()),HexUtils.hexStringToBytes(encryptDto.getSource()));
                }
                resData = SwsdsTools.sm2_gm_verifySign(encryptDto.getPubKey(),
                        source,HexUtils.hexStringToBytes(encryptDto.getSignData()));
                break;
            case 5://秘钥交换
//            	resData = pwspSM9Service.sm2KeyExchange(sm9Params);
                break;
            default:
        }
            return ResultHelper.genResult(0, null, resData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultHelper.genResult(1,"系统异常",e.getMessage());
        }
    }
	/**
	 * sm9功能
	 * @param request
	 * @param sm9Encrypt
	 * @return
	 * @throws UnsupportedEncodingException
	 */
    @RequiresPermissions(value = {"/**","sm9"},logical = Logical.OR)
    @ApiOperation(value = "在线验证SM9算法",notes = "在线验证SM9算法")
	@RequestMapping(value="sm9", method = RequestMethod.POST)
    @ResponseBody
    public Result encryptOnline(HttpServletRequest request,@RequestParam(value = "type") int type,
                                @RequestBody SM9Encrypt sm9Encrypt) throws Exception {
            logger.info("sm9功能：{}",sm9Encrypt);
            String IDB = sm9Encrypt.getIdb();
            Object resData = null;
        	switch (type) {
        	    case 0://获取公开参数
                    SM9ParamsDTO dto = sm9EncryptService.sm9params(IDB,sm9Encrypt);
                    resData = dto;
                    break;
                case 1://加密
                    SM9Encrypt reSM9Encrypt = sm9EncryptService.sm9Encrypt(IDB,sm9Encrypt);
                    SM9EncryptDTO dto1 =new SM9EncryptDTO();
                    BeanUtils.copyProperties(dto1, reSM9Encrypt);
                    resData = dto1;
                    break;
                case 2://解密
                    reSM9Encrypt = sm9EncryptService.sm9Decrypt(IDB,sm9Encrypt);
                    SM9DecryptDTO dto2 =new SM9DecryptDTO();
                    BeanUtils.copyProperties(dto2, reSM9Encrypt);
                    resData = dto2;
                    break;
                case 3://签名
                    reSM9Encrypt = sm9EncryptService.sm9Sign(IDB,sm9Encrypt);
                    SM9SignDTO dto3 =new SM9SignDTO();
                    BeanUtils.copyProperties(dto3, reSM9Encrypt);
                    resData = dto3;
                    break;
                case 4://验签
                    reSM9Encrypt = sm9EncryptService.sm9VeritySign(IDB,sm9Encrypt);
                    SM9VeritySignDTO dto4 =new SM9VeritySignDTO();
                    BeanUtils.copyProperties(dto4, reSM9Encrypt);
                    resData = dto4;
                    break;
                default:
                    throw new PwspException(ResultHelper.genResult(400,"InvalidParameter","指定的参数“<type>”无效"));
            }
            return ResultHelper.genResultWithSuccess(resData);
    }



}





