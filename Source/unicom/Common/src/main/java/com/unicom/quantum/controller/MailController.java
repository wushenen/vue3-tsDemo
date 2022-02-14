package com.unicom.quantum.controller;

import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.pojo.MailConfigInfo;
import com.unicom.quantum.pojo.MailLog;
import com.unicom.quantum.service.MailLogService;
import com.unicom.quantum.service.MailService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.unicom.quantum.controller.vo.MailConfigRequest;
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
    @GetMapping(value = "getMailLogs/{offset}/{pageSize}")
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
    @GetMapping(value = "getMailConfig")
    @ResponseBody
    public Result unicomGetMailConfig() {
        MailConfigInfo mailSenderConfig = mailService.getMailSenderConfig();
        return ResultHelper.genResultWithSuccess(mailSenderConfig);
    }

    @RequiresRoles("systemUser")
    @ApiOperation("修改邮件发件人配置信息")
    @PostMapping(value = "updateMailConfig")
    @ResponseBody
    public Result unicomUpdateMailConfig(@RequestBody MailConfigRequest mailConfigRequest) {
        MailConfigInfo mailConfigInfo = new MailConfigInfo();
        BeanUtils.copyProperties(mailConfigRequest,mailConfigInfo);
        System.out.println("mailConfigInfo = " + mailConfigInfo);
        mailService.updateMailSenderConfig(mailConfigInfo);
        return ResultHelper.genResultWithSuccess();
    }

}
