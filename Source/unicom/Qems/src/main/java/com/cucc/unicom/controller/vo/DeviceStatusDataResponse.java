package com.cucc.unicom.controller.vo;

import com.cucc.unicom.pojo.DTO.DeviceKeyInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@ApiModel("获取汇总信息返回结果")
@Data
@Accessors(chain = true)
public class DeviceStatusDataResponse {

    @ApiModelProperty("在线数量")
    private int onlineNum;

    @ApiModelProperty("设备总数量")
    private int deviceNum;

    @ApiModelProperty("离线数量")
    private int offlineNum;

    @ApiModelProperty("平台密钥生产总量")
    private Long keyGenNum;

    @ApiModelProperty("在线密钥生产总量")
    private Long onlineKeyNum;
    @ApiModelProperty("离线密钥生产总量")
    private Long offlineKeyNum;

    @ApiModelProperty("密钥使用总量")
    private Long keyUseNum;

    @ApiModelProperty("加密数据量")
    private Long encDataNum;

    @ApiModelProperty("解密数据量")
    private Long decDataNum;

//    @ApiModelProperty("平均密钥更新频率")
//    private int avgFre;
//
//    @ApiModelProperty("最大密钥更新频率")
//    private int maxFre;
//
//    @ApiModelProperty("最小密钥更新频率")
//    private int minFre;

//    @ApiModelProperty("近五日密钥生产总量")
//    private Long keyGenNum2;
//
//    @ApiModelProperty("近五日密钥使用总量")
//    private Long keyUseNum2;
//
//    @ApiModelProperty("近五日加密数据量")
//    private Long encDataNum2;
//
//    @ApiModelProperty("近五日解密数据量")
//    private Long decDataNum2;

//    @ApiModelProperty("近五日平均密钥更新频率")
//    private int avgFre2;
//
//    @ApiModelProperty("近五日最大密钥更新频率")
//    private int maxFre2;
//
//    @ApiModelProperty("近五日最小密钥更新频率")
//    private int minFre2;
    @ApiModelProperty("近5天密钥使用量")
    List<DeviceKeyInfo> everyDayKeyNum;
    @ApiModelProperty("近24小时密钥使用量")
    Long oneDayKeyNum;

}
