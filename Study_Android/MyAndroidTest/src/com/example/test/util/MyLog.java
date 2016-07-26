package com.example.test.util;

import android.util.Log;

public class MyLog {
	public static final String MY_LOG_TAG = "MyLog";

	public static void i(String info){
		Log.i(MY_LOG_TAG, info);
	}
	
	public static void e(String errorInfo,Throwable t){
		Log.e(MY_LOG_TAG, errorInfo, t);
	}
	
	public static void e(String errorInfo){
		Log.e(MY_LOG_TAG, errorInfo);
	}
	
	public static void w(String info){
		Log.w(MY_LOG_TAG, info);
	}
	
	public static void d(String info){
		Log.d(MY_LOG_TAG, info);
	}
}
