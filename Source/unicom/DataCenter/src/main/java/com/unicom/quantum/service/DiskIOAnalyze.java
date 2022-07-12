package com.unicom.quantum.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jerry
 * @version 1.0
 * @note
 * @date 2022/5/12
 */
public class DiskIOAnalyze {

    public static List<String> getDiskIOInfo(){
        float readRate = 0.0f;
        float writeRate = 0.0f;
        Process pro = null;
        Runtime r = Runtime.getRuntime();
        try {
            String command = "iostat -d -k";
            pro = r.exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String line = null;
            int count =  0;
            while((line=in.readLine()) != null){
                if(++count >= 4){
                    String[] temp = line.split("\\s+");
                    if(temp.length > 1){
                        readRate += Float.parseFloat(temp[2]);
                        writeRate += Float.parseFloat(temp[3]);
                    }
                }
            }
            in.close();
            pro.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> diskIOInfo = new ArrayList<>();
        diskIOInfo.add(readRate+"");
        diskIOInfo.add(writeRate+"");
        return diskIOInfo;
    }

}
