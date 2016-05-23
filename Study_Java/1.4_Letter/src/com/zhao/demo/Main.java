package com.zhao.demo;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		JFrame frame = new JFrame("消灭字母");
		frame.setSize(300, 400);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
				System.exit(0);
			}
		});
		
		MyPanel panel = new MyPanel();
		panel.setBackground(Color.BLACK);
		frame.add(panel);
		
		//设置键盘监听
		frame.addKeyListener(panel);
		panel.addKeyListener(panel);
		
		Thread thread = new Thread(panel);
		thread.start();
		
		frame.setVisible(true);
	}
	
}
