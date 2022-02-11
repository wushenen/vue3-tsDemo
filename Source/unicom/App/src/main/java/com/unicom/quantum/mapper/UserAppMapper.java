package com.unicom.quantum.mapper;

import com.unicom.quantum.pojo.App;
import com.unicom.quantum.pojo.dto.CurrentAppManager;
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
