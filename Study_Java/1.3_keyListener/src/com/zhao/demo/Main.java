package com.zhao.demo;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		JFrame f = new JFrame("闪电");
		f.setSize(1024, 768);
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
				System.exit(0);
			}
		});
		
		MyPanel p = new MyPanel();
		p.setBackground(Color.BLACK);
		
		f.add(p);
		f.addKeyListener(p);
		p.addKeyListener(p);
		
		Thread t = new Thread(p);
		t.start();
		
		f.setVisible(true);
	}
	
}
