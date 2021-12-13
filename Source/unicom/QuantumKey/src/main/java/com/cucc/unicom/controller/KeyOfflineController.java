package com.cucc.unicom.controller;

import com.cucc.unicom.pojo.DTO.KeyOfflineDTO;
import com.cucc.unicom.component.Result;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.controller.vo.OfflineKeyRequest;
import com.cucc.unicom.service.KeyOfflineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"离线充注密钥接口"})
@RequestMapping("/offline")
@RestController
public class KeyOfflineController {

    @Autowired
    private KeyOfflineService keyOfflineService;

    @RequiresRoles("deviceUser")
    @ApiOperation("充注离线密钥")
    @RequestMapping(value = "/getOfflineKey",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomGetOfflineKey(@RequestBody OfflineKeyRequest offlineKeyRequest) throws Exception {
        if (offlineKeyRequest.getEndIndex() - offlineKeyRequest.getStartIndex() > 2000)
            return ResultHelper.genResult(1,"充注密钥数不得大于2000");
        if (offlineKeyRequest.getEndIndex() < offlineKeyRequest.getStartIndex())
            return ResultHelper.genResult(1,"结束值不得小于开始值");
        List<KeyOfflineDTO> offKey = keyOfflineService.getOffKey(offlineKeyRequest.getStartIndex(), offlineKeyRequest.getEndIndex());
        return ResultHelper.genResultWithSuccess(offKey);
    }

}
