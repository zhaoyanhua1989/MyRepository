package com.zhao.demo;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Main {

	public static void main(String args[]) {
		JFrame frame = new JFrame("按钮测试");
		frame.setSize(300, 400);

		JButton b1 = new JButton("OK1");
		JButton b2 = new JButton("OK2");
		JButton b3 = new JButton("OK3");
		JButton b4 = new JButton("OK4");
		JButton b5 = new JButton("OK5");

		//--------------------------------布局练习-----------------------------------
		
		// 设置布局
		// frame.setLayout(new BorderLayout()); //边框布局,把屏幕分成5块,东西南北中,中间默认最大化
		// frame.add(b1, BorderLayout.NORTH);
		// frame.add(b2, BorderLayout.SOUTH);
		// frame.add(b3, BorderLayout.WEST);
		// frame.add(b4, BorderLayout.EAST);
		// frame.add(b5, BorderLayout.CENTER);

		// frame.setLayout(new GridLayout(2,3)); //表格布局,平分大小
		// frame.add(b1);
		// frame.add(b2);
		// frame.add(b3);
		// frame.add(b4);
		// frame.add(b5);

		// frame.setLayout(new FlowLayout()); //流式布局，尽量少占空间，默认放在第一排
		// frame.add(b1);
		// frame.add(b2);
		// frame.add(b3);
		// frame.add(b4);
		// frame.add(b5);

		//--------------------------------布局练习结束---------------------------------
		
		//--------------------------------登录菜单begin----------------------------------
		//分辨率250*125
//		JPanel panInput = new JPanel();		//设置输入模块
//		panInput.setLayout(new GridLayout(2, 2));
//		panInput.add(new JLabel("用户名"));
//		panInput.add(new JTextField());
//		panInput.add(new JLabel("密码"));
//		panInput.add(new JPasswordField());
//		
//		JPanel panButton = new JPanel();	//设置按钮模块
//		panButton.setLayout(new FlowLayout());
//		panButton.add(new JButton("登录"));
//		panButton.add(new JButton("注册"));
//		panButton.add(new JButton("取消"));
//
//		//将模块添加到frame
//		frame.setLayout(new BorderLayout());
//		frame.add(panInput, BorderLayout.CENTER);
//		frame.add(panButton, BorderLayout.SOUTH);
		
		//--------------------------------登录菜单end----------------------------------
		
		
		//--------------------------------聊天窗口begin--------------------------------
		
		
		JPanel panButton = new JPanel();
		panButton.setLayout(new GridLayout(1, 2));
		panButton.add(new JComboBox());
		panButton.add(new JButton("发送"));
		
		JPanel panInput = new JPanel();
		panInput.setLayout(new BorderLayout());
		panInput.add(new JTextArea(), BorderLayout.CENTER);
		panInput.add(new JScrollPane(), BorderLayout.EAST);	//滚动条
		
		JPanel inputAll = new JPanel();
		inputAll.setLayout(new GridLayout(2, 1));
		inputAll.add(panButton);
		inputAll.add(panInput);
		
		
		frame.setLayout(new BorderLayout());
		frame.add(new JTextArea(), BorderLayout.CENTER);
		frame.add(inputAll, BorderLayout.SOUTH);
		
		//--------------------------------聊天窗口end----------------------------------
		
		frame.setVisible(true);
		
	}

}
