package com.unicom.quantum.shiro;

import com.unicom.quantum.component.util.JWTUtil;
import com.unicom.quantum.pojo.ApiResource;
import com.unicom.quantum.pojo.App;
import com.unicom.quantum.pojo.DTO.GroupAuthInfo;
import com.unicom.quantum.pojo.DeviceUser;
import com.unicom.quantum.service.ApiResourceService;
import com.unicom.quantum.service.GroupAuthService;
import com.unicom.quantum.service.LoginService;
import com.unicom.quantum.service.ShiroAuthService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;

public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private LoginService loginService;

    @Autowired
    private ShiroAuthService shiroAuthService;

    @Autowired
    private GroupAuthService groupAuthService;

    @Autowired
    private ApiResourceService apiResourceService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        ShiroUser userInfo = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if (UserType.APP_USER.getUserType().equals(userInfo.getUserType())) {
            info.addRole("appUser");
        }else if (UserType.DEVICE_USER.getUserType().equals(userInfo.getUserType())) {
            info.addRole("deviceUser");
            DeviceUser deviceUser = (DeviceUser) userInfo.getUser();
            List<Integer> groups = null;
            List<GroupAuthInfo> groupAuth = null;
            HashSet<Integer> apiIdSet = new HashSet<>();
            int deviceId = deviceUser.getDeviceId();
            groups = shiroAuthService.getGroupInfosByDeviceId(deviceId);
            if (groups != null && !groups.isEmpty()){
                for (Integer groupInfo : groups) {
                    groupAuth = groupAuthService.getGroupAuth(groupInfo);
                    for (GroupAuthInfo groupAuthInfo : groupAuth) {
                        //????????????api???????????????????????????
                        ApiResource resourceInfo = apiResourceService.getResourceInfo(groupAuthInfo.getApiId());
                        info.addStringPermission(resourceInfo.getApiURL());
                    }
                }
            }
            if (apiIdSet.size() > 0) {
                for (Integer apiId : apiIdSet) {
                    ApiResource resourceInfo = apiResourceService.getResourceInfo(apiId);
                    info.addStringPermission(resourceInfo.getApiURL());
                }
            }
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        MyUserNamePasswordToken userNamePasswordToken = (MyUserNamePasswordToken) token;
        String userToken = userNamePasswordToken.getUsername();
        String userType = userNamePasswordToken.getUserType();
        String username = null;
        DeviceUser deviceUser = null;
        App app = null;
        ShiroUser user = new ShiroUser();
        try {
            username = JWTUtil.getUsername(userNamePasswordToken.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            throw  new AuthenticationException("???????????????");
        }
        if (UserType.APP_USER.getUserType().equals(userType)){
            try {
                 app = loginService.appLogin(username);
            } catch (Exception e) {
                throw new AuthenticationException("??????????????????");
            }
            if (app == null || !JWTUtil.verifyToken(userToken, username)) {
                throw new AuthenticationException("token???????????????");
            }
            user.setUser(app);
        }
        if (UserType.DEVICE_USER.getUserType().equals(userType)){
            try {
                deviceUser = loginService.deviceUserLogin(username);
            } catch (Exception e) {
                throw new AuthenticationException("??????????????????");
            }
            if (deviceUser == null || !JWTUtil.verifyToken(userToken, username)) {
                throw new AuthenticationException("token???????????????");
            }
            user.setUser(deviceUser);
        }
        user.setUserType(userType);
        return new SimpleAuthenticationInfo(user,userToken, getName());
    }
}
