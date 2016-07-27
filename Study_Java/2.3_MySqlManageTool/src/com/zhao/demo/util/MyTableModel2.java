package com.zhao.demo.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;

/**
 * 继承AbstractTableModel，实现方法较少，有需要自己添加
 * @author HKW2962
 */
public class MyTableModel2 extends AbstractTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ResultSet rs = null;
	private ResultSetMetaData rsm = null;

	public MyTableModel2(ResultSet rs) {
		this.rs = rs;
		try {
			rsm = rs.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getRowCount() {
		int n = 0;
		try {
			rs.last();
			n = rs.getRow();
			rs.beforeFirst();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return n;
	}

	@Override
	public int getColumnCount() {
		int n = 0;
		try {
			n = rsm.getColumnCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return n;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object value = null;
		try {
			rs.absolute(rowIndex + 1);
			value = rs.getObject(columnIndex + 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

}
