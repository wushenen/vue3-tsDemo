package com.qtec.unicom.util;

public class CalculateUtil {

    public static String calRate(Long num){
        if (Math.ceil(num/1280/1024)>0)
            return Math.ceil(num/1280/1024)+" Mbps";
        return Math.ceil(num/1280)+" Kbps";
    }

}
