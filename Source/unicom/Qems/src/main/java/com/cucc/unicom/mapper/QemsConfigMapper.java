package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.DeviceOperation;
import com.cucc.unicom.pojo.QemsConfig;
import com.cucc.unicom.controller.vo.UpdateQemsConfigRequest;
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
