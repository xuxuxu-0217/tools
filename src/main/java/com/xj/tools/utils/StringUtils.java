package com.xj.tools.utils;

/**
 * @author: xujie
 * @date: 2022/7/7
 **/
public class StringUtils {


    /**
     * 字符串为空白
     * 空白定义:1.空字符串,2.空格
     * @param cs
     * @return
     */
    public static boolean isBlank(CharSequence cs){
        int strLen;

        if (cs == null || (strLen = cs.length()) == 0){
            return true;
        }

        for (int i = 0; i < strLen; i++){
            //只要有一个非空字符串即为非空字符串
            if (!Character.isWhitespace(cs.charAt(i))){
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串不为空白
     * @param cs
     * @return
     */
    public static boolean isNotBlank(CharSequence cs){
        return !isBlank(cs);
    }




    public static boolean equals(final CharSequence cs1, final CharSequence cs2){
        if (cs1 == cs2){
            return true;
        }
        if (cs1 == null || cs2 == null){
            return false;
        }
        if (cs1.length() != cs2.length()){
            return false;
        }
        if (cs1 instanceof String && cs2 instanceof String) {
            return cs1.equals(cs2);
        }

        final int length = cs1.length();
        for (int i = 0; i < length; i++){
            if (cs1.charAt(i) != cs2.charAt(i)){
                return false;
            }
        }

        return true;
    }



}
