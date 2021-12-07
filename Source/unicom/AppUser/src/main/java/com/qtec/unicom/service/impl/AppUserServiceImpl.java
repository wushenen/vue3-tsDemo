package com.qtec.unicom.service.impl;


import com.qtec.unicom.component.annotation.OperateLogAnno;
import com.qtec.unicom.component.util.UtilService;
import com.qtec.unicom.mapper.AppSecretMapper;
import com.qtec.unicom.mapper.AppUserMapper;
import com.qtec.unicom.pojo.AppSecret;
import com.qtec.unicom.pojo.AppUser;
import com.qtec.unicom.service.AppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by duhc on 2017/4/5.
 */
@Service
public class AppUserServiceImpl implements AppUserService {

    private static final Logger logger = LoggerFactory.getLogger(AppUserServiceImpl.class);

    private final String OPERATE_MODEL = "应用用户管理模块";

    @Autowired
    private AppUserMapper appUserMapper;
    @Autowired
    private AppSecretMapper appSecretMapper;
    @Autowired
    private UtilService utilService;

    @OperateLogAnno(operateDesc = "获取应用用户", operateModel = OPERATE_MODEL)
    @Override
    public AppUser getAppUser(AppUser appUser) throws Exception {
        return appUserMapper.getAppUser(appUser);
    }

    @OperateLogAnno(operateDesc = "添加应用用户", operateModel = OPERATE_MODEL)
    @Transactional
    @Override
    public void addAppUser(AppUser appUser) throws Exception {
        appUser.setStatus(1);
        appUser.setLoginPass(utilService.encryptCBCWithPadding(appUser.getLoginPass().toString(),UtilService.SM4KEY));
        int num = appUserMapper.addAppUser(appUser);
        AppSecret appSecret = new AppSecret();
        appSecret.setUserName(appUser.getUserName());
        appSecret.setAppKey(utilService.encryptCBCWithPadding(utilService.createRandomCharData(24),UtilService.SM4KEY));
        appSecret.setAppSecret(utilService.encryptCBCWithPadding(utilService.createRandomCharData(32),UtilService.SM4KEY));
        appSecretMapper.addAppSecret(appSecret);
    }
    @OperateLogAnno(operateDesc = "列出应用用户", operateModel = OPERATE_MODEL)
    @Override
    public List<AppUser> listAppUser(AppUser appUser) throws Exception {
        List<AppUser> list = appUserMapper.listAppUser(appUser);
        for (AppUser user : list) {
            user.setLoginPass(utilService.decryptCBCWithPadding(user.getLoginPass().toString(),UtilService.SM4KEY));
            user.setAppKey(utilService.decryptCBCWithPadding(user.getAppKey(),UtilService.SM4KEY));
            user.setAppSecret(utilService.decryptCBCWithPadding(user.getAppSecret(),UtilService.SM4KEY));
        }
        return list;
    }
    @OperateLogAnno(operateDesc = "删除应用用户", operateModel = OPERATE_MODEL)
    @Override
    public int delUser(AppUser appUser) throws Exception {
        appSecretMapper.delAppKey(appUser.getUserName());
        return appUserMapper.delUser(appUser);
    }
    @OperateLogAnno(operateDesc = "应用用户详情", operateModel = OPERATE_MODEL)
    @Override
    public AppUser describeUser(AppUser appUser) throws Exception {
        AppUser user = appUserMapper.getAppUser(appUser);
        user.setLoginPass(utilService.decryptCBCWithPadding(user.getLoginPass().toString(),UtilService.SM4KEY));
        AppSecret appSecret = new AppSecret();
        appSecret.setUserName(appUser.getUserName());
        AppSecret secret = appSecretMapper.getAppSecretByUserName(appSecret);
        if(secret != null){
            user.setAppKey(utilService.decryptCBCWithPadding(secret.getAppKey(),UtilService.SM4KEY));
            user.setAppSecret(utilService.decryptCBCWithPadding(secret.getAppSecret(),UtilService.SM4KEY));
        }
        return user;
    }


    @Override
    public List<AppUser> queryAppUser(String appUserName) {
        List<AppUser> appUsers = appUserMapper.queryAppUser(appUserName);
        for (AppUser appUser : appUsers) {
            appUser.setLoginPass(utilService.decryptCBCWithPadding(appUser.getLoginPass().toString(),UtilService.SM4KEY));
        }
        return appUsers;
    }

    @Override
    public AppUser getAppUserInfo(Integer userId) {
        return appUserMapper.getAppUserInfo(userId);
    }
}




