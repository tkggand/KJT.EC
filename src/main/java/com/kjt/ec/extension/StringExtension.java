package com.kjt.ec.extension;

public class StringExtension {
    public static boolean isEmptyOrNull(String value){
        return value==null||value.equals("");
    }
}
