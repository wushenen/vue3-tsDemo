package com.cucc.unicom.controller;

import com.cucc.unicom.component.Result;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.pojo.MailConfigInfo;
import com.cucc.unicom.pojo.MailLog;
import com.cucc.unicom.service.MailLogService;
import com.cucc.unicom.service.MailService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cucc.unicom.controller.vo.MailConfigRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "邮件接口",tags = {"邮件接口"})
@RequestMapping("/mail")
@RestController
public class MailController {


    @Autowired
    private MailLogService mailLogService;

    @Autowired
    private MailService mailService;

    @ApiOperation("获取邮件告警日志")
    @RequestMapping(value = "getMailLogs/{offset}/{pageSize}",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomListOperateLogs(HttpServletRequest request,
                                        @PathVariable("offset") int offset,
                                        @PathVariable("pageSize") int pageSize) throws Exception {
        request.setCharacterEncoding("UTF-8");
        if (pageSize > 50) {
            return ResultHelper.genResult(1, "pageSize过大");
        }
        PageHelper.startPage(offset,pageSize);
        List<MailLog> mailLogs = mailLogService.getMailLogs();
        PageInfo<MailLog> info = new PageInfo<>(mailLogs);
        return ResultHelper.genResultWithSuccess(info);
    }

    @ApiOperation("获取邮件发件人信息")
    @RequestMapping(value = "getMailConfig",method = RequestMethod.GET)
    @ResponseBody
    public Result unicomGetMailConfig() {
        MailConfigInfo mailSenderConfig = mailService.getMailSenderConfig();
        return ResultHelper.genResultWithSuccess(mailSenderConfig);
    }

    @RequiresRoles("systemUser")
    @ApiOperation("修改邮件发件人配置信息")
    @RequestMapping(value = "updateMailConfig",method = RequestMethod.POST)
    @ResponseBody
    public Result unicomUpdateMailConfig(@RequestBody MailConfigRequest mailConfigRequest) {
        MailConfigInfo mailConfigInfo = new MailConfigInfo();
        BeanUtils.copyProperties(mailConfigRequest,mailConfigInfo);
        System.out.println("mailConfigInfo = " + mailConfigInfo);
        mailService.updateMailSenderConfig(mailConfigInfo);
        return ResultHelper.genResultWithSuccess();
    }

}
