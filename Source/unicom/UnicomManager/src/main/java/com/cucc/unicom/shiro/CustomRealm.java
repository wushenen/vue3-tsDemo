package com.cucc.unicom.shiro;

import com.cucc.unicom.component.util.JWTUtil;
import com.cucc.unicom.pojo.ApiResource;
import com.cucc.unicom.pojo.DTO.RoleAuthInfo;
import com.cucc.unicom.pojo.DeviceUser;
import com.cucc.unicom.pojo.User;
import com.cucc.unicom.service.*;
import com.cucc.unicom.pojo.AppUser;
import com.cucc.unicom.pojo.DTO.GroupAuthInfo;
import com.qtec.unicom.service.*;
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
    private AppUserService appUserService;
    @Autowired
    private ShiroAuthService shiroAuthService;

    @Autowired
    private RoleAuthService roleAuthService;
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
        System.out.println("--------权限判断--------");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        ShiroUser userInfo = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        int userId;
        List<Integer> roleIds = null;
        List<Integer> groupIds = null;
        List<RoleAuthInfo> roleAuth = null;
        List<GroupAuthInfo> groupAuth = null;
        List<Integer> strategyIdByUserId = null;
        HashSet<Integer> apiIdSet = new HashSet<>();

        if (UserType.SYSTEM_USER.getUserType().equals(userInfo.getUserType())){
            info.addRole("systemUser");
            info.addStringPermission("/**");
            return info;
        }else if (UserType.APP_USER.getUserType().equals(userInfo.getUserType())) {
            info.addRole("appUser");
            //查询该角色的所有权限
            AppUser appUser = (AppUser) userInfo.getUser();
            //通过用户名获取id
            AppUser appUserInfo = null;
            try {
                appUserInfo = appUserService.getAppUser(appUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (appUserInfo != null) {
                userId = Integer.valueOf(appUser.getUserId());
                roleIds = shiroAuthService.getRoleInfosByAppUserId(userId);
                groupIds = shiroAuthService.getGroupInfosByAppUserId(userId);
                strategyIdByUserId = shiroAuthService.getStrategyIdByAppUserId(userId);
            }
        }else if (UserType.DEVICE_USER.getUserType().equals(userInfo.getUserType())){
            info.addRole("deviceUser");
            DeviceUser deviceUser = (DeviceUser) userInfo.getUser();
            userId = deviceUser.getDeviceId();
            roleIds = shiroAuthService.getRoleInfosByDeviceId(userId);
            groupIds = shiroAuthService.getGroupInfosByDeviceId(userId);
            strategyIdByUserId = shiroAuthService.getStrategyIdByDeviceId(userId);

        }
        if (strategyIdByUserId != null && !strategyIdByUserId.isEmpty()){
            for (Integer strategyId : strategyIdByUserId) {
                List<Integer> apiId = shiroAuthService.getApiIdByStrategyId(strategyId);
                apiIdSet.addAll(apiId);
            }
        }
        //根据role获取权限
        if (roleIds != null && !roleIds.isEmpty()){
            for (Integer roleId : roleIds) {
                roleAuth = roleAuthService.getRoleAuth(roleId);
                List<Integer> strategyIds = shiroAuthService.getStrategyIdByRoleId(roleId);
                for (RoleAuthInfo roleAuthInfo : roleAuth) {
                    apiIdSet.add(roleAuthInfo.getApiId());
                }
                for (Integer strategyId : strategyIds) {
                    List<Integer> apiId = shiroAuthService.getApiIdByStrategyId(strategyId);
                    apiIdSet.addAll(apiId);
                }
                /*for (RoleAuthInfo roleAuthInfo : roleAuth) {
                    //此处可将api信息放置内存中获取
                    ApiResource resourceInfo = apiResourceService.getResourceInfo(roleAuthInfo.getApiId());
                    info.addStringPermission(resourceInfo.getApiURL());
                }*/
            }
        }
        if (groupIds != null && !groupIds.isEmpty()){
            for (Integer groupId : groupIds) {
                groupAuth = groupAuthService.getGroupAuth(groupId);
                List<Integer> strategyIds = shiroAuthService.getStrategyIdByGroupId(groupId);
                for (GroupAuthInfo groupAuthInfo : groupAuth) {
                    apiIdSet.add(groupAuthInfo.getApiId());
                }
                for (Integer strategyId : strategyIds) {
                    List<Integer> apiId = shiroAuthService.getApiIdByStrategyId(strategyId);
                    apiIdSet.addAll(apiId);
                }
                /*for (GroupAuthInfo groupAuthInfo : groupAuth) {
                    //此处可将api信息放置内存中获取
                    ApiResource resourceInfo = apiResourceService.getResourceInfo(groupAuthInfo.getApiId());
                    info.addStringPermission(resourceInfo.getApiURL());
                }*/
            }
        }
        if (apiIdSet.size() > 0){
            for (Integer apiId : apiIdSet) {
                ApiResource resourceInfo = apiResourceService.getResourceInfo(apiId);
                info.addStringPermission(resourceInfo.getApiURL());
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

        System.out.println("----------身份认证---------");

        MyUserNamePasswordToken userNamePasswordToken = (MyUserNamePasswordToken) token;
        String userToken = userNamePasswordToken.getUsername();
        String userType = userNamePasswordToken.getUserType();
        String username = null;
        User systemUser = null;
        AppUser appUser = null;
        DeviceUser deviceUser = null;
        ShiroUser user = new ShiroUser();
        try {
            username = JWTUtil.getUsername(userNamePasswordToken.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            throw  new AuthenticationException("用户不存在");
        }

        if (UserType.SYSTEM_USER.getUserType().equals(userType)){
            try {
                systemUser = loginService.systemUserLogin(username);
            } catch (Exception e) {
                throw new AuthenticationException("用户不存在！");
            }
            if (systemUser == null || !JWTUtil.verifyToken(userToken, username)) {
                throw new AuthenticationException("token认证失败！");
            }
            user.setUser(systemUser);
        }
        if (UserType.APP_USER.getUserType().equals(userType)){
            try {
                appUser = loginService.verifyAppUser(username);
            } catch (Exception e) {
                throw new AuthenticationException("用户不存在！");
            }
            if (appUser == null || !JWTUtil.verifyToken(userToken, username)) {
                throw new AuthenticationException("token认证失败！");
            }
            user.setUser(appUser);
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
