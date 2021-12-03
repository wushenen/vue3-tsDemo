package com.qtec.unicom.mapper;

import com.qtec.unicom.pojo.AppSecret;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AppSecretMapper {

    @Insert("insert into t_app_secret(user_name,app_key,app_secret) " +
            "values(#{appSecret.userName},#{appSecret.appKey},#{appSecret.appSecret})")
    int addAppSecret(@Param("appSecret") AppSecret appSecret);
    @Select("select user_name,app_key,app_secret,create_time " +
            "from t_app_secret where app_key = #{appSecret.appKey} ")
    AppSecret getAppSecret(@Param("appSecret") AppSecret appSecret);
    @Select("select user_name,app_key,app_secret,create_time " +
            "from t_app_secret where user_name = #{appSecret.userName}")
    AppSecret getAppSecretByUserName(@Param("appSecret") AppSecret appSecret);

    @Delete("delete from t_app_secret where binary user_name= #{userName}")
    int delAppKey(@Param("userName") String userName);
}




