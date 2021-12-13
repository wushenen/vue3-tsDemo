package com.qtec.unicom.controller;

import com.qtec.unicom.component.Result;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.pojo.AsymmetricData;
import com.qtec.unicom.pojo.dto.AsymmetricSignDTO;
import com.qtec.unicom.pojo.dto.DecryptionContextDTO;
import com.qtec.unicom.pojo.dto.EncryptionContextDTO;
import com.qtec.unicom.pojo.dto.GetPublicKeyDTO;
import com.qtec.unicom.service.AsymmetryEncryptService;
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

@Api(value = "非对称加解密管理接口",tags = {"非对称加解密管理接口"})
@RestController
@RequestMapping(value = "/v1/kms")
public class AsymmetryEncryptController {
    private static final Logger logger = LoggerFactory.getLogger(AsymmetryEncryptController.class);
    @Autowired
    private AsymmetryEncryptService asymmetryEncryptService;

    /**
     * 使用非对称密钥进行签名
     * 仅支持Usage为SIGN/VERIFY的非对称密钥
     * @param asymmetricData
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","AsymmetricSign"},logical = Logical.OR)
    @ApiOperation(value = "非对称密钥签名",notes = "非对称密钥签名")
    @RequestMapping(value = "/AsymmetricSign", method = RequestMethod.POST)
    public Result unicomAsymmetricSign(@RequestBody AsymmetricData asymmetricData) throws Exception {
        logger.info("使用非对称密钥进行签名");
        AsymmetricData reAsymmetricData = asymmetryEncryptService.asymmetricSign(asymmetricData);
        AsymmetricSignDTO dto = new AsymmetricSignDTO();
        BeanUtils.copyProperties(dto, reAsymmetricData);
        return ResultHelper.genResultWithSuccess(dto);
    }

    /**
     * 使用非对称密钥进行验签
     * @param asymmetricData
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","AsymmetricVerify"},logical = Logical.OR)
    @ApiOperation(value = "非对称密钥验签",notes = "非对称密钥验签")
    @RequestMapping(value = "/AsymmetricVerify", method = RequestMethod.POST)
    public Result unicomAsymmetricVerify(@RequestBody AsymmetricData asymmetricData) throws Exception {
        logger.info("使用非对称密钥进行验签");
        AsymmetricData reAsymmetricData = asymmetryEncryptService.asymmetricVerify(asymmetricData);
        AsymmetricSignDTO dto = new AsymmetricSignDTO();
        BeanUtils.copyProperties(dto, reAsymmetricData);
        return ResultHelper.genResultWithSuccess(dto);
    }

    /**
     * 使用非对称密钥进行加密
     * @param asymmetricData
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","AsymmetricEncrypt"},logical = Logical.OR)
    @ApiOperation(value = "非对称密钥加密",notes = "非对称密钥加密")
    @RequestMapping(value = "/AsymmetricEncrypt", method = RequestMethod.POST)
    public Result unicomAsymmetricEncrypt(@RequestBody AsymmetricData asymmetricData) throws Exception {
        logger.info("使用非对称密钥进行加密");
        AsymmetricData reAsymmetricData = asymmetryEncryptService.asymmetricEncrypt(asymmetricData);
        EncryptionContextDTO dto = new EncryptionContextDTO();
        BeanUtils.copyProperties(dto, reAsymmetricData);
        return ResultHelper.genResultWithSuccess(dto);
    }

    /**
     * 使用非对称密钥进行解密
     * @param asymmetricData
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","AsymmetricDecrypt"},logical = Logical.OR)
    @ApiOperation(value = "非对称密钥进行解密",notes = "非对称密钥进行解密")
    @RequestMapping(value = "/AsymmetricDecrypt", method = RequestMethod.POST)
    public Result unicomAsymmetricDecrypt(@RequestBody AsymmetricData asymmetricData) throws Exception {
        logger.info("使用非对称密钥进行解密");
        AsymmetricData reAsymmetricData = asymmetryEncryptService.asymmetricDecrypt(asymmetricData);
        DecryptionContextDTO dto = new DecryptionContextDTO();
        BeanUtils.copyProperties(dto, reAsymmetricData);
        return ResultHelper.genResultWithSuccess(dto);
    }

    /**
     * 获取非对称密钥的公钥
     * @param asymmetricData
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","GetPublicKey"},logical = Logical.OR)
    @ApiOperation(value = "获取非对称密钥公钥",notes = "获取非对称密钥公钥")
    @RequestMapping(value = "/GetPublicKey", method = RequestMethod.POST)
    public Result unicomGetPublicKey(@RequestBody AsymmetricData asymmetricData) throws Exception {
        logger.info("获取非对称密钥的公钥");
        AsymmetricData reAsymmetricData = asymmetryEncryptService.getPublicKey(asymmetricData);
        GetPublicKeyDTO dto = new GetPublicKeyDTO();
        BeanUtils.copyProperties(dto, reAsymmetricData);
        return ResultHelper.genResultWithSuccess(dto);
    }
}