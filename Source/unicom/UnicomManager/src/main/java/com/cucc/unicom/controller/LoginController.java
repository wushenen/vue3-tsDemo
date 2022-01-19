package com.cucc.unicom.controller;

import com.alibaba.fastjson.JSONObject;
import com.cucc.unicom.component.Result;
import com.cucc.unicom.component.ResultHelper;
import com.cucc.unicom.component.util.JWTUtil;
import com.cucc.unicom.component.util.UtilService;
import com.cucc.unicom.pojo.User;
import com.cucc.unicom.service.LoginService;
import com.cucc.unicom.shiro.MyUserNamePasswordToken;
import com.cucc.unicom.shiro.ShiroUser;
import com.cucc.unicom.controller.vo.SystemUserLoginRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@Api(tags = {"登录接口"})
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private SessionDAO sessionDAO;

    @Autowired
    private LoginService loginService;

    @Autowired
    private UtilService utilService;

    @ApiOperation(value = "系统用户登录",notes = "系统用户登录")
    @ResponseBody
    @RequestMapping(value = "/sysLogin",method = RequestMethod.POST)
    public Result unicomSysLogin(@RequestBody SystemUserLoginRequest systemUserLoginRequest)  {
        User login = loginService.systemUserLogin(systemUserLoginRequest.getUserName());
        if (login == null)
            return ResultHelper.genResult(1,"用户不存在");
        if (utilService.decryptCBCWithPadding(login.getPassword(),UtilService.SM4KEY).equals(systemUserLoginRequest.getPassword())){
            String jwtToken = JWTUtil.generateToken(systemUserLoginRequest.getUserName());
            UsernamePasswordToken token = new MyUserNamePasswordToken(jwtToken,jwtToken,"systemUser");
            Subject subject = SecurityUtils.getSubject();
            try {
                subject.logout();
                subject.login(token);
                Collection<Session> sessions = sessionDAO.getActiveSessions();
                for (Session session1 : sessions) {
                    if (systemUserLoginRequest.getUserName().equals(session1.getAttribute("loginedUser"))) {
                        session1.setTimeout(0);
                    }
                }
                ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
                User userInfo = (User) user.getUser();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("token", jwtToken);
                jsonObject.put("accountType", userInfo.getAccountType());
                jsonObject.put("userId", userInfo.getId());
                jsonObject.put("user", systemUserLoginRequest.getUserName());
                subject.getSession().setAttribute("loginedUser", systemUserLoginRequest.getUserName());
                return ResultHelper.genResultWithSuccess(jsonObject);
            } catch (IncorrectCredentialsException e) {
                return ResultHelper.genResult(1,"密码校验失败");
            }  catch (AuthenticationException e) {
                return ResultHelper.genResult(1,"该用户不存在");
            } catch (Exception e) {
                return ResultHelper.genResult(1,e.getMessage());
            }
        }else{
            return ResultHelper.genResult(1,"用户名或密码不匹配");
        }
    }

    @ApiOperation(value = "注销",notes = "用户注销登录")
    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Result unicomLogout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null){
            subject.logout();
            SecurityUtils.getSecurityManager().logout(subject);
        }
        return ResultHelper.genResultWithSuccess();
    }

    @ApiOperation(value = "无权限",notes = "无权限")
    @ResponseBody
    @RequestMapping(value = "/noPerm", method = RequestMethod.GET)
    public Result unicomNoPerm() {
        return ResultHelper.genResult(403,"no perms");
    }


}
