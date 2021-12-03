package com.qtec.unicom.mapper;

import com.qtec.unicom.pojo.Secret;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface SecretMapper {

    int createSecret(@Param("secret") Secret secret);

    int deleteSecrets(@Param("secret") Secret secret);
    int deleteSecretsTrue(@Param("secret") Secret secret);

    Secret describeSecret(@Param("secret") Secret secret);

    Secret getSecretValue(@Param("secret") Secret secret);

    int updateSecret(@Param("secret") Secret secret);

    int restoreSecret(@Param("secret") Secret secret);

    List<Secret> listSecretVersionIds(@Param("secret") Secret secret);

    List<Secret> listSecrets(@Param("creator") String creator);

    Secret getSecretByVersionId(@Param("secret") Secret secret);
    int putSecretValue(@Param("secret") Secret secret);

    //定时删除
    int autoDeleteSecret(@Param("now") Date now);

}
