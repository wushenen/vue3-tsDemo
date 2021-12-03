package com.qtec.unicom.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qtec.unicom.component.Result;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.component.util.DateUtils;
import com.qtec.unicom.component.util.UtilService;
import com.qtec.unicom.pojo.PageVo;
import com.qtec.unicom.pojo.Secret;
import com.qtec.unicom.service.SecretService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/v1/kms")
@Api(value = "凭据管理接口",tags = {"凭据管理接口"})
public class SecretController {

    private static final Logger logger = LoggerFactory.getLogger(SecretController.class);

    @Autowired
    SecretService secretService;

    /**
     * 创建凭据，并存入凭据的初始版本
     * @param secret
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","CreateSecret"},logical = Logical.OR)
    @ApiOperation(value = "创建凭据",notes = "创建凭据")
    @RequestMapping(value = "/CreateSecret", method = RequestMethod.POST)
    @ResponseBody
    public Result createSecret(@RequestBody Secret secret, HttpServletRequest request) throws Exception{
        logger.info("创建凭据，并存入凭据的初始版本");
        //获取请求头里的token对象
        secret.setCreator(UtilService.getCurrentUserName(request));
        secret = secretService.createSecret(secret);

        JSONObject object = new JSONObject();
        object.put("arn",secret.getArn());
        object.put("secretName",secret.getSecretName());
        object.put("versionId",secret.getVersionId());
        return ResultHelper.genResultWithSuccess(object);
    }


    /**
     * 在计划时间内删除凭证对象
     * 恢复窗口默认为30天
     * @param secret
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","DeleteSecret"},logical = Logical.OR)
    @ApiOperation(value = "在计划时间内删除凭证对象",notes = "在计划时间内删除凭证对象")
    @RequestMapping(value = "/DeleteSecret", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteSecret(@RequestBody Secret secret) throws Exception{
        logger.info("删除凭证对象，可指定回复窗口，也可以强制删除指定的凭据");
        if (!secret.getForceDeleteWithoutRecovery()) {
            Integer recoveryWindowInDays = secret.getRecoveryWindowInDays();
            if (recoveryWindowInDays.intValue() < 0 || recoveryWindowInDays.intValue() > 30) {
                return ResultHelper.genResult(1,"请按规定设置预删除时间！！！");
            }
            if(recoveryWindowInDays==null||recoveryWindowInDays.equals("")){
                secret.setRecoveryWindowInDays(30);
            }
        }
        secret = secretService.deleteSecret(secret);
        JSONObject object = new JSONObject();
        object.put("secretName",secret.getSecretName());
        object.put("plannedDeleteTime",secret.getPlannedDeleteTime());
        return ResultHelper.genResultWithSuccess(object);
    }

    /**
     * 查询凭据的元数据信息
     * @param secret
     * @return
     */
    @RequiresPermissions(value = {"/**","DescribeSecret"},logical = Logical.OR)
    @ApiOperation(value = "查询凭据的元数据信息",notes = "查询凭据的元数据信息")
    @RequestMapping(value = "/DescribeSecret",method = RequestMethod.POST)
    @ResponseBody
    public Result describeSecret(@RequestBody Secret secret) throws Exception{
        logger.info("查询凭据的元数据信息");
        secret = secretService.describeSecret(secret);
        JSONObject object = new JSONObject();
        object.put("arn",secret.getArn());
        object.put("secretName",secret.getSecretName());
        object.put("encryptionKeyId",secret.getEncryptionKeyId());
        object.put("description",secret.getDescription());
        object.put("createTime",secret.getCreateTime());
        object.put("updateTime",secret.getUpdateTime());
        object.put("plannedDeleteTime",secret.getPlannedDeleteTime());
        return ResultHelper.genResultWithSuccess(object);
    }

