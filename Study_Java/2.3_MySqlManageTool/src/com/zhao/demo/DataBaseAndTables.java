package com.zhao.demo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

import com.zhao.demo.util.SQLUtil;

public class DataBaseAndTables extends JFrame implements ActionListener, MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<String> cmbDataBase = new JComboBox<String>(); // 显示数据库的下拉列表框
	private JList<String> listTable = new JList<String>(); // 显示表名
	private TableFrame tf;

	DataBaseAndTables() {
		this.setSize(300, 500);
		JScrollPane sp = new JScrollPane(listTable);
		this.setLayout(new BorderLayout());
		this.add(cmbDataBase, BorderLayout.NORTH);
		this.add(sp, BorderLayout.CENTER);

		//设置监听
		cmbDataBase.addActionListener(this);
		listTable.addMouseListener(this);

		// 显示内容
		showDataBaseAndTables();

	}

	private void showDataBaseAndTables() {
		String[] databases = SQLUtil.queryDatabases();
		for (int i = 0; i < databases.length; i++) {
			cmbDataBase.addItem(databases[i]);
		}
	}

	private void showTables(String dataBase) {
		String[] tables = SQLUtil.queryTables(dataBase);
		listTable.setListData(tables);
	}

	public static void main(String[] args) {
		DataBaseAndTables m = new DataBaseAndTables();
		m.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		showTables((String) cmbDataBase.getSelectedItem());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("listTable-Value:" + listTable.getSelectedValue());
		if(tf == null) {
			tf = new TableFrame((String) cmbDataBase.getSelectedItem(), listTable.getSelectedValue(), this.getWidth());
			tf.setVisible(true);
		} else {
			tf.setVisible(false);
			tf = new TableFrame((String) cmbDataBase.getSelectedItem(), listTable.getSelectedValue(), this.getWidth());
			tf.setVisible(true);
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
