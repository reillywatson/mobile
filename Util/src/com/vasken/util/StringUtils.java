package com.vasken.util;

public class StringUtils {

    public static String escapeHtml(String str) {
    	return Entities.HTML40.escape(str);
    }
    
    public static String unescapeHtml(String str) {
    	return Entities.HTML40.unescape(str);
    }
}