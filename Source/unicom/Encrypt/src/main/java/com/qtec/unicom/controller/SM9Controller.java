package com.qtec.unicom.controller;

import com.qtec.unicom.component.Result;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.component.util.UtilService;
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

@Api(value = "密码运算接口",tags = {"密码运算接口"})
@RestController
@RequestMapping(value = "/v1/kms")
public class SM9Controller {
    private static final Logger logger = LoggerFactory.getLogger(SM9Controller.class);
    @Autowired
    private SM9EncryptService sm9EncryptService;
    /**
     * 获取SM9公开参数及用户私钥
     * @param request
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","SM9params"},logical = Logical.OR)
    @ApiOperation(value = "获取SM9公开参数及新用户私钥",notes = "获取SM9公开参数及新用户私钥")
    @RequestMapping(value = "/SM9params", method = RequestMethod.POST)
    @ResponseBody
    public Result sm9params(HttpServletRequest request, @RequestBody SM9Encrypt sm9Encrypt) throws Exception {
        logger.info("获取SM9公开参数及新用户私钥");
        String IDB = UtilService.getCurrentUserName(request);
        SM9ParamsDTO dto = sm9EncryptService.sm9params(IDB,sm9Encrypt);
        return ResultHelper.genResultWithSuccess(dto);
    }

    /**
     * SM9加密
     * @param request
     * @param sm9Encrypt
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","SM9Encrypt"},logical = Logical.OR)
    @ApiOperation(value = "SM9加密",notes = "SM9加密")
    @RequestMapping(value = "/SM9Encrypt", method = RequestMethod.POST)
    @ResponseBody
    public Result sm9Encrypt(HttpServletRequest request, @RequestBody SM9Encrypt sm9Encrypt) throws Exception {
        logger.info("SM9加密");
        String IDB = UtilService.getCurrentUserName(request);
        SM9Encrypt reSM9Encrypt = sm9EncryptService.sm9Encrypt(IDB,sm9Encrypt);
        SM9EncryptDTO dto =new SM9EncryptDTO();
        BeanUtils.copyProperties(dto, reSM9Encrypt);
        return ResultHelper.genResultWithSuccess(dto);
    }

    /**
     * SM9解密
     * @param request
     * @param sm9Encrypt
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","SM9Decrypt"},logical = Logical.OR)
    @ApiOperation(value = "SM9解密",notes = "SM9解密")
    @RequestMapping(value = "/SM9Decrypt", method = RequestMethod.POST)
    @ResponseBody
    public Result sm9Decrypt(HttpServletRequest request, @RequestBody SM9Encrypt sm9Encrypt) throws Exception {
        logger.info("SM9解密");
        String IDB = UtilService.getCurrentUserName(request);
        SM9Encrypt reSM9Encrypt = sm9EncryptService.sm9Decrypt(IDB,sm9Encrypt);
        SM9DecryptDTO dto =new SM9DecryptDTO();
        BeanUtils.copyProperties(dto, reSM9Encrypt);
        return ResultHelper.genResultWithSuccess(dto);
    }

    /**
     * SM9签名
     * @param request
     * @param sm9Encrypt
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","SM9Sign"},logical = Logical.OR)
    @ApiOperation(value = "SM9签名",notes = "SM9签名")
    @RequestMapping(value = "/SM9Sign", method = RequestMethod.POST)
    @ResponseBody
    public Result sm9Sign(HttpServletRequest request, @RequestBody SM9Encrypt sm9Encrypt) throws Exception {
        logger.info("SM9签名");
        String IDB = UtilService.getCurrentUserName(request);
        SM9Encrypt reSM9Encrypt = sm9EncryptService.sm9Sign(IDB,sm9Encrypt);
        SM9SignDTO dto =new SM9SignDTO();
        BeanUtils.copyProperties(dto, reSM9Encrypt);
        return ResultHelper.genResultWithSuccess(dto);
    }

    /**
     * SM9验签
     * @param request
     * @param sm9Encrypt
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","SM9VeritySign"},logical = Logical.OR)
    @ApiOperation(value = "SM9验签",notes = "SM9验签")
    @RequestMapping(value = "/SM9VeritySign", method = RequestMethod.POST)
    @ResponseBody
    public Result sm9VeritySign(HttpServletRequest request, @RequestBody SM9Encrypt sm9Encrypt) throws Exception {
        logger.info("SM9验签");
        String IDB = UtilService.getCurrentUserName(request);
        SM9Encrypt reSM9Encrypt = sm9EncryptService.sm9VeritySign(IDB,sm9Encrypt);
        SM9VeritySignDTO dto =new SM9VeritySignDTO();
        BeanUtils.copyProperties(dto, reSM9Encrypt);
        return ResultHelper.genResultWithSuccess(dto);
    }
}