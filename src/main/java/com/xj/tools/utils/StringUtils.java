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




    public static boolean equals(CharSequence cs1, CharSequence cs2){
        if (cs1 == cs2){
            return true;
        }else if (cs1 != null && cs2 != null){
            if (cs1.length() != cs2.length()){
                return false;
            }else {
                return cs1 instanceof String && cs2 instanceof String ? cs1.equals(cs2) : CharSe
            }
        }else {
            return false;
        }
    }



}
