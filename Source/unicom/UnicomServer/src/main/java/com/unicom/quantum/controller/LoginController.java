package com.unicom.quantum.controller;

import com.alibaba.fastjson.JSONObject;
import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.component.util.JWTUtil;
import com.unicom.quantum.component.util.SM3Util;
import com.unicom.quantum.controller.vo.AppLoginRequest;
import com.unicom.quantum.controller.vo.DeviceUserLoginRequest;
import com.unicom.quantum.pojo.App;
import com.unicom.quantum.pojo.DeviceUser;
import com.unicom.quantum.service.LoginService;
import com.unicom.quantum.shiro.MyUserNamePasswordToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;


@Api(tags = "登录接口")
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private SessionDAO sessionDAO;
    @Autowired
    private LoginService loginService;

    @ApiOperation(value = "终端用户登录",notes = "终端用户登录")
    @PostMapping(value = "/deviceLogin")
    @ResponseBody
    public Result unicomDeviceLogin(@RequestBody DeviceUserLoginRequest deviceUserLoginRequest) throws Exception {
        boolean tag = false;
        String deviceName = StringUtils.newStringUtf8(Base64.decodeBase64(deviceUserLoginRequest.getDeviceName()));
        byte[] hmac1 = Base64.decodeBase64(deviceUserLoginRequest.getPassword());
        DeviceUser deviceUser = loginService.deviceUserLogin(deviceName);
        if (deviceUser !=null){
            if (deviceUser.getUserType() ==1) {
                byte[] hash = SM3Util.hash(deviceUser.getPassword().getBytes());
                byte[] hmac = SM3Util.hmac(deviceUser.getEncKey(), hash);
                tag = Arrays.equals(hmac, hmac1);
            }
            if (deviceUser.getUserType() == 0)
                tag = Arrays.equals(deviceUser.getPassword().getBytes(),Base64.decodeBase64(deviceUserLoginRequest.getPassword()));
            if (tag){
                String token = JWTUtil.generateTokenWithId(deviceUser.getDeviceId(),deviceUser.getDeviceName());
                MyUserNamePasswordToken userToken = new MyUserNamePasswordToken(token, token,"deviceUser");
                Subject subject = SecurityUtils.getSubject();
                try {
                    subject.logout();
                    subject.login(userToken);
                    Collection<Session> sessions = sessionDAO.getActiveSessions();
                    for (Session session : sessions) {
                        if (deviceName.equals(session.getAttribute("loginedUser"))) {
                            session.setTimeout(0);
                        }
                    }
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("token", token);
                    subject.getSession().setAttribute("loginedUser", deviceName);
                    return ResultHelper.genResultWithSuccess(jsonObject);
                } catch (UnknownAccountException e){
                    return ResultHelper.genResult(1,"用户名不存在");
                } catch (IncorrectCredentialsException e) {
                    return ResultHelper.genResult(1, "密码错误");
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResultHelper.genResult(1, e.getMessage());
                }
            }else
                return ResultHelper.genResult(1,"用户名或密码错误");
        }else
            return ResultHelper.genResult(1,"用户不存在");
    }

    @ApiOperation(value = "应用登录",notes = "应用登录")
    @PostMapping(value = "/appLogin")
    @ResponseBody
    public Result unicomAppLogin(@RequestBody AppLoginRequest appLoginRequest) throws Exception {
        String appKey = StringUtils.newStringUtf8(Base64.decodeBase64(appLoginRequest.getAppKey()));
        App appLogin = loginService.appLogin(appKey);
        if (appLogin.getAppSecret().equals(StringUtils.newStringUtf8(Base64.decodeBase64(appLoginRequest.getAppSecret())))) {
            String token = JWTUtil.generateToken(appLogin.getAppKey());
            UsernamePasswordToken userToken = new MyUserNamePasswordToken(token, token,"appUser");
            Subject subject = SecurityUtils.getSubject();
            try {
                subject.logout();
                subject.login(userToken);
                Collection<Session> sessions = sessionDAO.getActiveSessions();
                for (Session session : sessions) {
                    if (appLogin.getAppKey().equals(session.getAttribute("loginedUser"))) {
                        session.setTimeout(0);
                    }
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("token", token);
                subject.getSession().setAttribute("loginedUser", appLogin.getAppKey());
                return ResultHelper.genResultWithSuccess(jsonObject);
            } catch (UnknownAccountException e){
                return ResultHelper.genResult(1,"用户名不存在");
            } catch (IncorrectCredentialsException e) {
                return ResultHelper.genResult(1, "密码错误");
            } catch (Exception e) {
                e.printStackTrace();
                return ResultHelper.genResult(1, e.getMessage());
            }
        }else
            return ResultHelper.genResult(1,"用户名或密码错误");

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
