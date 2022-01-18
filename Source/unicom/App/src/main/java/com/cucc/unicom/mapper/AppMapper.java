package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.App;
import com.cucc.unicom.pojo.dto.AppBaseInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AppMapper {
    int addApp(App app);
    boolean appExist(String appName);
    int deleteApp(int appId);
    List<App> getApps();
    List<AppBaseInfo> getAppBaseInfo();
}
