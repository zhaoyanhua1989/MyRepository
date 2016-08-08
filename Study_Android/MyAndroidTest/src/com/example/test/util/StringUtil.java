package com.example.test.util;

public class StringUtil {

	public static String defaultString(String str, String defaultStr) {
		if (str != null && str.trim().length() > 0) {
			return str;
		}
		return defaultStr;
	}

	public static boolean equals(String str1, String str2) {
		if (str1 == null && str2 == null) {
			return true;
		}
		if (str1 == null || str2 == null) {
			return false;
		}
		return str1.equals(str2);
	}

	public static boolean isEmpty(String str) {
		if (str.length() == 0 || str == null) {
			return true;
		}
		return false;
	}
}
