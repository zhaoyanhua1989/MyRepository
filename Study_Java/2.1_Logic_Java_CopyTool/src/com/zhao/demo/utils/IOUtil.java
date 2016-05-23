package com.zhao.demo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class IOUtil {

	public static void ByteWriteToCopy(File from, File to) throws IOException, FileNotFoundException {
		FileInputStream in = new FileInputStream(from);
		FileOutputStream out = new FileOutputStream(to, true);
		byte[] bytes = new byte[8192];
		int n = -1;
		while((n = in.read(bytes)) != -1) {
			out.write(bytes, 0, n);
		}
		out.close();
		in.close();
	}
	
}
