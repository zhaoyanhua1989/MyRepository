package com.example.test.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class AppUtil {

	private static final String CONTACT_REGEX = "((.+))?.*?<(.*)>";
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");
	private static final Pattern PHONE_ALL = Pattern.compile("13[0-9]|15[0-3]|15[7-9]|18[0-9]|145|147|15[5-6]|184");
	private static final Pattern PHONE_CMCC = Pattern.compile("13[4-9]|15[0-2]|15[7-9]|18[2-3]|18[7-8]|147|184");
	private static final Pattern PHONE_TIANYI = Pattern.compile("133|153|18[0-1]|189");
	private static final Pattern PHONE_WO = Pattern.compile("13[0-2]|15[5-6]|18[5-6]|145");
	public static final float TEXT_SIZE_320_240 = 7;
	public static final float TEXT_SIZE_480_320 = 8;
	public static final float TEXT_SIZE_800_480 = 12;
	public static final float TEXT_SIZE_960_640 = 13;
	public static final float TEXT_SIZE_960_540 = 13;
	public static final float TEXT_SIZE_1280_720 = 14;
	public static final float TEXT_SIZE_1920_1080 = 15;

	/**
	 * 获取屏幕尺寸信息
	 * 
	 * @param activity
	 * @return DisplayMetrics，可以metrics.widthPixels和metrics.heightPixels获取宽高
	 */
	public static DisplayMetrics getMetrics(Activity activity) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics;
	}

	/**
	 * 判断当前横竖屏
	 * 
	 * @return true横屏，false竖屏
	 */
	public static boolean isLandscape(Activity activity) {
		Configuration mConfiguration = activity.getResources().getConfiguration(); // 获取设置的配置信息
		int ori = mConfiguration.orientation; // 获取屏幕方向
		if (ori == Configuration.ORIENTATION_LANDSCAPE) {
			// 横屏
			return true;
		} else if (ori == Configuration.ORIENTATION_PORTRAIT) {
			// 竖屏
			return false;
		}
		return false;
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		int mResWidth = (int) (wm.getDefaultDisplay().getWidth());
		return mResWidth;
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		int mResHeight = (int) (wm.getDefaultDisplay().getHeight());
		return mResHeight;

	}

	/**
	 * 测试获取设备号
	 * 
	 * @param activity
	 * @return
	 */
	public static String getImei(Activity activity) {
		try {
			TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
			if (tm != null) {
				String imei = tm.getDeviceId();
				return imei;
			}
		} catch (Exception e) {
			MyLog.e("get imei error, exception is：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 获得手机的mac地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getMACId(Context context) {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifi == null) {
			return "null";
		}
		WifiInfo info = wifi.getConnectionInfo();
		return info == null ? "null" : info.getMacAddress();
	}

	public static String getMacAddress(Context context) {
		// 获取mac地址：
		String macAddress = "000000000000";
		try {
			WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
			if (null != info) {
				if (!TextUtils.isEmpty(info.getMacAddress()))
					macAddress = info.getMacAddress().replace(":", "");
				else
					return macAddress;
			}
		} catch (Exception e) {
			MyLog.e(e.toString());
			return macAddress;
		}
		return macAddress;
	}

	/**
	 * 显示或隐藏IME
	 * 
	 * @param context
	 * @param bHide
	 */
	public static void hideIME(Activity context, boolean bHide) {
		if (bHide) {
			try {
				((InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			} catch (NullPointerException e) {
				MyLog.e(e.toString());
			}
		} else { // show IME
			try {
				((InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE)).showSoftInput(context.getCurrentFocus(), InputMethodManager.SHOW_IMPLICIT);
			} catch (NullPointerException e) {
				MyLog.e(e.toString());
			}
		}
	}

	/**
	 * 在dialog开启前确定需要开启后跳出IME
	 * 
	 * @param dialog
	 */
	public static void showIMEonDialog(AlertDialog dialog) {
		try {
			Window window = dialog.getWindow();
			window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		} catch (Exception e) {
		}
	}

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
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
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
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
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

	/**
	 * 判断是否在锁屏状态
	 * 
	 * @return 锁屏状态时返回true
	 */
	public static boolean isScreenLocked(Context context) {
		KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
		return keyguardManager.inKeyguardRestrictedInputMode();
	}

	/**
	 * Dp float value xform to Px int value
	 * 
	 * @param context
	 * @param dpValue
	 * @return int px value
	 */
	public static int dp2px(Context context, float dpValue) {
		return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, convertDpi(context, dpValue), context.getResources().getDisplayMetrics()));
	}

	/**
	 * sp float value xform to Px int value
	 * 
	 * @param context
	 * @param spValue
	 * @return int px value
	 */
	public static int sp2px(Context context, float spValue) {
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	public static float convertDpi(Context context, float originDp) {
		DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();
		int dpi = mDisplayMetrics.densityDpi;
		float multiple = 1;

		if (dpi > 320 && dpi < 480) {
			multiple = 1;
		} else if (dpi > 480) {
			multiple = 1;
		}

		return originDp * multiple;
	}

	public static int getVerCode(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (Exception e) {
			return -1;
		}
	}

	public static String getVerName(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (Exception e) {
			return "no version name";
		}
	}

	/**
	 * 判断一个apk是否安装
	 * 
	 * @param ctx
	 * @param packageName
	 * @return
	 */
	public static boolean isPkgInstalled(Context ctx, String packageName) {
		PackageManager pm = ctx.getPackageManager();
		try {
			pm.getPackageInfo(packageName, 0);
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
		return true;
	}

	/**
	 * google框架是否已经安装
	 * 
	 * @param ctx
	 * @return
	 */
	public static boolean isGooglePlayInstalled(Context ctx) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://search?q=foo"));
		PackageManager pm = ctx.getPackageManager();
		List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static void installApkWithPrompt(File apkFile, Context context) {
		Intent promptInstall = new Intent(Intent.ACTION_VIEW);
		promptInstall.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
		context.startActivity(promptInstall);
	}

	/**
	 * @param context
	 *            used to check the device version and DownloadManager
	 *            information
	 * @return true if the download manager is available
	 */
	public static boolean isDownloadManagerAvailable(Context context) {
		try {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
				return false;
			}
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setClassName("com.android.providers.downloads.ui", "com.android.providers.downloads.ui.DownloadList");
			List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
			return list.size() > 0;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Note: Make sure isDownloadManagerAvailable return is true before use this
	 * method.
	 * 
	 * @param apkName
	 *            Apk File Name
	 * @param fullApkUrl
	 *            url of full
	 * @param context
	 *            Context
	 */
	@SuppressLint("NewApi")
	public static void downloadApkByDownloadManager(String apkName, String fullApkUrl, Context context) {
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fullApkUrl));
		request.setDescription(fullApkUrl);
		request.setTitle(apkName);
		// in order for this if to run, you must use the android 3.2 to compile
		// your app
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			request.allowScanningByMediaScanner();
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		}
		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, apkName);
		request.setVisibleInDownloadsUi(false);
		request.setMimeType("application/vnd.android.package-archive");
		// get download service and enqueue file
		DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		manager.enqueue(request);
	}

	/**
	 * 检查邮箱的格式
	 * 
	 * @param emailAddress
	 * @return
	 */
	public static boolean checkEmailFormate(String emailAddress) {
		boolean isFormated = false;
		if (EMAIL_PATTERN.matcher(emailAddress.trim()).find()) {
			isFormated = true;
		} else {
			isFormated = false;
		}
		return isFormated;
	}

	/**
	 * 判断是否是正确的手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNum(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 判断移动联通电信号码
	 * 
	 * @param phonenum
	 * @return
	 */
	public static boolean checkPhoneNum(String phonenum) {
		if (phonenum.length() < 11)
			return false;
		String phone = phonenum.substring(phonenum.length() - 11, phonenum.length() - 11 + 3);
		if (PHONE_ALL.matcher(phone).find())
			return true;
		else
			return false;
	}

	/**
	 * 看看是不是移动的号码
	 * 
	 * @param phonenum
	 * @return
	 */
	public static boolean checkPhoneCMCC(String phonenum) {
		if (phonenum.length() < 11)
			return false;
		String phone = phonenum.substring(phonenum.length() - 11, phonenum.length() - 11 + 3);
		if (PHONE_CMCC.matcher(phone).find())
			return true;
		else
			return false;
	}

	/**
	 * 号码返回邮箱后缀
	 * 
	 * @param phonenum
	 * @return
	 */
	public static String checkPhoneForResult(String phonenum) {
		if (phonenum.length() < 11)
			return "";
		String phone = phonenum.substring(phonenum.length() - 11, phonenum.length() - 11 + 3);
		if (PHONE_CMCC.matcher(phone).find())
			return "@139.com";
		else if (PHONE_TIANYI.matcher(phone).find())
			return "@189.cn";
		else if (PHONE_WO.matcher(phone).find()) {
			return "@wo.com.cn";
		} else
			return "";
	}

	/**
	 * 调用显示软键盘
	 * 
	 * @param view
	 * @param context
	 */
	public static void showKeyBoard(View view, Context context) {
		view.setFocusableInTouchMode(true); // 在view获取焦点之前需要把其他已经拥有焦点的控件setFocusable(false)
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param context
	 */
	public static void hideKeyBoard(Activity context) {
		View focusView = context.getCurrentFocus();
		if (focusView != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
		}
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param context
	 */
	public static void hideKeyBoard(Context context, View view) {
		if (view != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	/**
	 * @param cx
	 * 
	 * @param titleName
	 *            快捷方式名称
	 * 
	 * @return
	 */
	public static boolean hasShortcut(Context cx) {
		boolean result = false;
		Cursor c = null;
		// 获取当前应用名称
		String title = null;
		try {
			final PackageManager pm = cx.getPackageManager();
			title = pm.getApplicationLabel(pm.getApplicationInfo(cx.getPackageName(), PackageManager.GET_META_DATA)).toString();

			final String uriStr;
			if (android.os.Build.VERSION.SDK_INT < 8) {
				uriStr = "content://com.android.launcher.settings/favorites?notify=true";
			} else {
				uriStr = "content://com.android.launcher2.settings/favorites?notify=true";
			}
			final Uri CONTENT_URI = Uri.parse(uriStr);
			c = cx.getContentResolver().query(CONTENT_URI, null, "title=? and iconPackage=?", new String[] { title, cx.getPackageName() }, null);
			if (c != null && c.getCount() > 0) {
				result = true;
			}
		} catch (Exception e) {
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}
		return result;
	}

	public static String getEmailDefaultName(String emailName) {
		int index = emailName.indexOf("@");
		if (index > 0) {
			return emailName.substring(0, emailName.indexOf("@"));
		} else {
			return emailName;
		}
	}

	public static boolean networkStatusOK(final Context context) {
		boolean netStatus = false;
		try {
			ConnectivityManager connectManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectManager.getActiveNetworkInfo();
			if (activeNetworkInfo != null) {
				if (activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected()) {
					netStatus = true;
				}
			}
		} catch (Exception e) {
			MyLog.e(e.toString());
		}
		return netStatus;
	}

	public static boolean isWifiActive(Context icontext) {
		Context context = icontext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info;
		if (connectivity != null) {
			info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 是否连接了wifi
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo != null)
			return networkInfo.isConnected();
		return false;
	}

	/**
	 * 判断是否已开启网络，如果没有开启，则弹窗提示用户开启网络
	 * 
	 * @param context
	 */
	public static void checkNetworkAndShowDialogWhenUnConnected(final Context context) {
		try {
			// 判断有没有网
			if (isNetworkConnected(context)) {
				// 没网显示一个dialog
				AlertDialog.Builder dialog = new Builder(context);
				dialog.setMessage("亲，现在没网");
				dialog.setPositiveButton("打开", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 每个手机已经有一个activity界面打开网络
						// <intent_filter><action>open
						Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
						context.startActivity(intent);
						dialog.cancel();
					}
				});

				dialog.setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				dialog.show();
			}
		} catch (Exception e) {
			MyLog.e(e.toString());
		}
	}

	/**
	 * 是否连接了网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取网络连接状态
	 * 
	 * @param context
	 * @return 1代表gprs连接状态，返回0代表wifi连接状态
	 */
	public static int ConnectedState(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo != null && networkInfo.isConnected())
			return 0;

		NetworkInfo networkInfoMobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (networkInfoMobile != null && networkInfoMobile.isConnected())
			return 1;

		return -1;
	}

	/**
	 * 是否开启了移动网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (networkInfo != null)
			return networkInfo.isConnected();
		return false;
	}

	/**
	 * 用来获取手机拨号上网（包括CTWAP和CTNET）时由PDSN分配给手机终端的源IP地址。
	 * 
	 * @return
	 * @author SHANHY
	 */
	public static String getPsdnIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {
		}
		return "";
	}

	private static long lastClickTime; // 判断是否双击用到

	/**
	 * 判断双击的
	 * 
	 * @return
	 */
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 555) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/**
	 * 这个是看是不是4.0的
	 * 
	 * @return
	 */
	public static boolean moreThanAPI_14() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}

	/**
	 * 这个是获取app的data的
	 * 
	 * @param key
	 * @return
	 */

	public static String getMetaData(Context context, String key) {
		try {
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			return appInfo.metaData.getString(key);
		} catch (NameNotFoundException e) {
			// Log.e(TAG, "Failed to load meta-data, NameNotFound: " +
			// e.getMessage());
			return "";
		} catch (NullPointerException e) {
			// Log.e("s", "Failed to load meta-data, NullPointer: " +
			// e.getMessage());
			return "";
		}
	}

	/**
	 * 这个是查找一个字符串有多少个的情况
	 * 
	 * @param source
	 * @param regexNew
	 * @return
	 */
	public static int finder(String source, String regexNew) {
		String regex = "[a-zA-Z]+";
		if (regexNew != null && !regexNew.equals("")) {
			regex = regexNew;
		}
		Pattern expression = Pattern.compile(regex);
		Matcher matcher = expression.matcher(source);
		int n = 0;
		while (matcher.find()) {
			n++;
		}
		return n;
	}

	/**
	 * 获取现在日期，返回几月几日
	 */
	public static String getNowDate() {
		Calendar calendar = Calendar.getInstance();
		int dayOfMounth = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH);
		String date = (month + 1) + "月" + dayOfMounth + "日";
		return date;
	}

	/**
	 * 获取时间，返回例如：上午 9:50
	 */
	public static String getTimeFormatAm_Pm_HH_mm() {
		Calendar cl = Calendar.getInstance();
		cl.setTimeInMillis(System.currentTimeMillis());
		int amPm = cl.get(Calendar.AM_PM);
		int hour = cl.get(Calendar.HOUR);
		if (hour >= 12) {
			hour -= 12;
		}
		String time = null;
		switch (amPm) {
		case Calendar.AM:
			time = "上午 " + hour + ":" + cl.get(Calendar.MINUTE);
			break;
		case Calendar.PM:
			time = "下午 " + hour + ":" + cl.get(Calendar.MINUTE);
			break;
		}
		return time;
	}

	/**
	 * 获取时间戳
	 * 
	 * @return 类似20161221135359
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTime() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date);
	}

	public static String getName(String text) {
		Pattern pattern = Pattern.compile(CONTACT_REGEX);
		Matcher m = pattern.matcher(text);
		if (m.find()) {
			return m.group(2).replace("\"", "");
		} else {
			return text;
		}
	}

	public static String getEmail(String text) {
		Pattern pattern = Pattern.compile(CONTACT_REGEX);
		Matcher m = pattern.matcher(text);
		if (m.find()) {
			return m.group(3).replace("\"", "");
		} else {
			return text;
		}
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 获取versionName
	 * 
	 * @param context
	 * @return
	 * @throws NameNotFoundException
	 */
	public static String getCurrentVersion(Context context) throws NameNotFoundException {
		PackageManager manager = context.getPackageManager();
		String packageName = context.getPackageName();
		PackageInfo packageInfo = manager.getPackageInfo(packageName, 0);
		return packageInfo.versionName;
	}

	/**
	 * 获取应用名称
	 * 
	 * @param activity
	 * @return
	 */
	public String getAppName(Activity activity) {
		String appName = null;
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = activity.getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(activity.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		appName = (String) packageManager.getApplicationLabel(applicationInfo);
		return appName;
	}

	/**
	 * 获取当前地理坐标
	 * 
	 * @param context
	 * @return
	 */
	public static String getLocationPoint(Context context) {
		double latitude = 0.0;
		double longitude = 0.0;
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// 返回所有已知的位置提供者的名称列表，包括未获准访问或调用活动目前已停用的。
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(false);
		// 设置位置服务免费
		criteria.setAccuracy(Criteria.ACCURACY_COARSE); // 设置水平位置精度
		// getBestProvider 只有允许访问调用活动的位置供应商将被返回
		String providerName = lm.getBestProvider(criteria, true);
		if (providerName != null) {
			Location location = lm.getLastKnownLocation(providerName);
			if (location != null) {
				// 获取维度信息
				latitude = location.getLatitude();
				// 获取经度信息
				longitude = location.getLongitude();
			}
			return latitude + "," + longitude;
		} else {
			return latitude + "," + longitude;
		}
	}

	/**
	 * 获取用户当前的地理位置
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String getLocationAddress(Context context) {
		double latitude = 0.0;
		double longitude = 0.0;
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// 返回所有已知的位置提供者的名称列表，包括未获准访问或调用活动目前已停用的。
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(false);
		// 设置位置服务免费
		criteria.setAccuracy(Criteria.ACCURACY_COARSE); // 设置水平位置精度
		// getBestProvider 只有允许访问调用活动的位置供应商将被返回
		String providerName = lm.getBestProvider(criteria, true);
		if (providerName != null) {
			Location location = lm.getLastKnownLocation(providerName);
			if (location != null) {
				// 获取维度信息
				latitude = location.getLatitude();
				// 获取经度信息
				longitude = location.getLongitude();
			}
		} else {
			return "";
		}
		Geocoder geocoder = new Geocoder(context);
		try {
			List<Address> fromLocation = geocoder.getFromLocation(latitude, longitude, 5);
			for (int i = 0; i < fromLocation.size(); i++) {
				Address address = fromLocation.get(i);
				return address.getAdminArea() + "," + address.getLocality();
			}
		} catch (IOException e) {
			MyLog.e(e.toString());
		}
		return "";
	}

	/**
	 * 获得当前系统国家
	 */
	static public String getCurrentCountry(Context context) {
		Locale locale = context.getResources().getConfiguration().locale;
		String country = locale.getCountry();
		return country;
	}

	public static String urlEncode(String str) {
		String encode = "";
		if (TextUtils.isEmpty(str)) {
			return encode;
		}
		try {
			encode = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			MyLog.e(e.toString());
		}
		return encode;
	}

	/**
	 * 发送验证码，并获取验证码
	 * 
	 * @param context
	 * @param phoneNum
	 * @return -1如果传入的手机号码有误
	 */
	public static String sendVerificationCode(Context context, String phoneNum) {
		// TODO 校验手机号码
		if (!isMobileNum(phoneNum)) {
			return null;
		}
		// 1.获取系统服务 SmsManager
		SmsManager manager = SmsManager.getDefault();
		// 2.调用manager的相关方法发送短信
		// 随机6位数验证码
		String numCode = Integer.toString((int) ((Math.random() * 9 + 1) * 100000));
		// 切分短信
		ArrayList<String> bodies = manager.divideMessage(MessageFormat.format(OverallVariable.SENDMSGTOREGISTER, numCode));
		// 创建用于提示是否发送成功的PendingIntent
		Intent intent1 = new Intent(OverallVariable.SEND_OK_OR_NOT);
		intent1.putExtra("number", phoneNum);
		PendingIntent pi1 = PendingIntent.getBroadcast(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
		// pi2用于提示用户对方是否成功接收短信
		Intent intent2 = new Intent(OverallVariable.RECEIVE_OK_OR_NOT);
		PendingIntent pi2 = PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
		for (int i = 0; i < bodies.size(); i++) {
			manager.sendTextMessage(phoneNum, null, bodies.get(i), pi1, pi2);
		}
		return numCode;
	}

	private static ProgressDialog progressDialog;

	/**
	 * 显示安卓自带的加载框转菊花
	 * 
	 * @param activity
	 * @param msg
	 *            要显示的信息
	 * @param isCancel
	 *            true:点击加载框以外位置加载框会消失
	 */
	public static void showProgressDialog(Activity activity, String msg, boolean isCancel) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(activity);
			progressDialog.setMessage(msg);
			progressDialog.setCanceledOnTouchOutside(isCancel);
			progressDialog.show();
		}
	}

	/**
	 * 关闭安卓自带的加载框
	 */
	public static void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.cancel();
			progressDialog = null;
		}
	}
}
