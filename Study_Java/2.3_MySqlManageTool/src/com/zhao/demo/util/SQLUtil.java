package com.zhao.demo.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JTable;

public class SQLUtil {

	/**
	 * 查询所有数据库
	 * 
	 * @return 数据库名的String数组
	 */
	public static String[] queryDatabases() {
		Statement st = null;
		ResultSet rs = null;
		Connection cn = null;
		String strs[] = null;
		try {
			cn = DataBase.getInstance().getConnection();
			st = cn.createStatement();
			rs = st.executeQuery("show databases");
			rs.last();
			strs = new String[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; i < strs.length; i++) {
				rs.next();
				strs[i] = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				st.close();
				cn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return strs;
	}

	/**
	 * 查询所有表格
	 * 
	 * @param dataBase
	 * @return 表格名的String数组
	 */
	public static String[] queryTables(String dataBase) {
		Statement st = null;
		ResultSet rs = null;
		Connection cn = null;
		String strs[] = null;
		try {
			cn = DataBase.getInstance().getConnection();
			st = cn.createStatement();
			st.executeQuery("use " + dataBase);
			rs = st.executeQuery("show tables");
			rs.last();
			strs = new String[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; i < strs.length; i++) {
				rs.next();
				strs[i] = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				st.close();
				cn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return strs;
	}

	/**
	 * 查询单个表格，直接返回JTable
	 * 
	 * @param dataBase
	 *            数据库名
	 * @param tableName
	 *            表格名
	 * @return JTable
	 */
	public static JTable queryTableForJTable(String dataBase, String tableName) {
		Connection cn = null;
		Statement st = null;
		ResultSet rs = null;
		JTable jt = null;
		try {
			cn = DataBase.getInstance().getConnection(dataBase);
			st = cn.createStatement();
			rs = st.executeQuery("select * from " + tableName);
			int k = rs.getMetaData().getColumnCount();
			rs.last();
			jt = new JTable(rs.getRow() + 1, k);
			rs.beforeFirst();
			//显示表的列名 (表是从0行0列开始)
			for (int i = 0; i < k; i++) {
				jt.setValueAt(rs.getMetaData().getColumnName(i + 1), rs.getRow(), i);
			}
			//显示表的内容
			while (rs.next()) {
				for (int i = 1; i <= k; i++) {
					jt.setValueAt(rs.getObject(i), rs.getRow(), i - 1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				st.close();
				cn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jt;
	}
	
	/**
	 * 查询单个表格，直接返回MyTableModel
	 * 
	 * @param dataBase
	 *            数据库名
	 * @param tableName
	 *            表格名
	 * @return JTable
	 */
	public static MyTableModel1 queryTableForTableModel(String dataBase, String tableName) {
		Connection cn = null;
		Statement st = null;
		ResultSet rs = null;
		MyTableModel1 mtm = null;
		try {
			cn = DataBase.getInstance().getConnection(dataBase);
			st = cn.createStatement();
			rs = st.executeQuery("select * from " + tableName);
			mtm = new MyTableModel1(rs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			try {
//				rs.close();
//				st.close();
//				cn.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}
		return mtm;
	}

}
