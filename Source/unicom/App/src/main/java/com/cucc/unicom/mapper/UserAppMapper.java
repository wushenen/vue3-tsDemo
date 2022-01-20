package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.App;
import com.cucc.unicom.pojo.dto.CurrentAppManager;
import com.cucc.unicom.pojo.dto.CurrentManagerApp;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserAppMapper {
    int addUserApp(int appId,int userId);
    boolean userAppExist(int appId,int userId);
    int deleteUserApp(int appId,int userId);
    int deleteUserByAppId(int appId);
    List<CurrentAppManager> getCurrentAppManager(int appId);
    List<App> getCurrentManagerApp(int userId);
}
