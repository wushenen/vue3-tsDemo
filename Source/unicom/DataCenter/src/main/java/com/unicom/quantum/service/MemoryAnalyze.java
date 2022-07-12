package com.unicom.quantum.service;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

/**
 * @author jerry
 * @version 1.0
 * @note
 * @date 2022/5/12
 */
public class MemoryAnalyze {

    public static String getRamInfo(){
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long total = osmxb.getTotalPhysicalMemorySize();
        long free = osmxb.getFreePhysicalMemorySize();
        double used = (total-free)* 1.0/ total ;
        return new DecimalFormat("#.##").format(used);
    }
}
