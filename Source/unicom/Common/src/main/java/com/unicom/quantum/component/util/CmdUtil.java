package com.unicom.quantum.component.util;

import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.component.Exception.PwspException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CmdUtil {
    private static final Logger logger = LoggerFactory.getLogger(CmdUtil.class);

    public static ExecutorService CMD_THREAD_POOL = Executors.newFixedThreadPool(10);

    /**
     * 执行linux命令
     * @param cmd
     * @return
     */
    public static String executeLinuxCmd(String cmd) throws Exception{
        logger.info("执行命令[ " + cmd + "]");
        Runtime run = Runtime.getRuntime();
        BufferedReader stdoutReader = null;
        Process process = null;
        try {
            process = run.exec(cmd);
            String line;
            InputStreamReader read = new InputStreamReader(process.getInputStream());
            stdoutReader = new BufferedReader(read);
            StringBuffer out = new StringBuffer();
            while ((line = stdoutReader.readLine()) != null ) {
                out.append(line);
            }
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
            throw new PwspException(ResultHelper.genResult(403,"Forbidden.ProhibitedByRiskControl","当前用户使用脚本出错"));
        }catch (InterruptedException e) {
            e.printStackTrace();
            throw new PwspException(ResultHelper.genResult(403,"Forbidden.ProhibitedByRiskControl","当前用户使用脚本出错"));
        }finally{
            try {
                if(process != null){
                    process.destroy();
                }
                if(stdoutReader != null){
                    stdoutReader.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new PwspException(ResultHelper.genResult(403,"Forbidden.ProhibitedByRiskControl","当前用户使用脚本出错"));
            }
        }
        return String.valueOf(stdoutReader);
    }
    /**
     * 复杂linux命令执行，需要在命令前加入“/bin/bash, -c”.
     *
     * @see
     * @param command
     *            cmd
     * @return List<String>
     * @throws IOException
     *             IOException
     */
    public static String execCommand(List<String> command)throws Exception{
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process;
        String resultbuf = "";
        BufferedReader br = null;
        try {
            process = processBuilder.start();
            br = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                } else {
                    logger.info(line);
                    resultbuf = line;
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new PwspException(ResultHelper.genResult(403,"当前用户使用脚本出错"));
        }finally{
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    throw new PwspException(ResultHelper.genResult(403,"当前用户使用脚本出错"));
                }
            }
        }
        return resultbuf;
    }
}
