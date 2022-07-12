package com.unicom.quantum.controller;

import com.unicom.quantum.component.Result;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.pojo.DTO.MapDto;
import com.unicom.quantum.service.impl.BrainServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author lenovo
 */
@Api(tags = "量子安全大脑")
@RestController
@RequestMapping("/brain")
public class BrainController {



    @RequestMapping( value = "/physicalMachineInfo", method = RequestMethod.GET)
    @ApiOperation(value  = "物理机信息")
    public Result physicalMachineInfo() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("times", BrainServiceImpl.times);
        map.put("cpuRates", BrainServiceImpl.cpuRates);
        map.put("ramLoadRates", BrainServiceImpl.ramLoadRates);
        map.put("diskInRates", BrainServiceImpl.diskInRates);
        map.put("diskOutRates", BrainServiceImpl.diskOutRates);
        map.put("netUpRates", BrainServiceImpl.netUpRates);
        map.put("netDownRates", BrainServiceImpl.netDownRates);
        map.put("realTimes", BrainServiceImpl.realTimes);
        map.put("onlineNums", BrainServiceImpl.onlineNums);
        map.put("registerNums", BrainServiceImpl.registerNums);
        map.put("keyInNums", BrainServiceImpl.keyInNums);
        map.put("keyOutNums", BrainServiceImpl.keyOutNums);
        return ResultHelper.genResultWithSuccess(map);
    }




    @RequestMapping(value = "/mapInfo", method = RequestMethod.GET)
    @ApiOperation(value = "地图信息")
    public MapDto mapInfo() {
        MapDto mapDto = new MapDto();
        List<String> cityList = new ArrayList<>();
        cityList.add("容城");
        cityList.add("雄县");
        List<double[]> valueList = new ArrayList<>();
        valueList.add(new double[]{115.388965, 39.034787});
        valueList.add(new double[]{116.10865, 38.99455});

        int i = 0;
        for (String s : cityList) {
            mapDto.getGc().put(s, valueList.get(i));
            i++;
        }

        int speedRate = 4000;
        List<String> sendCityList = new ArrayList<>();
        sendCityList.add("容城");
        sendCityList.add("雄县");
        List<String> targetCityList = new ArrayList<>();
        targetCityList.add("雄县");
        targetCityList.add("容城");
        List<String> rateList = new ArrayList<>();
        rateList.add(randomOfNum(speedRate));
        rateList.add(randomOfNum(speedRate));
        List<Integer> cityValueList = new ArrayList<>();
        cityValueList.add(100);
        cityValueList.add(100);

        int count = 0;
        for (String s : sendCityList) {

            MapDto.CityMapDto cityMapDto = new MapDto.CityMapDto();
            cityMapDto.setCity(s);

            MapDto.CityLinksMapDto cityLinksDto = new MapDto.CityLinksMapDto();
            cityLinksDto.setTargetCity(targetCityList.get(count));
            cityLinksDto.setKeyDistRate(rateList.get(count));
            cityLinksDto.setValue(cityValueList.get(count));

            MapDto.CityDto cityDtoList = new MapDto.CityDto(cityMapDto, cityLinksDto);

            mapDto.getCityLinks().add(cityDtoList.getCityLinksMapDto());
            count++;
        }

        return mapDto;
    }

    public static String randomOfNum(double num) {
        return String.valueOf(String.format("%.0f", Math.random() * num));
    }



}
