package com.example.test;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.example.test.util.MyLog;

public class MyApplication extends Application {

	public static ArrayList<Activity> activitys = new ArrayList<Activity>();

	//首先执行
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MyLog.d("MyApplication attachBaseContext...");
	}

	//第二执行
	@Override
	public void onCreate() {
		super.onCreate();
		MyLog.d("MyApplication oncreate...");
		MyLog.openLog(true);
	}

	//当终止应用程序对象时调用，不保证一定被调用，当程序是被内核终止以便为其他应用程序释放资源，那
	//么将不会提醒，并且不调用应用程序的对象的onTerminate方法而直接终止进程
	@Override
	public void onTerminate() {
		super.onTerminate();
		MyLog.d("MyApplication attachBaseContext...");
	}

}
