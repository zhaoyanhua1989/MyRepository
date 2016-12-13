package com.example.test.util;

import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;

public class AppUtil {

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
	@Deprecated
	public static boolean isBackground(Context context) {

		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					MyLog.d("Background App:" + appProcess.processName);
					return true;
				} else {
					MyLog.d("Foreground App:" + appProcess.processName);
					MyLog.d("appProcess.importance:" + appProcess.importance);
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
	@Deprecated
	public static boolean isApplicationBroughtToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		MyLog.i("tasks.isEmpty():" + tasks.isEmpty());
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!(topActivity.getPackageName().equals(context.getPackageName()))) {
				MyLog.d("Foreground App:" + topActivity.getPackageName());
				return true;
			}
			MyLog.d("Foreground App:" + topActivity.getPackageName());
		}
		return false;
	}

	/**
	 * 判断当前进程是不是前台进程(每个通过本app启动的进程，在启动时，都会调用Application的onCreate方法)
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isForegroundProcess(Context context) {
		boolean isFP = false;
		int pid = Process.myPid();
		String packageName = context.getPackageName();
		ActivityManager am = (ActivityManager) context.getSystemService("activity");
		for (ActivityManager.RunningAppProcessInfo process : am.getRunningAppProcesses()) {
			if ((process.pid == pid) && (TextUtils.equals(packageName, process.processName))) {
				isFP = true;
				break;
			}
		}
		if (isFP) {
			MyLog.d("Current process is foreground process.");
		} else {
			MyLog.d("Current process is background process.");
		}
		return isFP;
	}

	/**
	 * 是否是指定的进程
	 * 
	 * @param context
	 * @param processNameSuffix
	 *            指定进程的后缀名(比如友盟推送进程是 包名:channel，这里传channel)
	 * @return
	 */
	public static boolean isAppointProcess(Context context, String processNameSuffix) {
		String packageName = context.getPackageName();
		String processName = getCurProcessName(context);
		if (processName == null) {
			return false;
		}
		return processName.equals(packageName + ":" + processNameSuffix);
	}

	private static String getCurProcessName(Context context) {
		int pid = Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService("activity");
		@SuppressWarnings("rawtypes")
		Iterator localIterator = mActivityManager.getRunningAppProcesses().iterator();
		while (localIterator.hasNext()) {
			ActivityManager.RunningAppProcessInfo appProcess = (ActivityManager.RunningAppProcessInfo) localIterator.next();
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}
}
