package com.zhao.demo;

import java.awt.*;
import java.awt.event.*;

public class Stars {

	public static void main(String[] args){
		Frame w = new Frame("满天星星");
		w.setSize(1024, 768);
		w.setBackground(Color.BLACK);
		
		//增加窗口监听事件，使用内部方法，并用监听器的默认适配器
		//Frame不同于JFrame，只能用WindowListener来关闭，不能用WindowAdapter关不
		w.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				//重写窗口关闭事件
				System.exit(0);
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		MyPanel mp = new MyPanel();
		w.add(mp);
		
		Thread t = new Thread(mp);
		t.start();
		
		w.setVisible(true);
	}
	
}

class MyPanel extends Panel implements Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x = 1000;
	private int starsm = 300;
	private int[] xs = new int[starsm];
	private int[] ys = new int[starsm];
	
	public MyPanel(){
		for(int i=0;i<starsm;i++){
			xs[i] = (int)(Math.random()*1024);
			ys[i] = (int)(Math.random()*768);
		}
	}
	
	public void paint(Graphics g){
		g.setColor(Color.WHITE);
		for(int i=0;i<starsm;i++){
			g.drawString("*", xs[i], ys[i]);
		}
		g.drawString("@~", x, 30);
	}

	@Override
	public void run() {
		while(true){
			x-=8;	//流星
			System.out.println("x = " + x);
			if(x<=50){
				x = 1000;
			}
			for(int i=0;i<starsm;i++){  //漫天大雪
				ys[i] += 3;
				if(ys[i] == 768){
					ys[i] = 0;
				}
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repaint();
		}
	}
	
}