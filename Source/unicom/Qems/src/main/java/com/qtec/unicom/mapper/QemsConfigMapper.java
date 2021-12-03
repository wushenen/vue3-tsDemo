package com.qtec.unicom.mapper;

import com.qtec.unicom.controller.vo.UpdateQemsConfigRequest;
import com.qtec.unicom.pojo.DeviceOperation;
import com.qtec.unicom.pojo.QemsConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface QemsConfigMapper {

    /*获取配置信息*/
    QemsConfig getQemsConfig();

    int updateQemsConfig(UpdateQemsConfigRequest updateQemsConfigRequest);

    //控制设备重启或置零
    int addQemsOperation(String deviceName, int operation);

    int countQemsOperation(String deviceName);

    DeviceOperation getOperation(String deviceName);

    int delQemsOperation(String deviceName);

}
