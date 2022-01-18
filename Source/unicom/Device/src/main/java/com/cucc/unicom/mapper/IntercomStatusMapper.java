package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.IntercomStatus;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IntercomStatusMapper {

    boolean intercomExist(String deviceId);

    int addIntercom(IntercomStatus intercomStatus);

    int updateIntercom(IntercomStatus intercomStatus);

    List<IntercomStatus> getIntercomStatus();

}
