package com.example.test.util;

import android.app.Activity;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

public class ScreenUtil {

	/**
	 * 获取屏幕尺寸信息
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

}
