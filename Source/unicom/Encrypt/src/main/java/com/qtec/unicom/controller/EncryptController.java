package com.qtec.unicom.controller;

import com.qtec.unicom.component.Result;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.pojo.DataKey;
import com.qtec.unicom.pojo.EncryptionContext;
import com.qtec.unicom.pojo.dto.*;
import com.qtec.unicom.service.EncryptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 对称密钥相关controller
 */
@Api(value = "对称加密管理接口",tags = {"对称加密管理接口"})
@RestController
@RequestMapping(value = "/v1/kms")
public class EncryptController {
    private static final Logger logger = LoggerFactory.getLogger(EncryptController.class);
    @Autowired
    private EncryptService encryptService;

    /**
     * 使用对称主密钥（Symmetric CMK）将明文加密
     * @param request
     * @param encrypt
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","Encrypt"},logical = Logical.OR)
    @ApiOperation(value = "加密",notes = "使用对称主密钥（Symmetric CMK）将明文加密")
    @RequestMapping(value = "/Encrypt", method = RequestMethod.POST)
    public Result unicomEncrypt(HttpServletRequest request, @RequestBody EncryptionContext encrypt) throws Exception {
        logger.info("使用对称主密钥（Symmetric CMK）将明文加密");
        EncryptionContext reEncrypt = encryptService.encrypt(encrypt);
        EncryptionContextDTO dto = new EncryptionContextDTO();
        BeanUtils.copyProperties(dto, reEncrypt);
        return ResultHelper.genResultWithSuccess(dto);
    }

    /**
     * 解密CiphertextBlob中的密文
     * @param request
     * @param encrypt
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","Decrypt"},logical = Logical.OR)
    @ApiOperation(value = "解密",notes = "解密CiphertextBlob中的密文")
    @RequestMapping(value = "/Decrypt", method = RequestMethod.POST)
    public Result unicomDecrypt(HttpServletRequest request, @RequestBody EncryptionContext encrypt) throws Exception {
        logger.info("解密CiphertextBlob中的密文");
        EncryptionContext reEncrypt = encryptService.decrypt(encrypt);
        DecryptionContextDTO dto = new DecryptionContextDTO();
        BeanUtils.copyProperties(dto, reEncrypt);
        return ResultHelper.genResultWithSuccess(dto);
    }

    /**
     * 生成一个随机的数据密钥,返回数据密钥的密文和明文
     * @param request
     * @param dataKey
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","GenerateDataKey"},logical = Logical.OR)
    @ApiOperation(value = "生成密钥",notes = "生成一个随机的数据密钥,返回数据密钥的密文和明文")
    @RequestMapping(value = "/GenerateDataKey", method = RequestMethod.POST)
    public Result unicomGenerateDataKey(HttpServletRequest request, @RequestBody DataKey dataKey) throws Exception {
        logger.info("生成一个随机的数据密钥,返回数据密钥的密文和明文");
        DataKey reDataKey = encryptService.generateDataKey(dataKey);
        GenerateDataKeyDTO dto = new GenerateDataKeyDTO();
        BeanUtils.copyProperties(dto, reDataKey);
//        dto.setPlaintext(UtilService.stringToHexStr(dto.getPlaintext()));
        return ResultHelper.genResultWithSuccess(dto);
    }

    /**
     * 生成一个随机的数据密钥,返回数据密钥的密文
     * @param request
     * @param dataKey
     * @return
     * @throws Exception
     */
//    @RequiresPermissions(value = {"/**","GenerateDataKeyWithoutPlaintext"},logical = Logical.OR)
    @ApiOperation(value = "生成明文密钥",notes = "生成一个随机的数据密钥,返回数据密钥的密文")
    @RequestMapping(value = "/GenerateDataKeyWithoutPlaintext", method = RequestMethod.POST)
    public Result unicomGenerateDataKeyWithoutPlaintext(HttpServletRequest request, @RequestBody DataKey dataKey) throws Exception {
        logger.info("生成一个随机的数据密钥,返回数据密钥的密文");
        DataKey reDataKey = encryptService.generateDataKey(dataKey);
        EncryptionContextDTO dto = new EncryptionContextDTO();
        BeanUtils.copyProperties(dto, reDataKey);
        return ResultHelper.genResultWithSuccess(dto);
    }

    /**
     * 使用传入的公钥加密导出数据密钥
     * @param request
     * @param dataKey
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","ExportDataKey"},logical = Logical.OR)
    @ApiOperation(value = "导出数据密钥",notes = "导出数据密钥")
    @RequestMapping(value = "/ExportDataKey", method = RequestMethod.POST)
    public Result unicomExportDataKey(HttpServletRequest request, @RequestBody DataKey dataKey) throws Exception {
        logger.info("使用传入的公钥加密导出数据密钥");
        DataKey reDataKey = encryptService.exportDataKey(dataKey);
        ExportDataKeyDTO dto = new ExportDataKeyDTO();
        BeanUtils.copyProperties(dto, reDataKey);
        return ResultHelper.genResultWithSuccess(dto);
    }

    /**
     * 随机生成一个数据密钥.返回CMK加密数据密钥的密文和公钥加密数据密钥的密文
     * @param request
     * @param dataKey
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","GenerateAndExportDataKey"},logical = Logical.OR)
    @ApiOperation(value = "随机生成密钥并返回加密密文"
            ,notes = "随机生成一个数据密钥.返回CMK加密数据密钥的密文和公钥加密数据密钥的密文")
    @RequestMapping(value = "/GenerateAndExportDataKey", method = RequestMethod.POST)
    public Result unicomGenerateAndExportDataKey(HttpServletRequest request, @RequestBody DataKey dataKey) throws Exception {
        logger.info("随机生成一个数据密钥.返回CMK加密数据密钥的密文和公钥加密数据密钥的密文");
        DataKey reDataKey = encryptService.generateAndExportDataKey(dataKey);
        GenerateAndExportDataKeyDTO dto = new GenerateAndExportDataKeyDTO();
        BeanUtils.copyProperties(dto, reDataKey);
        return ResultHelper.genResultWithSuccess(dto);
    }

    /**
     *对密文进行转加密
     * @param request
     * @param dataKey
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","ReEncrypt"},logical = Logical.OR)
    @ApiOperation(value = "对密文进行转加密",notes = "对密文进行转加密")
    @RequestMapping(value = "/ReEncrypt", method = RequestMethod.POST)
    public Result unicomReEncrypt(HttpServletRequest request, @RequestBody DataKey dataKey) throws Exception {
        logger.info("对密文进行转加密");
        DataKey reDataKey = encryptService.reEncrypt(dataKey);
        EncryptionContextDTO dto = new EncryptionContextDTO();
        BeanUtils.copyProperties(dto, reDataKey);
        return ResultHelper.genResultWithSuccess(dto);
    }
}




