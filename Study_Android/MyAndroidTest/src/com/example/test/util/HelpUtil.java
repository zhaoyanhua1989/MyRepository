package com.example.test.util;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.telephony.TelephonyManager;

public class HelpUtil {

	/**
	 * 不建议使用，与方法2冲突
	 * 
	 * 判断应用是否是后台应用，方法1 此方法不需要加应用权限
	 * 
	 * @param context
	 * @return true表示当前应用程序在后台运行。false为在前台运行
	 * 
	 *         这里返回值有问题，home键回到桌面后，还是显示是前台应用。需要调起另一个应用测试，尚未测试！
	 */
	public static boolean isBackground(Context context) {

		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					MyLog.d("Background App:" + appProcess.processName);
					return true;
				} else {
					MyLog.d( "Foreground App:" + appProcess.processName);
					MyLog.d( "appProcess.importance:" + appProcess.importance);
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 判断应用是否切换到后台，方法2 此方法需要加权限：android.permission.GET_TASKS
	 * 
	 * @param context
	 * @return true表示当前应用程序在后台运行。false为在前台运行
	 */
	public static boolean isApplicationBroughtToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		MyLog.i("tasks.isEmpty():" + tasks.isEmpty());
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!(topActivity.getPackageName().equals(context.getPackageName()))) {
				MyLog.d( "Foreground App:" + topActivity.getPackageName());
				return true;
			}
			MyLog.d( "Foreground App:" + topActivity.getPackageName());
		}
		return false;
	}
	
	/************************** 测试获取设备号 ***************************/
	public static String getImei(Activity activity) {
		try {
			TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
			if (tm != null) {
				String imei = tm.getDeviceId();
				return imei;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
