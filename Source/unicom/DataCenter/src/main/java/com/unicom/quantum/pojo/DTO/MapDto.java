package com.unicom.quantum.pojo.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lenovo
 */
@Data
@ApiModel("地图信息")
public class MapDto {

    @ApiModelProperty("告警信息")
    List<String> alarmInfos;

    @ApiModelProperty("地理坐标")
    Map<String, Object> gc;

    @ApiModelProperty("城市关联")
    List<List<Object>> cityLinks;

    public MapDto() {
        setAlarmInfos(new ArrayList<>());
        setGc(new HashMap<>());
        setCityLinks(new ArrayList<>());
    }

    @Data
    public static class CityDto {

        private List<Object> cityLinksMapDto;

        public CityDto(CityMapDto cityMapDto, CityLinksMapDto linksMapDto) {
            setCityLinksMapDto(new ArrayList<>());
            cityLinksMapDto.add(cityMapDto);
            cityLinksMapDto.add(linksMapDto);
        }
    }

    @Data
    public static class CityMapDto {

        @ApiModelProperty("城市")
        private String city;

        public CityMapDto() {

        }

    }

    @Data
    public static class CityLinksMapDto {

        @ApiModelProperty("目标城市")
        private String targetCity;

        @ApiModelProperty("密钥分发速率")
        private String keyDistRate;

        @ApiModelProperty("散点图大小")
        private int value;

        public CityLinksMapDto() {

        }
    }
}
