package com.zhao.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class PropertiesUtil {

	private static PropertiesUtil mPropertiesUtil;
	FileReader fr = null;
	BufferedReader br = null;

	public static PropertiesUtil getInstance() {
		if (mPropertiesUtil == null) {
			synchronized (PropertiesUtil.class) {
				if (mPropertiesUtil == null) {
					mPropertiesUtil = new PropertiesUtil();
				}
			}
		}
		return mPropertiesUtil;
	}

	/**
	 * 在读取中文字符时，无法转码
	 * @param key
	 * @param filePath
	 * @return
	 */
	public String getValue(String key, String filePath) {
		String content = null;
		try {
			File file = new File(filePath);
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			while ((content = br.readLine()) != null) {
				if (content.contains(key)) {
					content = content.replace(key + "=", "");
					return content;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return content;
	}
}