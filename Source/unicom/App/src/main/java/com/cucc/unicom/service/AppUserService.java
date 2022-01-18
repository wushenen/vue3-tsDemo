package com.cucc.unicom.service;


import com.cucc.unicom.pojo.AppUser;

import java.util.List;

/**
 * Created by duhc on 2017/4/5.
 */

public interface AppUserService {

    AppUser getAppUser(AppUser appUser)throws Exception;
    void addAppUser(AppUser appUser) throws Exception;
    List<AppUser> listAppUser(AppUser appUser)throws Exception;

    int delUser(AppUser appUser)throws Exception;

    AppUser describeUser(AppUser appUser)throws Exception;

    List<AppUser> queryAppUser(String appUserName);

    AppUser getAppUserInfo(Integer userId);
}
