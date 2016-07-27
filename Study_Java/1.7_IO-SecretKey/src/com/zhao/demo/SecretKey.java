package com.zhao.demo;

import java.io.FileOutputStream;

public class SecretKey {

	public static void main(String[] args) {
		try {
			// 随机生成128位的密钥文件，.key非文本文件，只做一组数
			FileOutputStream out = new FileOutputStream("E:/HM/temp/Key.key");
			for (int i = 0; i < 128; i++) {
				out.write((int) (Math.random() * 128));
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
