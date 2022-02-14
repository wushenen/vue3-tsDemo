package com.unicom.quantum.controller;

import com.unicom.quantum.controller.vo.OfflineKeyRequest;
import com.unicom.quantum.pojo.DTO.KeyOfflineDTO;
import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.service.KeyOfflineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "离线充注密钥接口")
@RequestMapping("/offline")
@RestController
public class KeyOfflineController {

    @Autowired
    private KeyOfflineService keyOfflineService;

    @RequiresRoles("deviceUser")
    @ApiOperation("充注离线密钥")
    @PostMapping(value = "/getOfflineKey")
    @ResponseBody
    public Result unicomGetOfflineKey(@RequestBody OfflineKeyRequest offlineKeyRequest) throws Exception {
        List<KeyOfflineDTO> offKey = keyOfflineService.getOffKey(offlineKeyRequest.getStartIndex(), offlineKeyRequest.getEndIndex());
        return ResultHelper.genResultWithSuccess(offKey);
    }

}
