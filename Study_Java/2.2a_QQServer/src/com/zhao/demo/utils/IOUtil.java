package com.zhao.demo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class IOUtil {

	/**
	 * 字符输入流
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String charsRead(InputStream is) throws IOException {
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String result = br.readLine();
//		is.close();
//		isr.close();
//		br.close();
		return result;
	}
	
	/**
	 * 字符输出流
	 * @param out
	 * @param values
	 * @throws IOException
	 */
	public static void charsWrite(OutputStream out, String values) throws IOException {
		OutputStreamWriter osw = new OutputStreamWriter(out);
		PrintWriter pw = new PrintWriter(osw, true);
		/**
		 * 切记这里不能直接使用write(String)
		 * 原因：println(String)比write(String)多调用了一次println(),打印了系统
		 * 分隔符(不同系统不一样)，如果要使用write(String),需要再调用一次println(), 不然read不到。
		 */
		pw.println(values);
//		out.close();
//		osw.close();
//		pw.close();
	}
	
}
