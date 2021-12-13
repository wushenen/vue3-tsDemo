package com.qtec.unicom.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qtec.unicom.component.Exception.PwspException;
import com.qtec.unicom.component.Result;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.component.util.DateUtils;
import com.qtec.unicom.component.util.UtilService;
import com.qtec.unicom.pojo.KeyVersion;
import com.qtec.unicom.pojo.PageVo;
import com.qtec.unicom.pojo.PrimaryKey;
import com.qtec.unicom.pojo.dto.KeyVersionDTO;
import com.qtec.unicom.pojo.dto.PrimaryKeyDTO;
import com.qtec.unicom.service.PrimaryKeyService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(value = "主密钥管理接口",tags = {"主密钥管理接口"})
@RestController
@RequestMapping(value = "/v1/kms")
public class PrimaryKeyController {
    private static final Logger logger = LoggerFactory.getLogger(PrimaryKeyController.class);
    @Autowired
    private PrimaryKeyService primaryKeyService;
    /**
     * 创建一个主密钥
     * @param primaryKey
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","CreateKey"} ,logical = Logical.OR)
    @ApiOperation(value = "创建主密钥",notes = "创建主密钥")
    @RequestMapping(value = "/CreateKey", method = RequestMethod.POST)
    @ResponseBody
    public Result unicomCreateKey(HttpServletRequest request, @RequestBody PrimaryKey primaryKey) throws Exception {
            logger.info("创建一个主密钥");
            primaryKey.setCreator(UtilService.getCurrentUserName(request));
            primaryKey.setOwner(UtilService.getCurrentUserName(request));

            PrimaryKey result = primaryKeyService.addKey(primaryKey);
            PrimaryKeyDTO dto = new PrimaryKeyDTO();
            BeanUtils.copyProperties(dto, result);
            if("Enabled".equalsIgnoreCase(result.getAutomaticRotation())){
                dto.setNextRotationDate(new Date(result.getLastRotationDate().getTime()+ DateUtils.integerUnit(result.getRotationInterval())));
            }
            JSONObject object = new JSONObject();
            object.put("keyMetadata",dto);
            return ResultHelper.genResultWithSuccess(object);
    }
    /**
     * 将一个指定的 CMK 标记为启用状态
     * @param primaryKey
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","EnableKey"} ,logical = Logical.OR)
    @ApiOperation(value = "启用指定主密钥",notes = "将一个指定的 CMK 标记为启用状态")
    @RequestMapping(value = "/EnableKey", method = RequestMethod.POST)
    @ResponseBody
    public Result unicomEnableKey(@RequestBody PrimaryKey primaryKey) throws Exception {
        logger.info("将一个指定的 CMK 标记为启用状态");
        primaryKeyService.enableKey(primaryKey);
        return ResultHelper.genResultWithSuccess();
    }
    /**
     * 将一个指定的主密钥（CMK）标记为禁用状态
     * @param primaryKey
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","DisableKey"} ,logical = Logical.OR)
    @ApiOperation(value = "禁用指定主密钥",notes = "将一个指定的主密钥（CMK）标记为禁用状态")
    @RequestMapping(value = "/DisableKey", method = RequestMethod.POST)
    @ResponseBody
    public Result unicomDisableKey(HttpServletRequest request, @RequestBody PrimaryKey primaryKey) throws Exception {
        logger.info("将一个指定的主密钥（CMK）标记为禁用状态");
        primaryKeyService.disableKey(primaryKey);
        return ResultHelper.genResultWithSuccess();
    }
    /**
     * 申请删除一个指定的主密钥
     * @param primaryKey
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","ScheduleKeyDeletion"} ,logical = Logical.OR)
    @ApiOperation(value = "申请删除一个指定的主密钥",notes = "申请删除一个指定的主密钥")
    @RequestMapping(value = "/ScheduleKeyDeletion", method = RequestMethod.POST)
    @ResponseBody
    public Result unicomScheduleKeyDeletion(@RequestBody PrimaryKey primaryKey) throws Exception {
        logger.info("申请删除一个指定的主密钥");
        Integer pendingWindowInDays = primaryKey.getPendingWindowInDays();
        if(pendingWindowInDays == null || pendingWindowInDays<7 || pendingWindowInDays>30){
            throw new PwspException(ResultHelper.genResult(400,"参数<预删除周期>无效","InvalidParameter"));
        }
        primaryKeyService.scheduleKeyDeletion(primaryKey);
        return ResultHelper.genResultWithSuccess();
    }
    /**
     * 撤销密钥删除
     * @param primaryKey
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","CancelKeyDeletion"} ,logical = Logical.OR)
    @ApiOperation(value = "撤销密钥删除",notes = "撤销删除的密钥")
    @RequestMapping(value = "/CancelKeyDeletion", method = RequestMethod.POST)
    @ResponseBody
    public Result unicomCancelKeyDeletion(@RequestBody PrimaryKey primaryKey) throws Exception {
        logger.info("撤销密钥删除");
        primaryKeyService.cancelKeyDeletion(primaryKey);
        return ResultHelper.genResultWithSuccess();
    }

    /**
     * 返回指定主密钥（CMK）的相关信息
     * @param primaryKey
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","DescribeKey"} ,logical = Logical.OR)
    @ApiOperation(value = "获取指定主密钥相关信息",notes = "返回指定主密钥（CMK）的相关信息")
    @RequestMapping(value = "/DescribeKey", method = RequestMethod.POST)
    @ResponseBody
    public Result unicomDescribeKey(@RequestBody PrimaryKey primaryKey) throws Exception {
        logger.info("返回指定主密钥（CMK）的相关信息");
        PrimaryKeyDTO dto = primaryKeyService.describeKey(primaryKey);
        if("Enabled".equalsIgnoreCase(dto.getAutomaticRotation())){
            dto.setNextRotationDate(new Date(dto.getLastRotationDate().getTime() + DateUtils.integerUnit(dto.getRotationInterval())));
        }
        JSONObject object = new JSONObject();
        object.put("keyMetadata",dto);
        return ResultHelper.genResultWithSuccess(object);
    }

    /**
     * SDK
     * 查询调用者在调用区域的所有主密钥ID
     * @param pageVo
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "SDK查询调用者所有主密钥ID",notes = "SDK查询调用者在调用区域的所有主密钥ID")
    @RequestMapping(value = "/ListKeys", method = RequestMethod.POST)
    @ResponseBody
    public Result unicomListKeys(HttpServletRequest request, @RequestBody PageVo pageVo) throws Exception {
        logger.info("SDK查询调用者在调用区域的所有主密钥ID");
        String userName = UtilService.getCurrentUserName(request);
        Integer pageNumber = pageVo.getPageNumber()==null?0: pageVo.getPageNumber();
        Integer pageSize = pageVo.getPageSize()==null?0: pageVo.getPageSize();
        PageHelper.startPage(pageNumber,pageSize);
        List<Map> result = primaryKeyService.listKeys(userName);
        PageInfo<Map> pageInfo = new PageInfo<>(result);
        return ResultHelper.genResultWithSuccess((PageInfo)pageInfo);
    }
    /**
     * 管理平台
     * 查询调用者在调用区域的所有主密钥ID
     * @param obj
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","ListKeys1"} ,logical = Logical.OR)
    @ApiOperation(value = "查询调用者所有主密钥ID",notes = "查询调用者在调用区域的所有主密钥ID")
    @RequestMapping(value = "/ListKeys1", method = RequestMethod.POST)
    @ResponseBody
    public Result unicomListKeys1(HttpServletRequest request, @RequestBody JSONObject obj) throws Exception {

        request.setCharacterEncoding("utf-8");


        PageVo pageVo = new PageVo();
        BeanUtils.copyProperties(pageVo, obj);
        PrimaryKeyDTO primaryK = new PrimaryKeyDTO();
        BeanUtils.copyProperties(primaryK, obj);

        logger.info("管理平台查询调用者在调用区域的所有主密钥ID");
        Integer pageNumber = pageVo.getPageNumber()==null?0: pageVo.getPageNumber();
        Integer pageSize = pageVo.getPageSize()==null?0: pageVo.getPageSize();
        PageHelper.startPage(pageNumber,pageSize);
        List<Map> result = primaryKeyService.listKeys(primaryK.getKeyId());
        PageInfo<Map> pageInfo = new PageInfo<>(result);
        return ResultHelper.genResultWithSuccess(pageInfo);
    }

    /**
     * 替换主密钥描述信息
     * @param primaryKey
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","UpdateKeyDescription"} ,logical = Logical.OR)
    @ApiOperation(value = "更新描述信息",notes = "替换主密钥描述信息")
    @RequestMapping(value = "/UpdateKeyDescription", method = RequestMethod.POST)
    @ResponseBody
    public Result unicomUpdateKeyDescription(@RequestBody PrimaryKey primaryKey) throws Exception {
        logger.info("替换主密钥描述信息");
        primaryKeyService.updateKeyDescription(primaryKey);
        return ResultHelper.genResultWithSuccess();
    }

    /**
     * 查询指定密钥版本信息
     * @param kv
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","DescribeKeyVersion"} ,logical = Logical.OR)
    @ApiOperation(value = "查询版本信息",notes = "查询指定密钥版本信息")
    @RequestMapping(value = "/DescribeKeyVersion", method = RequestMethod.POST)
    @ResponseBody
    public Result unicomDescribeKeyVersion(@RequestBody KeyVersion kv) throws Exception {
        logger.info("查询指定密钥版本信息");
        KeyVersion keyVersion = primaryKeyService.describeKeyVersion(kv);
        KeyVersionDTO dto = new KeyVersionDTO();
        BeanUtils.copyProperties(dto, keyVersion);
        JSONObject object = new JSONObject();
        object.put("keyVersion",dto);
        return ResultHelper.genResultWithSuccess(object);
    }

    /**
     * 列出主密钥的所有密钥版本
     * @param obj
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","ListKeyVersions"} ,logical = Logical.OR)
    @ApiOperation(value = "列出所有密钥版本",notes = "列出主密钥的所有密钥版本")
    @RequestMapping(value = "/ListKeyVersions", method = RequestMethod.POST)
    @ResponseBody
    public Result unicomListKeyVersions(@RequestBody JSONObject obj) throws Exception {
        PageVo pageVo = new PageVo();
        BeanUtils.copyProperties(pageVo, obj);
        KeyVersion kv = new KeyVersion();
        BeanUtils.copyProperties(kv, obj);
        logger.info("列出主密钥的所有密钥版本");
        Integer pageNumber = pageVo.getPageNumber()==null?0: pageVo.getPageNumber();
        Integer pageSize = pageVo.getPageSize()==null?0: pageVo.getPageSize();
        PageHelper.startPage(pageNumber,pageSize);
        List<Map> result = primaryKeyService.listKeyVersions(kv);
        PageInfo<Map> pageInfo = new PageInfo<>(result);
        return ResultHelper.genResultWithSuccess(pageInfo);
    }

    /**
     * 更新密钥轮转策略
     * 如果开启自动轮转，将在上一次轮转时间加上轮转周期天数后，自动创建一个新的密钥版本，并将它设置为主版本
     * @param primaryKey
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","UpdateRotationPolicy"} ,logical = Logical.OR)
    @ApiOperation(value = "更新轮转策略",notes = "更新密钥轮转策略")
    @RequestMapping(value = "/UpdateRotationPolicy", method = RequestMethod.POST)
    @ResponseBody
    public Result unicomUpdateRotationPolicy(@RequestBody PrimaryKey primaryKey) throws Exception {
        logger.info("更新密钥轮转策略");
        primaryKeyService.updateRotationPolicy(primaryKey);
        return ResultHelper.genResultWithSuccess();
    }

    /**
     * 为非对称主密钥创建一个新的密钥版本
     * @param primaryKey
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","CreateKeyVersion"} ,logical = Logical.OR)
    @ApiOperation(value = "创建新密钥版本",notes = "为非对称主密钥创建一个新的密钥版本")
    @RequestMapping(value = "/CreateKeyVersion", method = RequestMethod.POST)
    @ResponseBody
    public Result unicomCreateKeyVersion(HttpServletRequest request, @RequestBody PrimaryKey primaryKey) throws Exception {
        logger.info("为非对称主密钥创建一个新的密钥版本");
        primaryKey.setCreator(UtilService.getCurrentUserName(request));
        KeyVersion reKeyVersion = primaryKeyService.createKeyVersion(primaryKey);
        KeyVersionDTO dto = new KeyVersionDTO();
        BeanUtils.copyProperties(dto, reKeyVersion);
        JSONObject object = new JSONObject();
        object.put("keyVersion",dto);
        return ResultHelper.genResultWithSuccess(object);
    }
}





