package com.cucc.unicom.shiro;

import com.cucc.unicom.component.util.JWTUtil;
import com.cucc.unicom.pojo.ApiResource;
import com.cucc.unicom.pojo.App;
import com.cucc.unicom.pojo.DTO.GroupAuthInfo;
import com.cucc.unicom.pojo.DeviceUser;
import com.cucc.unicom.service.ApiResourceService;
import com.cucc.unicom.service.GroupAuthService;
import com.cucc.unicom.service.LoginService;
import com.cucc.unicom.service.ShiroAuthService;
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
    /**
     * 权限校验
     * @param principals
     * @return
     */
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
                        //此处可将api信息放置内存中获取
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

    /**
     * 身份认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
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
            throw  new AuthenticationException("用户不存在");
        }
        if (UserType.APP_USER.getUserType().equals(userType)){
            try {
                 app = loginService.appLogin(username);
            } catch (Exception e) {
                throw new AuthenticationException("用户不存在！");
            }
            if (app == null || !JWTUtil.verifyToken(userToken, username)) {
                throw new AuthenticationException("token认证失败！");
            }
            user.setUser(app);
        }
        if (UserType.DEVICE_USER.getUserType().equals(userType)){
            try {
                deviceUser = loginService.deviceUserLogin(username);
            } catch (Exception e) {
                throw new AuthenticationException("用户不存在！");
            }
            if (deviceUser == null || !JWTUtil.verifyToken(userToken, username)) {
                throw new AuthenticationException("token认证失败！");
            }
            user.setUser(deviceUser);
        }
        user.setUserType(userType);
        return new SimpleAuthenticationInfo(user,userToken, getName());
    }
}
