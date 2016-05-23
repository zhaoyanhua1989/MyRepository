package com.zhao.demo.utils;

public class StringUtil {

	public static boolean isNull(String str){
		if(str == null || "".equals(str)){
			return true;
		}
		return false;
	}
	
	public static String getType(String msg){
		if(msg.startsWith(ConstantUtil.PRIVATE_CHAT)) {
			return ConstantUtil.PRIVATE_CHAT;
		} else if (msg.startsWith(ConstantUtil.REFRESH_CONTACTS)) {
			return ConstantUtil.REFRESH_CONTACTS;
		} else if (msg.startsWith(ConstantUtil.LOGINACCOUNT)) {
			return ConstantUtil.LOGINACCOUNT;
		} else if (msg.startsWith(ConstantUtil.EXITACCOUNT)) {
			return ConstantUtil.EXITACCOUNT;
		}
		return null;
	}
	
}
