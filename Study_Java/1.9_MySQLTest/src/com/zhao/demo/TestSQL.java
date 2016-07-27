package com.zhao.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestSQL {

	public static void main(String[] args) {
		
		/************************** 模拟查询数据库 *************************/
		 doQueryTest();

		/********************** 在数据库中添加用户名表(注册功能) *********************/
//		registTest();

	}

	private static void doQueryTest() {
		Connection cn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			Class.forName("org.gjt.mm.mysql.Driver");
			cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mytest", "zhaoyh", "123456");
			st = cn.createStatement();
			rs = st.executeQuery("select*from student");
			/**
			 * 这里用result获取ResultSetMetaDate来拿到列的数量。
			 * 原因：getRow()是获取当前指针位置的行号。
			 * getMetaData().getColumnCount()是直接获取列的数量。
			 */
			System.out.println("reslutSet的初始指针：" + rs.getRow());
			int k = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= k; i++) {
					System.out.print(rs.getString(i) + "   ");
				}
				System.out.println("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				cn.close();
				st.close();
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void registTest() {
		String u = "aaa";
		String p = "111";

		Connection cn = null;
		PreparedStatement ps = null;
		
		try {
			Class.forName("org.gjt.mm.mysql.Driver");
			cn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mytest", "zhaoyh", "123456");
			// 方法一 ，比较繁琐
			// String SQLStr = "insert into user(uid, pw) values('";
			// SQLStr += u ;
			// SQLStr += "', '" ;
			// SQLStr += p ;
			// SQLStr += "')";
			// System.out.println(SQLStr);
			// st.executeUpdate(SQLStr);
			// 方法二 ，简洁方便
			ps = cn.prepareStatement("insert into user(uid, pw) values(?,?)");
			ps.setString(1, u); // 对占位符设置值，占位符顺序从1开始
			ps.setString(2, p);
			int n = ps.executeUpdate();
			System.out.println("执行SQL语句受影响的行：" + n);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				cn.close();
				ps.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
