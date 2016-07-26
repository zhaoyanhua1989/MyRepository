package com.example.test.util;

import android.app.Activity;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {
	
	private static Toast sToastInstance;

	public static void showToast(Activity activity, String message) {
		showToast(activity, message, Toast.LENGTH_SHORT);
	}

	public static void showToastLong(Activity activity, String message) {
		showToast(activity, message, Toast.LENGTH_LONG);
	}

	public static void showToast(final Activity activity, final String message, final int duration) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				makeText(activity, message, duration).show();
			}
		});
	}

	public static void showToast(Activity activity, int resId, int duration) {
		showToast(activity, activity.getString(resId), duration);
	}

	public static void showToast(Activity activity, int resId) {
		showToast(activity, activity.getString(resId));
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
		tv.setBackgroundColor(Color.BLACK);
		tv.setPadding(32, 24, 32, 24);
		return tv;
	}

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
