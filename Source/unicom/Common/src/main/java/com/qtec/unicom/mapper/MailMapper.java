package com.qtec.unicom.mapper;

import com.qtec.unicom.pojo.MailConfigInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MailMapper {

    MailConfigInfo getMailSenderConfig();

    void updateMailSenderConfig(MailConfigInfo mailConfigInfo);

}
