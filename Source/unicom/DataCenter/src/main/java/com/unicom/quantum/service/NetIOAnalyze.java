package com.unicom.quantum.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jerry
 * @version 1.0
 * @note
 * @date 2022/5/12
 */
public class NetIOAnalyze {

    private final static String WLAN_NAME = "eno1";
    private static long LAST_IN_SIZE = 0;
    private static long LAST_OUT_SIZE = 0;
    private static long LAST_Time = 0;


    public static List<String> getNetIOInfo(){
        double inRate = 0.0;
        double outRate = 0.0;
        long curInSize = 0;
        long curOutSize = 0;
        Process pro1;
        ArrayList<String> netIOInfo = new ArrayList<>();
        Runtime r = Runtime.getRuntime();
        try {
            String command = "cat /proc/net/dev";
            pro1 = r.exec(command);
            long curTime = System.currentTimeMillis();
            BufferedReader in1 = new BufferedReader(new InputStreamReader(pro1.getInputStream()));
            String line = null;
            while((line=in1.readLine()) != null){
                line = line.trim();
                if(line.startsWith(WLAN_NAME)){
                    String[] temp = line.split("\\s+");
                    curInSize = Long.parseLong(temp[1]);    //Receive bytes,单位为Byte
                    curOutSize = Long.parseLong(temp[8]);   //Transmit bytes,单位为Byte
                    break;
                }
            }
            in1.close();
            pro1.destroy();
            if (curInSize != 0 && curOutSize != 0){
                netIOInfo.add(new DecimalFormat("#.##").format((curInSize - LAST_IN_SIZE )/(curTime-LAST_Time)+1));
                netIOInfo.add(new DecimalFormat("#.##").format((curOutSize - LAST_OUT_SIZE )/(curTime-LAST_Time)+1));
            }
            LAST_IN_SIZE = curInSize;
            LAST_OUT_SIZE = curOutSize;
            LAST_Time = curTime;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return netIOInfo;
    }
}
