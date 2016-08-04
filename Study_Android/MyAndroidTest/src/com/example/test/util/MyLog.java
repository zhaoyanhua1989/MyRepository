package com.example.test.util;

import android.util.Log;

public class MyLog {

	public static final String MY_LOG_TAG = "MyLog";
	private static boolean isPrint = false; // 是否打印日志

	public static void i(String info) {
		if (isPrint)
			Log.i(MY_LOG_TAG, info);
	}

	public static void e(String errorInfo, Throwable t) {
		if (isPrint)
			Log.e(MY_LOG_TAG, errorInfo, t);
	}

	public static void e(String errorInfo) {
		if (isPrint)
			Log.e(MY_LOG_TAG, errorInfo);
	}

	public static void w(String info) {
		if (isPrint)
			Log.w(MY_LOG_TAG, info);
	}

	public static void d(String info) {
		if (isPrint)
			Log.d(MY_LOG_TAG, info);
	}

	public static void openLog(boolean flag) {
		isPrint = flag;
	}
}
