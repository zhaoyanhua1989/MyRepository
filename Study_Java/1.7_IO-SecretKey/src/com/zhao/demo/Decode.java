package com.zhao.demo;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Decode {

	public static void main(String[] args) {
		try {
			// 读取密钥文件
			int[] keys = new int[128];
			FileInputStream inKey = new FileInputStream("E:/HM/temp/Key.key");
			int inKeyN = inKey.available();
			for (int i = 0; i < inKeyN; i++) {
				keys[i] = inKey.read();
			}
			// 解密文件并输出
			FileInputStream in = new FileInputStream("E:/HM/temp/tp-Key.jpg");
			FileOutputStream out = new FileOutputStream("E:/HM/temp/tp2.jpg");
			int inN = in.available();
			for (int i = 0; i < inN; i++) {
				out.write(in.read() - keys[i % 128]);
			}
			in.close();
			out.close();
			inKey.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
