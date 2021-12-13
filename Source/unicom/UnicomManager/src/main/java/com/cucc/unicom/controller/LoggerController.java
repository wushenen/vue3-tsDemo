package com.cucc.unicom.controller;

import com.cucc.unicom.component.Result;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.pojo.OperateLog;
import com.cucc.unicom.service.OperateLogService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "日志管理接口")
@RestController
@RequestMapping(value = "/logger")
public class LoggerController {

    @Autowired
    private OperateLogService operateLogService;
    /**
     * 分页查看操作日志
     *
     * @param request
     * @param offset
     * @param pageSize
     * @return
     * @throws ParseException
     */
    @ApiOperation(value = "获取日志信息",notes = "获取指定页和页面大小的日志信息")
    @RequestMapping(value = "listOperateLogs/{offset}/{pageSize}",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomListOperateLogs(HttpServletRequest request,
                                        @PathVariable("offset") int offset,
                                        @PathVariable("pageSize") int pageSize) throws ParseException {
        if (pageSize > 50) {
            return ResultHelper.genResult(1, "pageSize过大");
        }

        /**
         * 请求参数封装
         */
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String ip = request.getParameter("Ip");
        String operatorName = request.getParameter("operatorName");
        String logInfo = request.getParameter("logInfo");

        Map<String, Object> paramas = new HashMap<>(16);
        if (startTime != null && !startTime.trim().equals("")) {
            String s = startTime + " 00:00:00";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = sdf.parse(s);
            paramas.put("startTime", date);
        }
        if (endTime != null && !endTime.trim().equals("")) {
            String s = endTime + " 23:59:59";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = sdf.parse(s);
            paramas.put("endTime", date);
        }
        if (ip != null && !ip.trim().equals("")) {
            paramas.put("ip", ip);
        }
        if (operatorName != null && !operatorName.trim().equals("")) {
            paramas.put("operatorName", operatorName);
        }
        if (logInfo != null && !logInfo.trim().equals("")) {
            paramas.put("logInfo", logInfo);
        }

        paramas.put("offset", offset);
        paramas.put("pageSize", pageSize);

        PageInfo<OperateLog> pageInfo = operateLogService.getOperateLogs1(paramas);
        return ResultHelper.genResultWithSuccess(pageInfo);
    }
}
