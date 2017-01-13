package com.example.test.util;

import android.app.Activity;
import android.content.Context;

public class RUtil {

	public static int getResIdentifier(Context context, String name, String type) {
		return context.getResources().getIdentifier(name, type, context.getPackageName());
	}

	public static int getId(Context context, String name) {
		return getResIdentifier(context, name, "id");
	}

	public static int getDrawable(Context context, String name) {
		return getResIdentifier(context, name, "drawable");
	}

	public static int getAnim(Context context, String name) {
		return getResIdentifier(context, name, "anim");
	}

	public static int getString(Context context, String name) {
		return getResIdentifier(context, name, "string");
	}

	public static int getLayout(Context context, String name) {
		return getResIdentifier(context, name, "layout");
	}

	public static int getStringArray(Context context, String name) {
		return getResIdentifier(context, name, "array");
	}

	public static int getStyle(Context context, String name) {
		return getResIdentifier(context, name, "style");
	}

	public static String getValuesString(Activity activity, String name) {
		return activity.getResources().getString(getString(activity, name));
	}

	public static String[] getValuesStringArray(Activity activity, String name) {
		return activity.getResources().getStringArray(getStringArray(activity, name));
	}

	/**
	 * 通过资源id获取name
	 * 
	 * @param context
	 * @param id
	 *            资源id
	 * @return
	 */
	public static String getIdName(Context context, int id) {
		return context.getResources().getResourceEntryName(id);
	}

	/**
	 * 获取做为app显示图片的id
	 * 
	 * @param context
	 * @return
	 */
	public static int getAppIcon(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.icon;
		} catch (Exception e) {
			MyLog.e("Error occurred when get app icon", e);
		}
		return -1;
	}
}
