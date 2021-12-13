package com.cucc.unicom.controller;

import com.cucc.unicom.pojo.dto.EncryptDto;
import com.cucc.unicom.component.Result;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.component.util.HexUtils;
import com.cucc.unicom.component.util.SwsdsTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@Api(value = "密码运算接口",tags = {"密码运算接口"})
@RestController
@RequestMapping(value = "/arithmetic")
public class ArithmeticController {
	private static final Logger logger = LoggerFactory.getLogger(ArithmeticController.class);
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
    public Result unicomEncryptOnlineSm3(@RequestBody EncryptDto encryptDto) throws Exception {
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
    public Result unicomEncryptOnlineSm4(@RequestBody EncryptDto encryptDto,
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
    public Result unicomEncryptOnlineSm2(@RequestBody EncryptDto encryptDto,
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

}





