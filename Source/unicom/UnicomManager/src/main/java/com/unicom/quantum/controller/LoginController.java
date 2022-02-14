package com.unicom.quantum.controller;

import com.alibaba.fastjson.JSONObject;
import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.component.util.*;
import com.unicom.quantum.service.LoginService;
import com.unicom.quantum.controller.vo.SystemUserLoginRequest;
import com.unicom.quantum.pojo.User;
import com.unicom.quantum.shiro.MyUserNamePasswordToken;
import com.unicom.quantum.shiro.ShiroUser;
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
    @PostMapping(value = "/sysLogin")
    public Result unicomSysLogin(@RequestBody SystemUserLoginRequest systemUserLoginRequest)  {
        User login = loginService.systemUserLogin(systemUserLoginRequest.getUserName());
        if (login == null)
            return ResultHelper.genResult(1,"用户不存在");
        String s = HexUtils.bytesToHexString(SM3Util.hash(SM4Util.byteMerger(utilService.decryptCBCWithPadding(login.getPassword(), UtilService.SM4KEY).getBytes(),systemUserLoginRequest.getUserName().getBytes())));
        if (systemUserLoginRequest.getPassword().equalsIgnoreCase(s)){
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
    @PostMapping(value = "/logout")
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
    @GetMapping(value = "/noPerm")
    public Result unicomNoPerm() {
        return ResultHelper.genResult(403,"no perms");
    }


}
