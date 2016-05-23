package com.zhao.demo;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class Balls {

	public static void main(String[] args) {
		JFrame frame = new JFrame("碰撞小球");
		frame.setSize(1024, 768);
//		frame.setBackground(Color.BLACK);	//这句没有作用
		
		frame.addWindowListener(new WindowAdapter() {
		
			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
				System.exit(0);
			}
			
		});
		
		//JFrame和Frame设置背景颜色有区别，JFrame需要通过JPanel来设置
		MyPanel myPanel = new MyPanel();
		myPanel.setBackground(Color.BLACK);
		frame.add(myPanel);

		Thread thread = new Thread(myPanel);
		thread.start();
//		thread.setPriority(Thread.NORM_PRIORITY);	//设置线程的优先级，main线程是norm
		
		frame.setVisible(true);
		
	}
	
}