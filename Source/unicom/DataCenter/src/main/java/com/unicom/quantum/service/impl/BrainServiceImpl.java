package com.unicom.quantum.service.impl;

import com.sun.jmx.remote.internal.ArrayQueue;
import com.unicom.quantum.mapper.BrainMapper;
import com.unicom.quantum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * @author jerry
 * @version 1.0
 * @note
 * @date 2022/5/12
 */
@Service
public class BrainServiceImpl implements BrainService {

    public static ArrayQueue<String> times = new ArrayQueue<String>(5);
    public static ArrayQueue<String> cpuRates = new ArrayQueue<String>(5);
    public static ArrayQueue<String> ramLoadRates = new ArrayQueue<String>(5);
    public static ArrayQueue<String> diskInRates = new ArrayQueue<String>(5);
    public static ArrayQueue<String> diskOutRates = new ArrayQueue<String>(5);
    public static ArrayQueue<String> netUpRates = new ArrayQueue<String>(5);
    public static ArrayQueue<String> netDownRates = new ArrayQueue<String>(5);
    public static ArrayQueue<String> realTimes = new ArrayQueue<String>(6);
    public static ArrayQueue<Long> onlineNums = new ArrayQueue<Long>(6);
    public static ArrayQueue<Long> registerNums = new ArrayQueue<Long>(6);
    public static ArrayQueue<Long> keyInNums = new ArrayQueue<Long>(5);
    public static ArrayQueue<Long> keyOutNums = new ArrayQueue<Long>(5);


    @Autowired
    private BrainMapper brainMapper;

    @Override
    public void getPhysicalMachineInfo() throws InterruptedException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        String time = sf.format(calendar.getTime());
        if (times.size() == 5){
            times.remove(0);
            cpuRates.remove(0);
            ramLoadRates.remove(0);
            diskInRates.remove(0);
            diskOutRates.remove(0);
            netUpRates.remove(0);
            netDownRates.remove(0);
            keyInNums.remove(0);
            keyOutNums.remove(0);
        }
        times.add(time);
        cpuRates.add(CpuAnalyze.getCpuInfo());
        ramLoadRates.add(MemoryAnalyze.getRamInfo());
        List<String> diskIOInfo = DiskIOAnalyze.getDiskIOInfo();
        diskInRates.add(diskIOInfo.get(1));
        diskOutRates.add(diskIOInfo.get(0));
        List<String> netIOInfo = NetIOAnalyze.getNetIOInfo();
        netUpRates.add(netIOInfo.get(1));
        netDownRates.add(netIOInfo.get(0));
        keyInNums.add(brainMapper.getKeyInNum().longValue());
        keyOutNums.add(brainMapper.getKeyOutNum().longValue());
    }


    @Override
    public void geCockpitRealTimeInfo() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sf = new SimpleDateFormat("HH:00");
        String time = sf.format(calendar.getTime());
        if (realTimes.size() == 6){
            realTimes.remove(0);
            onlineNums.remove(0);
            registerNums.remove(0);
        }
        realTimes.add(time);
        onlineNums.add(brainMapper.getOnlineNum().longValue());
        registerNums.add(brainMapper.getRegisterNum().longValue());
    }
}
