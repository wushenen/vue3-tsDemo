package com.unicom.quantum.mapper;

import com.unicom.quantum.pojo.MailConfigInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MailMapper {

    MailConfigInfo getMailSenderConfig();

    void updateMailSenderConfig(MailConfigInfo mailConfigInfo);

}
