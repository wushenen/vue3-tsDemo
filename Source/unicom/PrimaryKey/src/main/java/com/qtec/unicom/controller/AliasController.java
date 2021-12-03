package com.qtec.unicom.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qtec.unicom.component.Result;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.component.util.UtilService;
import com.qtec.unicom.pojo.Alias;
import com.qtec.unicom.pojo.PageVo;
import com.qtec.unicom.service.AliasService;
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
import java.util.List;

@Api(value = "别名管理接口",tags = {"别名管理接口"})
@RestController
@RequestMapping(value = "/v1/kms")
public class AliasController {
    private static final Logger logger = LoggerFactory.getLogger(AliasController.class);

    @Autowired
    private AliasService aliasService;

    /**
     * 给主密钥（CMK）创建一个别名
     * @param request
     * @param alias
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","CreateAlias"},logical = Logical.OR)
    @ApiOperation(value = "创建别名",notes = "给主密钥（CMK）创建一个别名")
    @RequestMapping(value = "/CreateAlias", method = RequestMethod.POST)
    @ResponseBody
    public Result createAlias(HttpServletRequest request, @RequestBody Alias alias) throws Exception {
        logger.info("给主密钥（CMK）创建一个别名");
        alias.setCreator(UtilService.getCurrentUserName(request));
        alias.setOwner(UtilService.getCurrentUserName(request));
        aliasService.createAlias(alias);
        return ResultHelper.genResultWithSuccess();
    }

    /**
     * 更新已存在的别名所代表的主密钥
     * @param request
     * @param alias
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","UpdateAlias"},logical = Logical.OR)
    @ApiOperation(value = "更新已存在别名主密钥",notes = "更新已存在的别名所代表的主密钥")
    @RequestMapping(value = "/UpdateAlias", method = RequestMethod.POST)
    @ResponseBody
    public Result updateAlias(HttpServletRequest request, @RequestBody Alias alias) throws Exception {
        logger.info("更新已存在的别名所代表的主密钥");
        aliasService.updateAlias(alias);
        return ResultHelper.genResultWithSuccess();
    }

    /**
     * 30.删除别名
     * @param request
     * @param alias
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","DeleteAlias"},logical = Logical.OR)
    @ApiOperation(value = "删除别名",notes = "删除别名")
    @RequestMapping(value = "/DeleteAlias", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteAlias(HttpServletRequest request, @RequestBody Alias alias) throws Exception {
        logger.info("删除别名");
        aliasService.deleteAlias(alias);
        return ResultHelper.genResultWithSuccess();
    }

    /**
     * 查询当前用户的所有别名
     * @param request
     * @param pageVo
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","ListAliases"},logical = Logical.OR)
    @ApiOperation(value = "查询当前用户的所有别名",notes = "查询当前用户的所有别名")
    @RequestMapping(value = "/ListAliases", method = RequestMethod.POST)
    @ResponseBody
    public Result listAliases(HttpServletRequest request, @RequestBody PageVo pageVo) throws Exception {
        logger.info("查询当前用户的所有别名");
        String userName = UtilService.getCurrentUserName(request);
        Integer pageNumber = pageVo.getPageNumber()==null?0: pageVo.getPageNumber();
        Integer pageSize = pageVo.getPageSize()==null?0: pageVo.getPageSize();
        PageHelper.startPage(pageNumber,pageSize);
        List<Alias> result = aliasService.listAliases(userName);
        PageInfo<Alias> pageInfo = new PageInfo<>(result);
        return ResultHelper.genResultWithSuccess(pageInfo);
    }

    /**
     * 查询所有别名
     * @param request
     * @param pageVo
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","ListAliases1"},logical = Logical.OR)
    @ApiOperation(value = "查询所有别名",notes = "查询所有别名")
    @RequestMapping(value = "/ListAliases1", method = RequestMethod.POST)
    @ResponseBody
    public Result listAliases1(HttpServletRequest request, @RequestBody PageVo pageVo) throws Exception {
        logger.info("查询所有别名");
        Integer pageNumber = pageVo.getPageNumber()==null?0: pageVo.getPageNumber();
        Integer pageSize = pageVo.getPageSize()==null?0: pageVo.getPageSize();
        PageHelper.startPage(pageNumber,pageSize);
        List<Alias> result = aliasService.listAliases(null);
        PageInfo<Alias> pageInfo = new PageInfo<>(result);
        return ResultHelper.genResultWithSuccess(pageInfo);
    }

    /**
     * 查询指定主密钥（CMK）对应的所有别名
     * @param request
     * @param obj
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","ListAliasesByKeyId"},logical = Logical.OR)
    @ApiOperation(value = "查询指定主密钥对应的所有别名",notes = "查询指定主密钥对应的所有别名")
    @RequestMapping(value = "/ListAliasesByKeyId", method = RequestMethod.POST)
    @ResponseBody
    public Result listAliasesByKeyId(HttpServletRequest request, @RequestBody JSONObject obj) throws Exception {
        PageVo pageVo = new PageVo();
        BeanUtils.copyProperties(pageVo, obj);
        Alias alias = new Alias();
        BeanUtils.copyProperties(alias, obj);
        logger.info("查询指定主密钥（CMK）对应的所有别名");
        Integer pageNumber = pageVo.getPageNumber()==null?0: pageVo.getPageNumber();
        Integer pageSize = pageVo.getPageSize()==null?0: pageVo.getPageSize();
        PageHelper.startPage(pageNumber,pageSize);
        List<Alias> result = aliasService.listAliasesByKeyId(alias);
        PageInfo<Alias> pageInfo = new PageInfo<>(result);
        return ResultHelper.genResultWithSuccess(pageInfo);
    }
}





