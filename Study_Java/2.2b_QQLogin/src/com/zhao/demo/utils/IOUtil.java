package com.zhao.demo.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
		/**
		 * 注意：socket打开期间，和它相关的流不能关闭，关闭流则socket会关闭
		 */
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
		pw.println(values);
//		out.close();
//		osw.close();
//		pw.close();
	}
	
}
