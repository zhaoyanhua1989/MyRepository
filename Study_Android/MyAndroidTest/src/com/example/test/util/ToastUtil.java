package com.example.test.util;

import android.app.Activity;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {

	private static Toast sToastInstance;

	public static void showCustomToast(Activity activity, String message) {
		showCustomToast(activity, message, Toast.LENGTH_SHORT);
	}

	public static void showCustomToastLong(Activity activity, String message) {
		showCustomToast(activity, message, Toast.LENGTH_LONG);
	}

	public static void showCustomToast(final Activity activity, final String message, final int duration) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				makeText(activity, message, duration).show();
			}
		});
	}

	public static void showCustomToast(Activity activity, int resId, int duration) {
		showCustomToast(activity, activity.getString(resId), duration);
	}

	/**
	 * @param activity
	 * @param resId
	 *            在String.xml中定义的 字符串的资源id
	 */
	public static void showCustomToast(Activity activity, int resId) {
		showCustomToast(activity, activity.getString(resId));
	}

	private static Toast makeText(Activity activity, String message, int duration) {
		if (sToastInstance == null) {
			sToastInstance = new Toast(activity);
			TextView tv = getTextView(activity);
			sToastInstance.setView(tv);
		}
		((TextView) sToastInstance.getView()).setText(message);

		sToastInstance.setDuration(duration);
		return sToastInstance;
	}

	private static TextView getTextView(Activity activity) {
		TextView tv = new TextView(activity);
		tv.setTextColor(Color.WHITE);
		// 自定义toast背景样式
		tv.setBackgroundResource(RUtil.getDrawable(activity, "toastutil_toast_bg"));
		tv.setPadding(50, 30, 50, 30);
		return tv;
	}

	/**
	 * @param activity
	 * @param resId
	 *            在String.xml中定义的 字符串的资源id
	 */
	public static void showNormalToast(Activity activity, int resId) {
		showNormalToast(activity, activity.getString(resId), Toast.LENGTH_SHORT);
	}

	public static void showNormalToast(Activity activity, int resId, int duration) {
		showNormalToast(activity, activity.getString(resId), duration);
	}

	public static void showNormalToast(Activity activity, String message) {
		showNormalToast(activity, message, Toast.LENGTH_SHORT);
	}

	public static void showNormalToast(final Activity activity, final String message, final int duration) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast t = Toast.makeText(activity, message, duration);
				TextView tv = getTextView(activity);
				tv.setText(message);
				t.setView(tv);
				t.show();
			}
		});
	}
}
