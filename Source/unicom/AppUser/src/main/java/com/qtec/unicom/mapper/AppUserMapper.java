package com.qtec.unicom.mapper;

import com.qtec.unicom.pojo.AppUser;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AppUserMapper {

    @Insert("insert into t_app_user(user_name,login_pass,status,commit_info) " +
            "values(#{appUser.userName},#{appUser.loginPass},#{appUser.status},#{appUser.commitInfo})")
    int addAppUser(@Param("appUser") AppUser appUser);
    /*@Select({"<script>" +
            "select user_id,user_name,login_pass,status,commit_info,update_time " +
            "from t_app_user where 1=1 " +
            "<if test=\"appUser.userName != null\">and user_name like concat(#{appUser.userName},'%') </if>"
            + "</script>"})*/
    @Select({"<script>" +
            "select user_id,a.user_name,login_pass,status,commit_info,update_time,b.app_key,b.app_secret " +
            "from t_app_user a,t_app_secret b where binary a.user_name = binary b.user_name " +
            "<if test=\"appUser.userName != null\">and user_name like concat(#{appUser.userName},'%') </if>" +
            "order by a.update_time desc"
            + "</script>"})
    List<AppUser> listAppUser(@Param("appUser") AppUser appUser);
    @Select("select user_id,user_name,login_pass,status,commit_info,update_time " +
            "from t_app_user where binary user_name= #{appUser.userName}")
    AppUser getAppUser(@Param("appUser") AppUser appUser);
    @Delete("delete from t_app_user where binary user_name= #{appUser.userName}")
    int delUser(@Param("appUser") AppUser appUser);

    /*模糊查询应用用户*/
    @Select("select * from t_app_user where user_name like concat('%',#{appUserName},'%') order by update_time desc")
    List<AppUser> queryAppUser(@Param("appUserName") String appUserName);

    @Select("select user_id,user_name,commit_info from t_app_user where user_id= #{userId}")
    AppUser getAppUserInfo(@Param("userId") Integer userId);

}




