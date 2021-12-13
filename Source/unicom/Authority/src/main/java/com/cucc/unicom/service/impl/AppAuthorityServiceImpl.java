package com.cucc.unicom.service.impl;

import com.cucc.unicom.mapper.AppAuthorityMapper;
import com.cucc.unicom.pojo.DTO.AuthInfo;
import com.cucc.unicom.service.AppAuthorityService;
import com.cucc.unicom.component.annotation.OperateLogAnno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppAuthorityServiceImpl implements AppAuthorityService {

    private final String OPERATE_MODEL = "应用用户授权模块";

    @Autowired
    private AppAuthorityMapper appAuthorityMapper;

    @OperateLogAnno(operateDesc = "添加终端权限", operateModel = OPERATE_MODEL)
    @Override
    public int addAppAuthority(int appUserId, List<Integer> apiIds) {
        for (Integer apiId : apiIds) {
            if (!appAuthorityMapper.appAuthorityIsAdd(appUserId, apiId))
                appAuthorityMapper.addAppAuthority(appUserId, apiId);
        }
        return 0;
    }


    @OperateLogAnno(operateDesc = "删除终端权限", operateModel = OPERATE_MODEL)
    @Override
    public int delAppAuthority(int authId) {
        return appAuthorityMapper.delAppAuthority(authId);
    }

    @OperateLogAnno(operateDesc = "删除终端及所有权限", operateModel = OPERATE_MODEL)
    @Override
    public int delAppAuthByAppUserId(int appUserId) {
        return appAuthorityMapper.delAppAuthByAppUserId(appUserId);
    }

    @OperateLogAnno(operateDesc = "获取指定终端权限", operateModel = OPERATE_MODEL)
    @Override
    public List<AuthInfo> getAppAuthority(int appUserId) {
        return appAuthorityMapper.getAppAuthority(appUserId);
    }
}
