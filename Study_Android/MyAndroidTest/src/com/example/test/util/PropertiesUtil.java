package com.example.test.util;

import java.io.IOException;
import java.util.Properties;

/**
 * 基本信息处理类
 */
import android.content.Context;

public class PropertiesUtil {

	private static String deviceId = "undefined";
	private static String screenSize = "undefined";
	private static Properties configProperties;

	public static void loadFromAssetConfig(Context context) {
		MyLog.d("load config from assets...");
		try {
			configProperties = new Properties();
			configProperties.clear();
			configProperties.load(context.getAssets().open(OverallVariable.INFOMSGFILE));
		} catch (IOException e) {
			MyLog.e("failed to load config properties from " + OverallVariable.INFOMSGFILE, e);
		}
	}

	private static String getValue(String str) {
		return configProperties.getProperty(str);
	}

	public static String getVersion() {
		return getValue("version");
	}

	public static String getDeviceId() {
		return deviceId;
	}

	public static void setDeviceId(String deviceId) {
		PropertiesUtil.deviceId = deviceId;
	}

	public static String getScreenSize() {
		return screenSize;
	}

	public static void setScreenSize(String screenSize) {
		PropertiesUtil.screenSize = screenSize;
	}

	public static void destroy(Context context) {
		if (configProperties != null) {
			configProperties.clear();
			context.getAssets().close();
		}
	}

}
