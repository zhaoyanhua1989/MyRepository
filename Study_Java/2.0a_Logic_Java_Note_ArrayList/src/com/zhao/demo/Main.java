package com.zhao.demo;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * 测试数组
 * @author HKW2962
 *
 */
public class Main {

	public static void main(String[] args) {
		JFrame frame = new JFrame("记事本");
		frame.setSize(300, 400);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
				System.exit(0);
			}
		});
		frame.setLocationRelativeTo(null);
		
		MyPanel mp = new MyPanel();
		frame.add(mp);
		
		mp.addKeyListener(mp);
		frame.addKeyListener(mp);
		
		frame.setVisible(true);
	}
	
}
