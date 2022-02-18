package com.unicom.quantum.component.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegUtils {

    /**
     * 字符串必须包含字母数字和特殊字符
     * @param str
     * @return
     */
    public static boolean pwdMatch(String str) {
        String pattern = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[.~!@#$%^_&*])[A-Za-z0-9.~!@#$%^_&*]{8,20}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        return m.matches();
    }

}
