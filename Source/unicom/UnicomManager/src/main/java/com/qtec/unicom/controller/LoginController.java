package com.qtec.unicom.controller;

import com.alibaba.fastjson.JSONObject;
import com.qtec.unicom.component.Result;
import com.qtec.unicom.component.ResultHelper;
import com.qtec.unicom.component.util.JWTUtil;
import com.qtec.unicom.component.util.SM3Util;
import com.qtec.unicom.component.util.UtilService;
import com.qtec.unicom.controller.vo.AppUserLoginRequest;
import com.qtec.unicom.controller.vo.DeviceUserLoginRequest;
import com.qtec.unicom.controller.vo.SystemUserLoginRequest;
import com.qtec.unicom.pojo.AppSecret;
import com.qtec.unicom.pojo.DeviceUser;
import com.qtec.unicom.pojo.User;
import com.qtec.unicom.service.LoginService;
import com.qtec.unicom.shiro.MyUserNamePasswordToken;
import com.qtec.unicom.shiro.ShiroUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
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
    public Result sysLogin(@RequestBody SystemUserLoginRequest systemUserLoginRequest)  {
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


    /*@ApiOperation(value = "应用用户登录",notes = "应用用户登录")
    @RequestMapping(value = "/appUserLogin",method = RequestMethod.POST)
    @ResponseBody
    public Result appUserLogin(@RequestBody AppUserLoginRequest appUserLoginRequest) {

        String appKey = StringUtils.newStringUtf8(Base64.decodeBase64(appUserLoginRequest.getAppKey()));
        String appSecret = StringUtils.newStringUtf8(Base64.decodeBase64(appUserLoginRequest.getAppSecret()));
        AppSecret appUserInfo = loginService.appUserLogin(appKey, appSecret);

        //判断用户成功后，获取用户的权限信息
        if (appUserInfo != null){
            String userName = appUserInfo.getUserName();
            String token = JWTUtil.generateToken(userName);
            UsernamePasswordToken userToken = new MyUserNamePasswordToken(token, token, "appUser");
            Subject subject = SecurityUtils.getSubject();
            try {
                subject.logout();
                subject.login(userToken);
                // 剔除其他此账号在其它地方登录
                // 获取所有的session
                Collection<Session> sessions = sessionDAO.getActiveSessions();
                for (Session session : sessions) {
                    //第二次登录时，把第一个session剔除
                    if (userName.equals(session.getAttribute("loginedUser"))) {
                        session.setTimeout(0);
                    }
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("token", token);
                subject.getSession().setAttribute("loginedUser", userName);
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

    @ApiOperation(value = "终端用户登录",notes = "终端用户登录")
    @RequestMapping(value = "/deviceLogin",method = RequestMethod.POST)
    @ResponseBody
    public Result deviceLogin(@RequestBody DeviceUserLoginRequest deviceUserLoginRequest) {

        String deviceName = StringUtils.newStringUtf8(Base64.decodeBase64(deviceUserLoginRequest.getDeviceName()));
        byte[] hmac1 = Base64.decodeBase64(deviceUserLoginRequest.getPassword());
        DeviceUser deviceUser = loginService.deviceUserLogin(deviceName);
        if (deviceUser !=null){
            byte[] hash = SM3Util.hash(deviceUser.getPassword().getBytes());
            byte[] hmac = SM3Util.hmac(deviceUser.getEncKey(),hash);
            //判断用户成功后，获取用户的权限信息
            if (Arrays.equals(hmac, hmac1)){
                String token = JWTUtil.generateToken(deviceUser.getDeviceName());
                UsernamePasswordToken userToken = new MyUserNamePasswordToken(token, token, "deviceUser");
                Subject subject = SecurityUtils.getSubject();
                try {
                    subject.logout();
                    subject.login(userToken);
                    // 剔除其他此账号在其它地方登录
                    // 获取所有的session
                    Collection<Session> sessions = sessionDAO.getActiveSessions();
                    for (Session session : sessions) {
                        //第二次登录时，把第一个session剔除
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

    }*/



    @ApiOperation(value = "注销",notes = "用户注销登录")
    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Result logout() {
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
    public Result noPerm() {
        return ResultHelper.genResult(403,"no perms");
    }


}
