package com.zhao.demo;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.zhao.demo.util.MyTableModel1;
import com.zhao.demo.util.SQLUtil;

public class TableFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TableFrame(String dataBase, String tableName, int frameX) {
		this.setSize(700, 500);
		this.setLocation(frameX, 0);
		this.setTitle(tableName);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
				System.exit(0);
			}
		});

		showTable(dataBase, tableName);
	}

	private void showTable(String dataBase, String tableName) {
		/*******************面向过程的思路*********************/
		/*
		 * 优点：可以关闭ResultSet释放内存
		 * 缺点：实现较复杂
		 */
//		JTable jt = SQLUtil.queryTableForJTable(dataBase, tableName);
//		this.add(jt);
		/*******************面向对象的思路*********************/
		/*
		 * 优点：实现逻辑较简单
		 * 缺点：不能关闭ResultSet释放内存 
		 */
		MyTableModel1 mm = SQLUtil.queryTableForTableModel(dataBase, tableName);
		JTable jt = new JTable(mm);
		JScrollPane jp = new JScrollPane(jt);
		this.add(jp);
	}

}
