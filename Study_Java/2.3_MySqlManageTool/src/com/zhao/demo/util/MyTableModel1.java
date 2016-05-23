package com.zhao.demo.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * 继承TableModel需要全部实现，实现的较多
 * @author HKW2962
 */
public class MyTableModel1 implements TableModel{

	private ResultSet rs = null;
	private ResultSetMetaData rsm = null;
	
	public MyTableModel1(ResultSet rs) {
		this.rs = rs;
		try {
			rsm = rs.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getRowCount() {
		// 返回总行数
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
		// 返回总列数
		int n = 0;
		try {
			n = rsm.getColumnCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return n;
	}

	@Override
	public String getColumnName(int columnIndex) {
		// 返回指定的列名
		String name = null;
		try {
			name = rsm.getColumnName(columnIndex + 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		// 返回每个列的数据类型，需注意数据库和Java类型之间的对应关系
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// 这个单元格是否允许编辑
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// 获取指定行，列的值
		Object value = null;
		try {
			rs.absolute(rowIndex + 1);
			value = rs.getObject(columnIndex + 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// 修改指定行、列的值
		
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		// 提供了一个时间注册机制，在正确注册事件后，表格发生改变时事件处理程序会工作
		
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// 移除事件注册
		
	}

}
