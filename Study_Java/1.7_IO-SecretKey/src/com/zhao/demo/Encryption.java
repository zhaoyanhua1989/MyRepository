package com.zhao.demo;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Encryption {

	public static void main(String[] args) {
		try {
			// 读取密钥文件
			int[] keys = new int[128];
			FileInputStream keyIn = new FileInputStream("E:/HM/temp/Key.key");
			int keyInN = keyIn.available();
			for (int i = 0; i < keyInN; i++) {
				keys[i] = keyIn.read();
			}
			// 文件加密
			FileInputStream in = new FileInputStream("E:/HM/temp/tp.jpg");
			FileOutputStream out = new FileOutputStream("E:/HM/temp/tp-Key.jpg");
			int inN = in.available();
			for (int i = 0; i < inN; i++) {
				out.write(in.read() + keys[i % 128]);
			}
			keyIn.close();
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
