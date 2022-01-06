package com.cucc.unicom.component.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public final class IpWhiteCheckUtil {

    private static Pattern pattern = Pattern
            .compile("(1\\d{1,2}|2[0-4]\\d|25[0-5]|\\d{1,2})\\." + "(1\\d{1,2}|2[0-4]\\d|25[0-5]|\\d{1,2})\\."
                    + "(1\\d{1,2}|2[0-4]\\d|25[0-5]|\\d{1,2})\\." + "(1\\d{1,2}|2[0-4]\\d|25[0-5]|\\d{1,2})");
    public static final String DEFAULT_ALLOW_ALL_FLAG = "*";
    public static final String DEFAULT_DENY_ALL_FLAG = "0";

    private static Set<String> getAvailableIpList(String allowIp) {
        String[] splitRex = allowIp.split(";");// 拆分出白名单正则
        Set<String> ipList = new HashSet<String>(splitRex.length);
        for (String allow : splitRex) {
            if (allow.contains("*")) {// 处理通配符 *
                String[] ips = allow.split("\\.");
                String[] from = new String[] { "0", "0", "0", "0" };
                String[] end = new String[] { "255", "255", "255", "255" };
                List<String> tem = new ArrayList<>();
                for (int i = 0; i < ips.length; i++)
                    if (ips[i].indexOf("*") > -1) {
                        tem = complete(ips[i]);
                        from[i] = null;
                        end[i] = null;
                    } else {
                        from[i] = ips[i];
                        end[i] = ips[i];
                    }
                StringBuilder fromIP = new StringBuilder();
                StringBuilder endIP = new StringBuilder();
                for (int i = 0; i < 4; i++)
                    if (from[i] != null) {
                        fromIP.append(from[i]).append(".");
                        endIP.append(end[i]).append(".");
                    } else {
                        fromIP.append("[*].");
                        endIP.append("[*].");
                    }
                fromIP.deleteCharAt(fromIP.length() - 1);
                endIP.deleteCharAt(endIP.length() - 1);

                for (String s : tem) {
                    String ip = fromIP.toString().replace("[*]", s.split(";")[0]) + "-"
                            + endIP.toString().replace("[*]", s.split(";")[1]);
                    if (validate(ip)) {
                        ipList.add(ip);
                    }
                }
            } else if (allow.contains("/")) {
                ipList.add(allow);
            } else {
                if (validate(allow)) {
                    ipList.add(allow);
                }
            }

        }

        return ipList;
    }


    public static List<String> complete(String arg) {
        List<String> com = new ArrayList<>();
        int len = arg.length();
        if (len == 1) {
            com.add("0;255");
        } else if (len == 2) {
            String s1 = complete(arg, 1);
            if (s1 != null)
                com.add(s1);
            String s2 = complete(arg, 2);
            if (s2 != null)
                com.add(s2);
        } else {
            String s1 = complete(arg, 1);
            if (s1 != null)
                com.add(s1);
        }
        return com;
    }

    public static String complete(String arg, int length) {
        String from = "";
        String end = "";
        if (length == 1) {
            from = arg.replace("*", "0");
            end = arg.replace("*", "9");
        } else {
            from = arg.replace("*", "00");
            end = arg.replace("*", "99");
        }
        if (Integer.valueOf(from) > 255)
            return null;
        if (Integer.valueOf(end) > 255)
            end = "255";
        return from + ";" + end;
    }

    /**
     * 在添加至白名单时进行格式校验
     * 校验 单个ip地址或者是 ip 地址范围
     * 对整个ip地址段 不进行校验
     * @param ip
     * @return
     */
    public static boolean validate(String ip) {
        String[] temp = ip.split("-");
        for (String s : temp)
            if (!pattern.matcher(s).matches()) {
                return false;
            }
        return true;
    }

    public static boolean isPermitted(String ip, Set<String> ipList) {
        if (ipList.isEmpty() || ipList.contains(ip))
            return true;
        for (String allow : ipList) {
            if (allow.indexOf("-") > -1) {
                String[] tempAllow = allow.split("-");
                String[] from = tempAllow[0].split("\\.");
                String[] end = tempAllow[1].split("\\.");
                String[] tag = ip.split("\\.");
                boolean check = true;
                for (int i = 0; i < 4; i++) {// 对IP从左到右进行逐段匹配
                    int s = Integer.valueOf(from[i]);
                    int t = Integer.valueOf(tag[i]);
                    int e = Integer.valueOf(end[i]);
                    if (!(s <= t && t <= e)) {
                        check = false;
                        break;
                    }
                }
                if (check)
                    return true;
            } else if (allow.contains("/")) {
                int splitIndex = allow.indexOf("/");
                String ipSegment = allow.substring(0, splitIndex);
                String netmask = allow.substring(splitIndex + 1);
                long ipLong = ipToLong(ip);
                long maskLong=(2L<<32 -1) -(2L << Integer.valueOf(32-Integer.valueOf(netmask))-1);
                String calcSegment = longToIP(ipLong & maskLong);
                if(ipSegment.equals(calcSegment))return true;
            }
        }
        return false;
    }

    public static boolean isPermitted(String ip, String ipWhiteConfig) {
        if (null == ip || "".equals(ip))
            return false;
        //ip格式不对
        if(!pattern.matcher(ip).matches())return false;
        if (DEFAULT_ALLOW_ALL_FLAG.equals(ipWhiteConfig))
            return true;
        if (DEFAULT_DENY_ALL_FLAG.equals(ipWhiteConfig))
            return false;
        Set<String> ipList = getAvailableIpList(ipWhiteConfig);
        return isPermitted(ip, ipList);
    }

    private static long ipToLong(String strIP) {
        long[] ip = new long[4];
        int position1 = strIP.indexOf(".");
        int position2 = strIP.indexOf(".", position1 + 1);
        int position3 = strIP.indexOf(".", position2 + 1);
        ip[0] = Long.parseLong(strIP.substring(0, position1));
        ip[1] = Long.parseLong(strIP.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strIP.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strIP.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    private static String longToIP(long longIP) {
        StringBuilder sb = new StringBuilder("");
        sb.append(String.valueOf(longIP >>> 24));
        sb.append(".");
        sb.append(String.valueOf((longIP & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((longIP & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append(String.valueOf(longIP & 0x000000FF));
        return sb.toString();
    }
}