    /**
     * 获取凭据值
     * @param secret
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","GetSecretValue"},logical = Logical.OR)
    @ApiOperation(value = "获取凭据值",notes = "获取凭据值")
    @RequestMapping(value = "/GetSecretValue",method = RequestMethod.POST)
    @ResponseBody
    public Result getSecretValue(@RequestBody Secret secret) throws Exception{
        logger.info("获取凭据值");
        secret = secretService.getSecretValue(secret);
        JSONObject object = new JSONObject();

        object.put("secretName",secret.getSecretName());
        object.put("versionId",secret.getVersionId());
        object.put("secretData",secret.getSecretData());
        object.put("secretDataType",secret.getSecretDataType());
        object.put("createTime",secret.getCreateTime());
        return ResultHelper.genResultWithSuccess(object);
    }

    /**
     * 更新凭据的元数据
     * @param secret
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","updateSecret"},logical = Logical.OR)
    @ApiOperation(value = "更新凭据的元数据",notes = "更新凭据的元数据")
    @RequestMapping(value = "/UpdateSecret",method = RequestMethod.POST)
    @ResponseBody
    public Result updateSecret(@RequestBody Secret secret) throws Exception{
        logger.info("更新凭据的元数据");
        secret = secretService.updateSecret(secret);
        JSONObject object = new JSONObject();
        object.put("secretName",secret.getSecretName());
        return ResultHelper.genResultWithSuccess(object);
    }

    /**
     * 恢复被删除的凭证
     * @param secret
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","RestoreSecret"},logical = Logical.OR)
    @ApiOperation(value = "恢复被删除的凭证",notes = "恢复被删除的凭证")
    @RequestMapping(value = "/RestoreSecret",method = RequestMethod.POST)
    @ResponseBody
    public Result restoreSecret(@RequestBody Secret secret) throws Exception{
     logger.info("恢复被删除的凭据");
     secret = secretService.restoreSecret(secret);
     JSONObject object = new JSONObject();
     object.put("secretName",secret.getSecretName());
        return ResultHelper.genResultWithSuccess(object);
    }

    @RequiresPermissions(value = {"/**","ListSecretVersionIds"},logical = Logical.OR)
    @ApiOperation(value = "获取密钥版本信息",notes = "获取密钥版本信息")
    @RequestMapping(value = "/ListSecretVersionIds",method = RequestMethod.POST)
    @ResponseBody
    public Result listSecretVersionIds(@RequestBody Secret secret) throws Exception{
        logger.info("查询凭证所有版本信息");
        Integer pageNumber = secret.getPageNumber()==null?0: secret.getPageNumber();
        Integer pageSize = secret.getPageSize()==null?0: secret.getPageSize();
        PageHelper.startPage(pageNumber,pageSize);
        List<Secret> versionIds = secretService.listSecretVersionIds(secret);
        PageInfo<Secret> pageInfo1 = new PageInfo<>(versionIds);

        List<Map<String,String>> list = new ArrayList<>();
        for(int i=0;i<versionIds.size();i++){
            Map map = new HashMap();
            String versionId = versionIds.get(i).getVersionId();
            Date plannedDeleteTime = versionIds.get(i).getPlannedDeleteTime();
            Date createTime = versionIds.get(i).getCreateTime();
            map.put("versionId",versionId);
            map.put("plannedDeleteTime",DateUtils.getZTimeStr(plannedDeleteTime));
            map.put("createTime",DateUtils.getZTimeStr(createTime));
            list.add(map);
        }
        PageInfo<Map<String,String >> pageInfo = new PageInfo<>(list);

        PageVo pageVo = new PageVo();
        pageVo.setPageNumber(pageInfo.getPageNum());
        pageVo.setPageSize(pageInfo.getPageSize());
        pageVo.setTotalCount(Integer.parseInt(pageInfo1.getTotal()+""));

        JSONObject object = (JSONObject)JSONObject.toJSON(pageVo);
        object.put("versionIds",pageInfo.getList());
        object.put("secretName",secret.getSecretName());

        return ResultHelper.genResultWithSuccess(object);
    }

    /**
     * 查询当前用户在当前地域创建的所有凭证
     * @param pageVo
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"/**","ListSecrets"},logical = Logical.OR)
    @ApiOperation(value = "查询当前用户在当前地域创建的所有凭证",notes = "查询当前用户在当前地域创建的所有凭证")
    @RequestMapping(value = "/ListSecrets", method = RequestMethod.POST)
    @ResponseBody
    public Result listSecrets(@RequestBody PageVo pageVo, HttpServletRequest request) throws Exception{
        logger.info("查询当前用户在当前地域创建的所有凭证");
        //通过token获取当前用户
        String creator = UtilService.getCurrentUserName(request);

        Integer pageNumber = pageVo.getPageNumber()==null?0: pageVo.getPageNumber();
        Integer pageSize = pageVo.getPageSize()==null?0: pageVo.getPageSize();
        PageHelper.startPage(pageNumber,pageSize);
        List<Secret> result = secretService.listSecrets(creator);
        PageInfo<Secret> pageInfo1 = new PageInfo<>(result);

        List<Map<String,String>> list = new ArrayList<>();
        for(int i=0;i<result.size();i++){
            Map map = new HashMap();
            String secretName = result.get(i).getSecretName();
            Date createTime = result.get(i).getCreateTime();
            Date updateTime = result.get(i).getUpdateTime();
            Date plannedDeleteTime = result.get(i).getPlannedDeleteTime();
            map.put("secretName",secretName);
            map.put("createTime", DateUtils.getZTimeStr(createTime));
            map.put("updateTime",DateUtils.getZTimeStr(updateTime));
            map.put("plannedDeleteTime",DateUtils.getZTimeStr(plannedDeleteTime));
            list.add(map);
        }
        PageInfo<Map<String,String >> pageInfo = new PageInfo<>(list);

        pageInfo.setTotal(pageInfo1.getTotal());

        return ResultHelper.genResultWithSuccess(pageInfo);
    }

    @RequiresPermissions(value = {"/**","PutSecretValue"},logical = Logical.OR)
    @ApiOperation(value = "为凭据存入一个新版本的凭据值",notes = "为凭据存入一个新版本的凭据值")
    @RequestMapping(value = "/PutSecretValue",method = RequestMethod.POST)
    @ResponseBody
    public Result putSecretValue(HttpServletRequest request, @RequestBody Secret secret) throws Exception{
        logger.info("为凭据存入一个新版本的凭据值");
        //获取请求头里的token对象
        secret.setCreator(UtilService.getCurrentUserName(request));
        secret = secretService.putSecretValue(secret);

        JSONObject object = new JSONObject();
        object.put("secretName",secret.getSecretName());
        object.put("versionId",secret.getVersionId());
        return ResultHelper.genResultWithSuccess(object);
    }

}
