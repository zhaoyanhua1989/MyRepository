package com.zhao.demo;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JPanel;

/**
 * 遇到问题：在坦克运动时，会出现不计算子弹是否打中坦克的情况
 */

public class MyPanel extends JPanel implements KeyListener, Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int k = 30;
	private int xs[] = new int[k];
	private int ys[] = new int[k];
	private Tank tank;

	public MyPanel(){
		for(int i=0;i<k;i++){
			xs[i] = (int)(Math.random()*1024);
			ys[i] = -(int)(Math.random()*768);
		}
		tank = new Tank(502,708, 20, 20);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.WHITE);
		for(int i=0; i<k; i++){
			g.drawString("*", xs[i], ys[i]);
		}
		g.fillRect(tank.x, tank.y, tank.width, tank.high);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:	//用编码code 37 也行
			tank.removeAddX(-5);
			break;
		case KeyEvent.VK_UP:	//用编码code 38 也行
			tank.removeAddY(-5);
			break;
		case KeyEvent.VK_RIGHT:	//用编码code 39 也行
			tank.removeAddX(5);
			break;
		case KeyEvent.VK_DOWN:	//用编码code 40 也行
			tank.removeAddY(5);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		out:
		while(true){
			for(int i=0;i<k;i++){
				ys[i] += 1;
				if(ys[i] == 768){
					xs[i] = (int)(Math.random()*1024);
					ys[i] = 0;
				}
			}
			for(int i=0;i<k;i++){ //判断子弹是否达到坦克了
				if(tank.isInTheRange(xs[i], ys[i])){
					break out;
				}
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repaint();
		}
	}

}
