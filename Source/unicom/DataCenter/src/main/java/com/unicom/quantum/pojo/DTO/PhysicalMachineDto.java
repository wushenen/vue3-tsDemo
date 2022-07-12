package com.unicom.quantum.pojo.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 物理机信息
 * @author lenovo
 */
@Data
@ApiModel("物理机信息")
public class PhysicalMachineDto {

    @ApiModelProperty("物理机CPU")
    private RateDto cpuLoadRate;

    @ApiModelProperty("物理机内存")
    private RateDto ramLoadRate;

    @ApiModelProperty("总物理机磁盘IO")
    private SendRecDto diskIo;

    @ApiModelProperty("总物理机网络吞吐量")
    private SendRecDto netTps;

    public PhysicalMachineDto() {
        setCpuLoadRate(new RateDto());
        setRamLoadRate(new RateDto());
        setDiskIo(new SendRecDto());
        setNetTps(new SendRecDto());
    }

    @Data
    public static class RateDto {

        @ApiModelProperty("时间")
        private List<String> times;

        @ApiModelProperty("总负载率")
        private List<String> rates;

        public RateDto() {
            setTimes(new ArrayList<>());
            setRates(new ArrayList<>());
        }
    }

    @Data
    public static class SendRecDto {

        @ApiModelProperty("时间")
        private List<String> times;

        @ApiModelProperty("发送次数")
        private List<String> sendTimes;

        @ApiModelProperty("接收次数")
        private List<String> recTimes;

        public SendRecDto() {
            setTimes(new ArrayList<>());
            setSendTimes(new ArrayList<>());
            setRecTimes(new ArrayList<>());
        }
    }

}
