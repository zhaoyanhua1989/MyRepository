package com.zhao.demo;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Main {

	public static void main(String args[]){
		JFrame frame = new JFrame("鼠标控制小球测试");
		frame.setSize(500, 600);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
				System.exit(0);
			}
		});
		
		MyPanel panel = new MyPanel();
		frame.addMouseListener(panel);
		panel.addMouseListener(panel);
		frame.addMouseMotionListener(panel);
		panel.addMouseMotionListener(panel);
		
		frame.add(panel);
		
		frame.setVisible(true);
	}
	
}
