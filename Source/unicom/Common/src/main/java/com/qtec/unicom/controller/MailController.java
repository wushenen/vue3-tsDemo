package com.qtec.unicom.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qtec.unicom.component.Result;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.controller.vo.MailConfigRequest;
import com.qtec.unicom.pojo.MailLog;
import com.qtec.unicom.pojo.MailConfigInfo;
import com.qtec.unicom.service.MailLogService;
import com.qtec.unicom.service.MailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
