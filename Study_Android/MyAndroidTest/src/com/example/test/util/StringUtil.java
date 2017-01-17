package com.example.test.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class StringUtil {

	public static String defaultString(String str, String defaultStr) {
		if (str != null && str.trim().length() > 0) {
			return str;
		}
		return defaultStr;
	}

	public static boolean equals(String str1, String str2) {
		if (str1 == null && str2 == null) {
			return true;
		}
		if (str1 == null || str2 == null) {
			return false;
		}
		return str1.equals(str2);
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.length() == 0 || str.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public static String getMetaDataValue(Context mContext, String meta_key) {
		ApplicationInfo appInfo = null;
		String msg;
		try {
			appInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
			msg = appInfo.metaData.getString(meta_key);
		} catch (NameNotFoundException e) {
			msg = "";
			MyLog.e("the metadata name is not find, exception=" + e.getMessage());
		}
		return msg;
	}
}
