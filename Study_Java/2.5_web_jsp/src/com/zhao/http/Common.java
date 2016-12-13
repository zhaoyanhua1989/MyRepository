package com.zhao.http;

import java.io.InputStream;

public class Common {

	/**
	 * 获取post请求数据
	 * @param in InputStream
	 * @param size 长度
	 * @param charset 字符编码
	 * @return
	 */
	public static String getPostData(InputStream in, int size, String charset) {
		if (in != null && size > 0) {
			byte[] buf = new byte[size];
			try {
				in.read(buf);
				if (charset == null || charset.length() == 0) {
					return new String(buf);
				} else {
					return new String(buf, charset);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
