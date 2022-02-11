package com.unicom.quantum.component.util;

public class CalculateUtil {

    public static String calRate(Long num){
        if (Math.ceil(num/1280/1024)>0)
            return Math.ceil(num/1280/1024)+" Mbps";
        return Math.ceil(num/1280)+" Kbps";
    }

    public static String calRate(Long num,Long time){
        if (Math.ceil(num/128/1024/time)>0)
            return Math.ceil(num/128/1024/time)+" Mbps";
        return Math.ceil(num/128/time)+" Kbps";
    }

}
