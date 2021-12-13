package com.qtec.unicom.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qtec.unicom.component.Result;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.controller.vo.AddStrategyActionRequest;
import com.qtec.unicom.controller.vo.AddStrategyRequest;
import com.qtec.unicom.controller.vo.UpdateStrategyRequest;
import com.qtec.unicom.pojo.DTO.StrategyActionInfo;
import com.qtec.unicom.pojo.Strategy;
import com.qtec.unicom.service.StrategyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "策略管理接口",tags = {"策略管理接口"})
@RestController
@RequestMapping("/strategy")
public class StrategyController {

    @Autowired
    private StrategyService strategyService;


    @ApiOperation(value = "添加策略", notes = "添加策略")
    @RequestMapping(value = "/addStrategy",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomAddStrategy(@RequestBody AddStrategyRequest addStrategyRequest){
        if (!strategyService.strategyNameExist(addStrategyRequest.getStrategyName())) {
            int res = strategyService.addStrategy(addStrategyRequest.getStrategyName(), 1, addStrategyRequest.getStrategyDescribe());
            if (res == 1)
                return ResultHelper.genResultWithSuccess();
            else return ResultHelper.genResult(1,"添加策略失败");
        }else
            return ResultHelper.genResult(1,"策略名称重复");
    }


    @ApiOperation(value = "删除策略", notes = "删除策略信息")
    @RequestMapping(value = "/deleteStrategy",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomDeleteStrategy(@RequestParam("strategyId") int strategyId){
        int i = strategyService.delStrategy(strategyId);
        if (1 == i)
            return ResultHelper.genResultWithSuccess();
        else
            return ResultHelper.genResult(1,"策略信息删除失败");
    }


    @ApiOperation(value = "修改策略", notes = "修改策略信息")
    @RequestMapping(value = "/updateStrategy",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomUpdateStrategy(@RequestBody UpdateStrategyRequest updateStrategyRequest){
        boolean exist = strategyService.strategyNameExist(updateStrategyRequest.getStrategyName());
        boolean equals = strategyService.getStrategyInfo(updateStrategyRequest.getStrategyId()).getStrategyName().equals(updateStrategyRequest.getStrategyName());

        if (!exist || equals){
            int i = strategyService.updateStrategy(updateStrategyRequest.getStrategyId(), updateStrategyRequest.getStrategyName(), updateStrategyRequest.getStrategyDescribe());
            if (1 == i)
                return ResultHelper.genResultWithSuccess();
            else
                return ResultHelper.genResult(1,"修改策略信息失败");
        }else
            return ResultHelper.genResult(1,"策略名称重复");
    }


    @ApiOperation(value = "获取所有策略信息", notes = "获取所有策略信息")
    @RequestMapping(value = "/getStrategy/{offset}/{pageSize}",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomGetStrategy(HttpServletRequest request, @PathVariable("offset") int offset,
                              @PathVariable("pageSize") int pageSize) throws Exception {
        request.setCharacterEncoding("utf-8");
        if (pageSize > 50)
            return ResultHelper.genResult(1,"pageSize过大");
        PageHelper.startPage(offset,pageSize);
        List<Strategy> strategyList = strategyService.getStrategyList();
        PageInfo<Strategy> info = new PageInfo<>(strategyList);
        return ResultHelper.genResultWithSuccess(info);
    }


    @ApiOperation(value = "添加策略操作", notes = "添加策略操作")
    @RequestMapping(value = "/addAction",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomAddAction(@RequestBody AddStrategyActionRequest addStrategyActionRequest){
        for (Integer apiId : addStrategyActionRequest.getApiId()) {
            strategyService.addStrategyAction(addStrategyActionRequest.getStrategyId(),apiId);
        }
        return ResultHelper.genResultWithSuccess();
    }


    @ApiOperation(value = "删除策略操作", notes = "删除策略操作")
    @RequestMapping(value = "/deleteAction",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomDeleteAction(@RequestParam("strategyActionId") int strategyActionId){
        int i = strategyService.delStrategyAction(strategyActionId);
        if (1 == i)
            return ResultHelper.genResultWithSuccess();
        else
            return ResultHelper.genResult(1,"策略操作删除失败");
    }


    @ApiOperation(value = "获取指定策略操作信息", notes = "获取指定策略操作信息")
    @RequestMapping(value = "/getActionInfo",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomGetActionInfo(@RequestParam("strategyId") int strategyId){
        List<StrategyActionInfo> strategyActionInfo = strategyService.getStrategyActionInfo(strategyId);
        return ResultHelper.genResultWithSuccess(strategyActionInfo);
    }

}