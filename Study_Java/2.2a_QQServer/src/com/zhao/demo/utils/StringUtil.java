package com.zhao.demo.utils;

public class StringUtil {

	public static boolean isNull(String str){
		if(str == null || "".equals(str)){
			return true;
		}
		return false;
	}
	
}
