package com.qtec.unicom.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qtec.unicom.component.Result;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.mapper.ApiMapper;
import com.qtec.unicom.pojo.*;
import com.qtec.unicom.service.SystemMangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * 系统配置
 */
@Api(value = "系统配置接口",tags = {"系统配置接口"})
@RestController
@RequestMapping(value = "/system")
public class SystemController {
    private static final Logger logger = LoggerFactory.getLogger(SystemController.class);

    @Autowired
    private SystemMangeService systemMangeService;

    @Autowired
    private ApiMapper apiMapper;
    /**
     * 修改本机ip,网关,掩码
     * @param linuxServer
     * @return
     */
    @ApiOperation(value = "修改本机ip,网关,掩码",notes = "修改本机ip,网关,掩码")
    @PostMapping("/updateIpNetmaskAndGateway")
    @ResponseBody
    public Result updateIpNetmarkAndGateway(@RequestBody @Valid LinuxServer linuxServer) throws Exception{
        String result = systemMangeService.updateIpNetmaskAndGateway(linuxServer);
        System.out.println(result);
        if (result.equals("0")) {
            return ResultHelper.genResultWithSuccess();
        } else if (result.equals("1")) {
            return ResultHelper.genResult(1, "ip冲突");
        }
        return ResultHelper.genResult(-1);
    }
    /**
     *获取系统版本信息
     * @return
     */
    @ApiOperation(value = "获取系统版本信息",notes = "获取系统版本信息")
    @GetMapping("/qkmVersion")
    @ResponseBody
    public Result getQkmVersion()  throws Exception{
        QkmVersion qkmVersions = systemMangeService.getQkmVersion();
        return ResultHelper.genResultWithSuccess(qkmVersions);
    }

    /**
     * 初始化
     * @param
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "系统初始化",notes = "系统初始化")
    @PostMapping("/init")
    @ResponseBody
    public Result init() throws Exception{
        String result = systemMangeService.init();
        if (result.equals("0")) {
            return ResultHelper.genResultWithSuccess();
        } else if (result.equals("1")) {
            return ResultHelper.genResult(1, "系统已经初始化");
        }
        return ResultHelper.genResult(-1);
    }

    /**
     * 密码卡备份
     * @param cardData
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "密码卡备份",notes = "密码卡备份")
    @PostMapping("/backUp")
    @ResponseBody
    public Result backUp(@RequestBody CardData cardData) throws Exception{
        systemMangeService.backUp(cardData.getBackPass());
        return ResultHelper.genResultWithSuccess();
    }

    /**
     * 密码卡还原
     * @param cardData
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "密码卡还原",notes = "密码卡还原")
    @PostMapping("/restore")
    @ResponseBody
    public Result restore(@RequestBody CardData cardData) throws Exception{
        String result = systemMangeService.restore(cardData);
        System.out.println(result);
        if (result.equals("0")) {
            return ResultHelper.genResultWithSuccess();
        } else {
            return ResultHelper.genResult(1, result);
        }
    }
    /**
     * 列出所有密码卡备份信息
     * @param cardData
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "列出所有密码卡备份信息",notes = "列出所有密码卡备份信息")
    @PostMapping("/listCardData/{offset}/{pageSize}")
    @ResponseBody
    public Result listCardData(@RequestBody CardData cardData,
                               @PathVariable("offset") int offset,
                               @PathVariable("pageSize") int pageSize) throws Exception{
        if (pageSize > 50) {
            return ResultHelper.genResult(1, "pageSize过大");
        }
        PageHelper.startPage(offset,pageSize);
        List<CardData> result = systemMangeService.listCardData(cardData);
        PageInfo<CardData> pageInfo = new PageInfo<>(result);
        return ResultHelper.genResultWithSuccess(pageInfo);
    }

    @ApiOperation(value = "批量导入终端",notes = "批量导入终端用户信息")
    @RequestMapping(value = "/importApi",method = RequestMethod.POST)
    @ResponseBody
    public Result importDeviceUser(@RequestPart("excelFile") MultipartFile excelFile) throws Exception {
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1);
        List<ImportApiInfoDTO> list = ExcelImportUtil.importExcel(excelFile.getInputStream(), ImportApiInfoDTO.class, importParams);

        for (ImportApiInfoDTO apiInfoDTO : list) {
            apiMapper.add(apiInfoDTO);
        }
        return ResultHelper.genResultWithSuccess();
    }

}




