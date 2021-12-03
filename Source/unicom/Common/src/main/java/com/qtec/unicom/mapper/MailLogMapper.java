package com.qtec.unicom.mapper;

import com.qtec.unicom.pojo.MailLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MailLogMapper {

    int insertMailLog(MailLog mailLog);

    List<MailLog> getMailLogs();

}
