package com.qtec.unicom.controller;

import com.qtec.unicom.component.Result;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.pojo.Material;
import com.qtec.unicom.pojo.dto.MaterialDTO;
import com.qtec.unicom.service.MaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "密钥材料管理接口",tags = {"密钥材料管理接口"})
@RestController
@RequestMapping(value = "/v1/kms")
public class MaterialController {
    private static final Logger logger = LoggerFactory.getLogger(MaterialController.class);

    @Autowired
    private MaterialService materialService;

    /**
     * 获取导入主密钥（CMK）材料
     * @param material
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","GetParametersForImport"},logical = Logical.OR)
    @ApiOperation(value = "获取主密钥材料",notes = "获取导入主密钥（CMK）材料")
    @RequestMapping(value = "/GetParametersForImport", method = RequestMethod.POST)
    @ResponseBody
    public Result getParametersForImport(@RequestBody Material material) throws Exception {
        logger.info("获取导入主密钥（CMK）材料");
        MaterialDTO result = materialService.getParametersForImport(material);
        return ResultHelper.genResultWithSuccess(result);
    }
    /**
     * 导入密钥材料
     * @param material
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","ImportKeyMaterial"},logical = Logical.OR)
    @ApiOperation(value = "导入密钥材料",notes = "导入密钥材料")
    @RequestMapping(value = "/ImportKeyMaterial", method = RequestMethod.POST)
    @ResponseBody
    public Result importKeyMaterial(@RequestBody Material material) throws Exception {
        logger.info("导入密钥材料");
        Object result = materialService.importKeyMaterial(material);
        return ResultHelper.genResultWithSuccess();
    }
    /**
     * 删除已导入的密钥材料
     * @param material
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","DeleteKeyMaterial"},logical = Logical.OR)
    @ApiOperation(value = "删除已导入的密钥材料",notes = "删除已导入的密钥材料")
    @RequestMapping(value = "/DeleteKeyMaterial", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteKeyMaterial(@RequestBody Material material) throws Exception {
        logger.info("删除已导入的密钥材料");
        materialService.deleteKeyMaterial(material);
        return ResultHelper.genResultWithSuccess();
    }


    @ApiOperation(value = "密钥来源判断",notes = "获取密钥参数、导入密钥时密钥来源判断")
    @RequestMapping(value = "/isExternal", method = RequestMethod.GET)
    @ResponseBody
    public Result isExternal(@RequestParam("keyId") String keyId) throws Exception{
        logger.info("密钥来源判断");
        if (materialService.checkExternal(keyId))
            return ResultHelper.genResultWithSuccess();
        return ResultHelper.genResult(1,"材料来源不是external");
    }

}
