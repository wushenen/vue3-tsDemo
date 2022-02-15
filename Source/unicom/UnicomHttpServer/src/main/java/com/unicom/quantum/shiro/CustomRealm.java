package com.unicom.quantum.shiro;

import com.unicom.quantum.service.LoginService;
import com.unicom.quantum.component.util.JWTUtil;
import com.unicom.quantum.pojo.DeviceUser;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;


public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private LoginService loginService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken userNamePasswordToken = (UsernamePasswordToken) token;
        String userToken = userNamePasswordToken.getUsername();
        String username = null;
        DeviceUser deviceUser = null;
        try {
            username = JWTUtil.getUsername(userNamePasswordToken.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            throw  new AuthenticationException("用户不存在");
        }
        try {
            deviceUser = loginService.deviceUserLogin(username);
        } catch (Exception e) {
            throw new AuthenticationException("用户不存在！");
        }
        if (deviceUser == null || !JWTUtil.verifyToken(userToken, username)) {
            throw new AuthenticationException("token认证失败！");
        }
        return new SimpleAuthenticationInfo(deviceUser,userToken, getName());
    }
}
