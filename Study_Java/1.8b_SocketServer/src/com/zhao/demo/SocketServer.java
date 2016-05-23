package com.zhao.demo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(8000);
			System.out.println("监听前...");
			Socket s = ss.accept();
//			int msg = s.getInputStream().read();
			/**
			 * 注意，这里写入字符用字符流，FileReader和FileWriter虽然也是字符流，但是是针对文件的
			 * 这里用outputStreamWriter和inputStreamReader
			 */
			//服务端读取客户端发送过来的消息
			InputStream is = s.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String msg = br.readLine();
			System.out.println("从客户端接收的消息： " + msg);
			//服务端向客户端发送消息
			OutputStream os = s.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			PrintWriter pw = new PrintWriter(osw, true);
			pw.println("这是" + msg);

			System.out.println("监听后...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
