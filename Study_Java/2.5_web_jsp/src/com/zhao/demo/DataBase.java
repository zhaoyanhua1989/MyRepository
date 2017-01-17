package com.zhao.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;

public class DataBase {

	private static DataBase dataBase;

	public static DataBase getInstance() {
		if (dataBase == null) {
			synchronized (DataBase.class) {
				if (dataBase == null) {
					dataBase = new DataBase();
				}
			}
		}
		return dataBase;
	}

	public Connection getConnection(String filePath) {
		Connection cn = null;
		FileReader fr = null;
		BufferedReader br = null;
		try {
			File file = new File(filePath);
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String path = br.readLine();
			String url = br.readLine();
			String user = br.readLine();
			String pw = br.readLine();
			System.out.println("path=" + path + ",url=" + url + ",user=" + user + ",pw=" + pw);
			Class.forName(path);
			cn = DriverManager.getConnection(url, user, pw);
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
		return cn;
	}
}
