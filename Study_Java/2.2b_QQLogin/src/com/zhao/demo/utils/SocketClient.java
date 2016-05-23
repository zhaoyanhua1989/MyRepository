package com.zhao.demo.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient {

	private Socket s;
	private PrintWriter pw;
	private BufferedReader br;
	private String uid;

	public SocketClient(Socket s) {
		this.s = s;
		try {
			OutputStream os = s.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			pw = new PrintWriter(osw, true); //boolean自动刷新
			InputStream is = s.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return s;
	}

	public PrintWriter getPrintWriter() {
		return pw;
	}

	public BufferedReader getBufferedReader() {
		return br;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUid() {
		return uid;
	}

}
