package com.zhao.demo.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SQLUtil {

	public static boolean DoLoginQueryAndCheck(String str1, String str2) {
		if(StringUtil.isNull(str1) || StringUtil.isNull(str2)) 
			return false;
		boolean checkResult = false;
		Connection cn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("org.gjt.mm.mysql.Driver");
			cn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mytest", "zhaoyh", "123456");
			ps = cn.prepareStatement("select uid, pw from user where uid=? and pw=?");
			ps.setString(1, str1);
			ps.setString(2, str2);
			rs = ps.executeQuery();
			checkResult = rs.next();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				cn.close();
				ps.close();
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return checkResult;
	}

}
